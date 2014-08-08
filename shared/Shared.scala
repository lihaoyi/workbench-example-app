package example

import scala.annotation.ClassfileAnnotation


trait Api{
  def list(path: String): Seq[String]
}