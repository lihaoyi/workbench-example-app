package example

import scala.collection.{SortedMap, mutable}
import scalatags._
import scala.util.Random
import scalatags.all._
import rx._
import rx.core.{Propagator, Obs}
import org.scalajs.dom
import org.scalajs.dom.DOMParser
import scala.scalajs.js

/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
object Framework {

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

    override def transforms = {
      Array((children, attrs) => Mod.Attr("id", elemId))
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

    override def transforms = {
      Array((children, attrs) => Mod.Attr("id", elemId))
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
      override def transforms = {

        Array(
          (children, attrs) => Mod.Attr("id", attrs.getOrElse("id", ""+Random.nextInt())),
          (children, attrs) => {
            val elemId = attrs("id")
            val funcName = a.name + "Func"

            dom.setTimeout(() => {
              val target = dom.document
                .getElementById(elemId)
                .asInstanceOf[js.Dynamic]
              if (target != null)
                target.updateDynamic(funcName)(func: js.Function0[Unit])
            }, 10)
            Mod.Attr(a.name, s"this.$funcName(); return false;")
          }
        )
      }
    }
    def <~ (func: => Unit) = new CallbackModifier(a, () => func)
  }

}
