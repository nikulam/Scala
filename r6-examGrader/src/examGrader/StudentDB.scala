
package examGrader

/**
 * A very simple, abstract student data base query interface specification.
 */
abstract class StudentDB {
  /** Get the set of student ids in the data base */
  def students: Set[StudentId]
  /** The first name of a student */
  def firstNameOf(id: StudentId): String
  /** The last name of a student */
  def lastNameOf(id: StudentId): String
  /** The study programme of a student */
  def programmeOf(id: StudentId): StudyProgramme
}

/**
 * A student data base for testing.
 */
class TestStudentDB(entries: Seq[Tuple4[StudentId, String, String, StudyProgramme]]) extends StudentDB {
  // Information on individual students.
  private class StudentInfo(val id: StudentId, val firstName: String, val lastName: String,
    val prog: StudyProgramme) {
  }
  private object StudentInfo {
    def apply(id: StudentId, firstName: String, lastName: String, prog: StudyProgramme) =
      new StudentInfo(id, firstName, lastName, prog)
  }
  // The actual "database"
  private val db: Map[StudentId, StudentInfo] = entries.map({ case (id, fn, ln, prog) => (id, StudentInfo(id, fn, ln, prog)) }).toMap
  // Accessors.
  lazy val students: Set[StudentId] = db.keys.toSet
  def programmeOf(id: StudentId): StudyProgramme = db(id).prog
  def firstNameOf(id: StudentId): String = db(id).firstName
  def lastNameOf(id: StudentId): String = db(id).lastName
  override def toString: String = {
    db.keys.toSeq.sorted.map(id => id + " : " + firstNameOf(id) + " : " + lastNameOf(id) + " : " + programmeOf(id)).mkString("\n")
  }
}

/**
 * Companion object for constructing databases, just for testing.
 */
object TestStudentDB {
  def apply(entries: Seq[Tuple4[StudentId, String, String, StudyProgramme]]) = new TestStudentDB(entries)
}

