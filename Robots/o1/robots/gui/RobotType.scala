  
////////////////// NOTE TO STUDENTS //////////////////////////
// For the purposes of our course, it's not necessary    
// that you understand or even look at the code in this file.
//////////////////////////////////////////////////////////////

package o1.robots.gui


import o1.robots.RobotBody
import o1.robots.RobotBrain
import o1.util.assignments._
import scala.util.Random


/** The class `RobotType` represents instantiable kinds of robots whose availability
  * the Robots user interface checks dynamically.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this class works or can be used.''' */
sealed abstract class RobotType(className: String, additionalParameterTypes: Class[_]*) 
    extends DynamicClass[RobotBrain](className, Array(classOf[String], classOf[RobotBody]) ++ additionalParameterTypes) {
  
  def apply(constructorArguments: Any*) = Some(this.instantiate(constructorArguments.map(arg): _*))  
  
  def instantiateRandom(name: String, body: RobotBody): RobotBrain
  
}
     
   

/** This companion object of class `RobotsType` provides access to statically defined instances of the class.
  *
  * '''NOTE TO STUDENTS: In this course, you don't need to understand how this object works or can be used.''' */
object RobotType {

  trait Basic {
    self: RobotType =>
    def instantiateRandom(name: String, body: RobotBody) = this.instantiate(arg(name), arg(body))
  }
  
  case object Spinbot    extends RobotType("o1.robots.Spinbot") with Basic
  
  case object Nosebot    extends RobotType("o1.robots.Nosebot") with Basic
  
  case object Psychobot  extends RobotType("o1.robots.Psychobot") with Basic
  
  case object Staggerbot extends RobotType("o1.robots.Staggerbot", classOf[Int]) {
    override def instantiateRandom(name: String, body: RobotBody) = this.instantiate(arg(name), arg(body), arg(Random.nextInt()))
  }
  
  case object Lovebot    extends RobotType("o1.robots.Lovebot", classOf[RobotBody]) {      
    override def instantiateRandom(name: String, body: RobotBody) = {
      val bots = body.world.robotList
      this.instantiate(arg(name), arg(body), arg(bots(Random.nextInt(bots.size))))
    }
  }
  
  val All = Seq(Spinbot, Nosebot, Psychobot, Staggerbot, Lovebot)
  
}