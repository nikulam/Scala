
package examGrader
import scala.collection.immutable._

/**
 * An immutable class that provides handy methods for obtaining statistics of
 * an exam of a course.
 * Please see the UnitTests class for usage examples.
 * @param studentDB is the student database, associating non-course related information
 *   (name, study programme etc) to each student id
 * @param homeworkDone is the set of students who have done their homework and thus
 *   the set of students whose exam will be graded.
 *   The students who have not completed the homework do not get ANY grade, not even 0.
 * @param bonusPoints associates bonus points to some of the students
 * @param examPoints maps each student id that has taken the exam to a list of exam assignment points.
 * @param gradeLimits is the list of lower points for grades 1, 2, 3, 4, and 5.
 *   For example, if gradeLimits = List(30,36,42,48,54) and a student gets 36 points, then s/he
 *   will get the grade 2 (provided that the homework is done, of course).
 *   On the other hand, if a student gets 27 points, then his/her grade will be 0 (failed).
 *
 * NOTE: this is an exercise on functional programming style.
 * Therefore, you are NOT allowed to use mutable data structures or vars.
 */
class ExamGrader(val studentDB: StudentDB,
  val homeworkDone: Seq[StudentId],
  val bonusPoints: Map[StudentId, Int],
  val examPoints: Map[StudentId, List[Int]],
  val gradeLimits: List[Int]) {

  // Check that the student ids in other lists are also in studentDB
  homeworkDone.foreach(id => require(studentDB.students.contains(id), "Unknown student " + id + " in homework list"))
  bonusPoints.keys.foreach(id => require(studentDB.students.contains(id), "Unknown student " + id + " in bonus points list"))
  examPoints.keys.foreach(id => require(studentDB.students.contains(id), "Unknown student " + id + " in exam points list"))
  // Check that the gradeLimits list is of correct length and that the grade limits are increasing.
  require(gradeLimits.length == 5 && (gradeLimits zip gradeLimits.tail).forall(p => p._1 < p._2))

  /**
   * The list of students whose exam is graded, i.e. those that
   * appear in both examPoints and homeworkDone.
   * Hints: the methods "keys" of Map as well as "filter" and "toSet" of Seq/List/etc may be useful.
   * Note: "lazy" means that this is computed when needed for the first time, not necessarily
   * immediately when an object of this class is created.
   */
  lazy val graded: Set[StudentId] = {
    val koe = this.examPoints.keys
    this.homeworkDone.filter(n => koe.exists(_ == n)).toSet
    
  }

  /**
   * Return the grade (0, 1, 2, 3, 4, or 5) that is obtained with n points.
   * See the explanation in the comments/documentation of the class above.
   */
  def pointsToGrade(n: Int): Int = {
   if(gradeLimits.exists(_ <= n))
     gradeLimits.lastIndexWhere(_ <= n) + 1
   else 0
  }
  
  /**
   * The sum of bonus and exam assignment points of a student.
   * May only be called for students who have exam points.
   * But note that a student may not appear on the list of bonus points receivers, meaning
   * that s/he will have 0 bonus points.
   */
  def points(id: StudentId): Int = {
    require(examPoints.contains(id))
    if(bonusPoints.contains(id)) examPoints(id).sum + bonusPoints(id)
    else examPoints(id).sum
    }

  /**
   * The grades of the students whose exam is graded.
   * The grade of a student is determined by computing the sum of his/her bonus and exam points,
   * and then taking the grade from the gradeLimits.
   */
  lazy val grades: Map[StudentId, Int] = {
    this.graded.zip(graded).map(n => (n._1, pointsToGrade(points(n._2)))).toMap
  }
  
  /**
   * Return the n best students, best first, with respect to the points obtained
   * (again, only the students whose exam is graded are taken into account).
   * Breaking ties can be arbitrary: for instance, if 5 students got full points and 3 best
   * students are requested, you can return any 3 of those 5.
   */
  def bestStudents(n: Int): Seq[StudentId] = {
    val pisteet = this.graded.map(n => (n, points(n))).toVector
    pisteet.sortBy(_._2).takeRight(n).map(_._1).toSeq.reverse
  }

  /**
   * Return a map associating each grade {0,...,5} to the set of students who obtained that grade.
   * If no students obtained some grade (e.g. 3), then an empty set should be associated to it.
   */
  def studentsByGrade: Map[Int, Set[StudentId]] = {
    val setti = Vector(0, 1, 2, 3, 4, 5)
    setti.map(n => (n, this.grades.filter(_._2 == n).keys.toSet)).toMap
    
  }
  
  /**
   * The set of study programmes whose students took the exam and whose exam was graded.
   */
  def gradedProgrammes: Set[StudyProgramme] = {
    graded.map(studentDB.programmeOf(_))
  }

  /**
   * Associate each study programme whose students were graded in the exam to
   * a sub-map of "grades" restricted to the students of that programme.
   * For those study programs whose students were not graded the resulting map
   * should be undefined.
   */
  def gradesByProgramme: Map[StudyProgramme, Map[StudentId, Int]] = {
   grades.groupBy(n => studentDB.programmeOf(n._1))
  }

}

