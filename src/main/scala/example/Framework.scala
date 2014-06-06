package example

import scala.collection.{SortedMap, mutable}
import scalatags.JsDom._
import scala.util.Random
import all._
import rx._
import rx.core.{Propagator, Obs}
import org.scalajs.dom
import org.scalajs.dom.{Element, DOMParser}
import scala.scalajs.js
import scalatags.generic

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


  implicit class DomMod(val r: dom.HTMLElement) extends Node{
    override def writeTo(strb: dom.Element): Unit = {
      strb.appendChild(r)
    }
  }

  /**
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit def rxMod[T <: dom.HTMLElement](r: Rx[HtmlTag]): Modifier = {
    var last = r().toDom
    Obs(r, skipInitial = true){
      val newLast = r().toDom
      last.parentElement.replaceChild(newLast, last)
      last = newLast
    }
    new DomMod(last)
  }
  implicit class RxAttrVal[T](r: Rx[T])(implicit f: T => AttrVal) extends AttrVal {
    def merge(o: generic.AttrVal[Element]) = ???
    def applyPartial(t: Element) = ???
    override def applyTo(t: Element, k: generic.Attr) = {
      Obs(r){
        r().applyTo(t, k)
      }
    }
  }
  implicit class RxAttrStyle[T](r: Rx[T])(implicit f: T => StyleVal) extends StyleVal {
    override def applyTo(t: Element, k: generic.Style) = {
      Obs(r){
        r().applyTo(t, k)
      }
    }
  }
}
