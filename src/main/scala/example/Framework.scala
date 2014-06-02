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

  def render[T <: dom.HTMLElement](t: TypedHtmlTag[T]): T = {
    val elem = dom.document.createElement(t.tag).asInstanceOf[T]
    val moddedAttrs = t.collapsedAttrs

    moddedAttrs.foreach{
      case (k, x: Function0[_]) =>
        dom.console.log("F0")
        elem.asInstanceOf[js.Dynamic].updateDynamic(k)(x)
      case (k, x: Function1[_, _]) =>
        dom.console.log("F1")
        elem.asInstanceOf[js.Dynamic].updateDynamic(k)(x)
      case (k, v) => elem.setAttribute(k, v.toString)
    }
    t.children.reverse.foreach{
      case d: DomMod => elem.appendChild(d.r)
      case t: TypedHtmlTag[`dom`.HTMLElement] => elem.appendChild(render(t))
      case s: StringNode => elem.appendChild(dom.document.createTextNode(s.v))
      case r: RawNode =>
        val div = dom.document.createElement("div")
        div.innerHTML = r.v
        elem.appendChild(div)
    }
    elem
  }

  /**
   * Wraps reactive strings in spans, so they can be referenced/replaced
   * when the Rx changes.
   */
  implicit def RxStr(r: Rx[String]): Modifier = {
    rxMod(Rx(span(r())))
  }


  implicit class DomMod(val r: dom.HTMLElement) extends Node{
    override def writeTo(strb: StringBuilder): Unit = {
      strb.append(r.outerHTML)
    }
  }

  /**
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit def rxMod[T <: dom.HTMLElement](r: Rx[TypedHtmlTag[T]]): Modifier = {
    var last: dom.HTMLElement = render(r())
    dom.console.log("A")
    lazy val obs: Obs = Obs(r, skipInitial = true){
      dom.console.log("B")
      val newLast = render(r())
      last.parentElement.replaceChild(last, newLast)
      last = newLast
    }
    new DomMod(last)
  }

  /**
   * Lets you stick Scala callbacks onto onclick and other onXXXX
   * attributes using Scala callbacks, monkey-patching the callback
   * object onto the element itself to avoid it getting prematurely
   * garbage-collected.
   */
  implicit class Transformable(a: Attr){
    class CallbackModifier(a: Attr, func: () => Unit) extends Modifier{
      override def transforms = Array(Mod.Attr(a.name, func))
    }
    def apply (func: => Unit) = new CallbackModifier(a, () => func)
  }
}
