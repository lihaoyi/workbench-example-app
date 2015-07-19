package example


trait Api{
  def list(path: String): Seq[String]
}