
package object suffixBuild {
  def suffixArray(s: String): Seq[Int] = (0 until s.length).map(n => (s.drop(n), n)).sortBy(_._1).map(_._2)
}


