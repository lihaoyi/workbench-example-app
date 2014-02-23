package example

import scala.collection.mutable
import scalatags.{HtmlTag, Attr, Modifier}
import scala.util.Random
import scalatags.all._
import scalatags.HtmlTag
import rx._
import rx.core.{Propagator, Obs}
import org.scalajs.dom
import org.scalajs.dom.DOMParser
import scala.scalajs.js
import scalatags.HtmlTag

/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
object Framework {
  /**
   * Lets you put Unit into a scalatags tree, as a no-op.
   */
  implicit def UnitModifier(u: Unit) = new scalatags.Modifier{
    def transform(t: scalatags.HtmlTag) = t
  }

  /**
   * Wraps reactive strings in spans, so they can be referenced/replaced
   * when the Rx changes.
   */
  implicit def RxStr(r: Rx[String]): Modifier = {
    new RxMod(Rx(span(r())))
  }

  /**
   * Sticks an ID to a HtmlTag if it doesnt already have one, so we can refer
   * to it later.
   */
  class DomRef[T](r0: HtmlTag) extends Modifier{
    val elemId = r0.attrs.getOrElse("id", ""+Random.nextInt())
    val r = r0(id := elemId)
    def transform(tag: HtmlTag): HtmlTag = {
      tag(r)
    }
  }

  implicit def derefDomRef[T](d: DomRef[T]) = {
    dom.document.getElementById(d.elemId).asInstanceOf[T]
  }

  /**
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit class RxMod(r: Rx[HtmlTag]) extends Modifier{
    val elemId = r.now.attrs.getOrElse("id", ""+Random.nextInt())
    lazy val obs: Obs = Obs(r, skipInitial = true){
      dom.console.log("Obs fire!", elemId)
      val target = dom.document.getElementById(elemId)
      val element = dom.document.createElement("div")
      element.innerHTML = r.now(
        id := elemId
      ).toString()
      if (target != null){
        target.parentElement.replaceChild(element.children(0), target)
      }else{
        obs.kill()
        r.kill()
      }
    }

    dom.setTimeout(() => {
      target.asInstanceOf[js.Dynamic].obs = obs.asInstanceOf[js.Dynamic]
    }, 10)

    def transform(tag: HtmlTag): HtmlTag = {
      tag(
        r.now(
          id := elemId
        )
      )
    }
  }

  /**
   * Lets you stick Scala callbacks onto onclick and other onXXXX
   * attributes using Scala callbacks, monkey-patching the callback
   * object onto the element itself to avoid it getting prematurely
   * garbage-collected.
   */
  implicit class Transformable(a: Attr){
    class CallbackModifier(a: Attr, func: () => Unit) extends Modifier{
      def transform(tag: HtmlTag): HtmlTag = {
        val elemId = tag.attrs.getOrElse("id", ""+Random.nextInt())

        dom.setTimeout(() => {
          val target = dom.document
                          .getElementById(elemId)
                          .asInstanceOf[js.Dynamic]
          if (target != null)
            target.func = func: js.Function0[Unit]
        }, 10)
        tag(id:=elemId, a:=s"this.func(); return false;")
      }
    }
    def <~ (func: => Unit) = new CallbackModifier(a, () => func)
  }

  class StagedSet[T](val pair: (Var[T], T)){
    def set() = {
      pair._1.updateSilent(pair._2)
    }
  }
  implicit class Stager[T](val v: Var[T]){
    def ~>(t: T) = new StagedSet(v, t)
  }
  implicit class Multisetable(v: Var.type){
    def set[P: Propagator](stages: StagedSet[_]*) = {
      stages.foreach(_.set())
      Propagator().propagate(
        stages.flatMap( s => s.pair._1.children.map(s.pair._1 -> _)).toSet
      )
    }
  }
}
