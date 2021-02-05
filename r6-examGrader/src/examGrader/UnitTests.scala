
package examGrader

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 */
class UnitTests {
  def isInt(s: String): Boolean = s.forall(_.isDigit)
  def parseDSV[T](decoder: (String, Char) => T)(fileAsString: String): Seq[T] = {
    import scala.collection.mutable.ArrayBuffer
    val reader = new java.io.BufferedReader(new java.io.StringReader(fileAsString))
    val separator: Char = ':'
    val lines = ArrayBuffer[String]()
    var line = reader.readLine()
    while (line != null) {
      line = line.trim()
      if (line.nonEmpty) lines += line
      line = reader.readLine()
    }
    lines.map(l => decoder(l, separator)).toSeq
  }
  val decodeDbLine =
    (line: String, separator: Char) => {
      val data = line.split(separator).map(s => s.trim())
      val id: StudentId = data(0)
      val firstName = data(1)
      val lastName = data(2)
      val programme: StudyProgramme = data(3)
      (id, firstName, lastName, programme)
    }
  val decodeIdLine =
    (line: String, separator: Char) => {
      val data = line.split(separator).map(s => s.trim())
      val id: StudentId = data(0)
      id
    }
  val decodeBonusPointLine =
    (line: String, separator: Char) => {
      val data = line.split(separator).map(s => s.trim())
      val id: StudentId = data(0)
      val points = data(1).toInt
      (id, points)
    }
  val decodeExamResultLine =
    (line: String, separator: Char) => {
      val data = line.split(separator).map(s => s.trim())
      val id: StudentId = data(0)
      data.tail.foreach(p => require(isInt(p), "Exam result line contains non-numeric field " + p))
      val points = data.tail.map(p => p.toInt).toList
      (id, points)
    }


  val dbDSV = """
123456 : Ed : Ykskivi : Tik
829399 : Tiina : Aide : Muoti
783663 : Henrietta : Akkeri : Tik
834533 : Sami :  Skaalaaja : Tik
111111 : Aino : Ykkönen : Tfy
549874 : Harri : Haskell : Inf
"""

  lazy val homeworkDoneDSV = """
123456
829399
783663
111111
549874
"""

  lazy val bonusPointsDSV = """
123456 : 4
834533 : 2
"""

  lazy val examPointsDSV = """
123456 : 5 : 3 : 6 : 3 : 2
829399 : 3 : 5 : 6 : 2 : 4
783663 : 5 : 3 : 6 : 3 : 2
834533 : 1 : 1 : 6 : 5 : 2
111111 : 5 : 4 : 6 : 6 : 0
"""

  lazy val db = TestStudentDB(parseDSV(decodeDbLine)(dbDSV))
  lazy val homeworkDone = parseDSV(decodeIdLine)(homeworkDoneDSV).toList
  lazy val bonusPoints = parseDSV(decodeBonusPointLine)(bonusPointsDSV).toMap
  lazy val examPoints = parseDSV(decodeExamResultLine)(examPointsDSV).toMap

  val gradeLimits = List(10, 14, 18, 22, 26)

  lazy val grader = new ExamGrader(db, homeworkDone, bonusPoints, examPoints, gradeLimits)

  @Test def testGraded() {
    assertEquals("set of graded students", Set("111111", "123456", "783663", "829399"), grader.graded)
  }
  @Test def testPointsToGraded() {
    assertEquals("grade for 16 points", 2, grader.pointsToGrade(16))
  }
  @Test def testPoints() {
    assertEquals("student 783663 points", 19, grader.points("783663"))
  }
  @Test def testPoints2() {
    assertEquals("student 834533 points", 17, grader.points("834533"))
  }
  @Test def testGrading() {
    assertEquals("student 111111 grade", 3, grader.grades("111111"))
  }
  @Test def testBestStudents() {
    assertEquals("best 3 students", List("123456", "111111", "829399"), grader.bestStudents(3))
  }
  @Test def testStudentsByGrade() {
    assertEquals("students with grade 3", Set("111111", "783663", "829399"), grader.studentsByGrade(3))
  }
  @Test def testGradeByProgramme() {
    assertEquals("graded in Tik", Map("783663" -> 3, "123456" -> 4), grader.gradesByProgramme("Tik"))
  }
}

