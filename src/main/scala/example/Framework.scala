package example

import scala.collection.{SortedMap, mutable}
import scalatags.JsDom._
import scala.util.{Failure, Success, Random}
import all._
import rx._
import rx.core.{Propagator, Obs}
import org.scalajs.dom
import org.scalajs.dom.{Element, DOMParser}
import scala.scalajs.js


/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
object Framework {

  /**
   * Wraps reactive strings in spans, so they can be referenced/replaced
   * when the Rx changes.
   */
  implicit def RxStr[T](r: Rx[T])(implicit f: T => Modifier): Modifier = {
    rxMod(Rx(span(r())))
  }

  /**
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit def rxMod[T <: dom.HTMLElement](r: Rx[HtmlTag]): Modifier = {
    def rSafe = r.toTry match {
      case Success(v) => v.render
      case Failure(e) => span(e.toString, backgroundColor := "red").render
    }
    var last = rSafe
    Obs(r, skipInitial = true){
      val newLast = rSafe
      last.parentElement.replaceChild(newLast, last)
      last = newLast
    }
    bindElement(last)
  }
  implicit def RxAttrValue[T: scalatags.JsDom.AttrValue] = new scalatags.JsDom.AttrValue[Rx[T]]{
    def apply(t: Element, a: Attr, r: Rx[T]): Unit = {
      Obs(r){ implicitly[scalatags.JsDom.AttrValue[T]].apply(t, a, r())}
    }
  }
  implicit def RxStyleValue[T: scalatags.JsDom.StyleValue] = new scalatags.JsDom.StyleValue[Rx[T]]{
    def apply(t: Element, s: Style, r: Rx[T]): Unit = {
      Obs(r){ implicitly[scalatags.JsDom.StyleValue[T]].apply(t, s, r())}
    }
  }

}
