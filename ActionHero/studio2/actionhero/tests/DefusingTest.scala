package studio2.actionhero.tests

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.Assertions._
import studio2.actionhero._
import scala.Vector

/**
 * This test allows you to test your solution. It's the EXACTLY same test we have on server.
 */

@RunWith(classOf[JUnitRunner])
class DefusingTest extends FlatSpec with Matchers {

  
    "Defusing.perform" should "(10p) catch all the slight mishaps." in {
      val b = new TestRedFirstBomb
      
      // If exceptions are thrown, this test fails
      
      ImaginaryActsOfCourage.defuse(b)
      
      // And in case you did nothing it fails as well
      
      assert(b.parts(1).accessed, "There was no attempt to remove the part that would have thrown the exception.")      
    }

    "Defusing.perform" should "(10p) not catch the catastrophic failures." in {
      val b = TestBoobytrapBomb
      
      // This tests that a specific exception IS thrown when defusing
      
      intercept[CatastrophicException] {
        ImaginaryActsOfCourage.defuse(b)
      }
    }
    
    "Defusing.perform" should "(30p) completely defuse the bomb." in {

      // Test two bombs and if they are not defused, fail test
      
      val b = new TestRedFirstBomb
      ImaginaryActsOfCourage.defuse(b)
      
      assert(b.defused == true, "The bomb wasn't actually defused." )

      val b2 = new TestBlueFirstBomb
      ImaginaryActsOfCourage.defuse(b2)
      
      assert(b2.defused == true, "The bomb2 wasn't actually defused." )    
    }
    
}


/**
 * remove the red wire first
 */

class TestRedFirstBomb extends Bomb {
    
  val parts = {
    val redWire  = new Part("Red Wire", None, false, Some(Fire))
    val blueWire = new Part("Blue Wire", Some(redWire), false)
    
    Vector(redWire, blueWire)
  }
  
}

/**
 * remove the blue wire first
 */

class TestBlueFirstBomb extends Bomb {

  val parts = {
    val blueWire = new Part("Blue Wire", None, false)
    val redWire  = new Part("Red Wire", Some(blueWire), false, Some(Heat))
    
    Vector(redWire, blueWire)
  }

}

/**
 * This bomb cannot be defused
 */
object TestBoobytrapBomb extends Bomb {

  val parts = {
    val blueWire  = new Part("Blue Wire", None, false, Some(Lost))
    val redWire   = new Part("Red Wire", Some(blueWire), true)
    
    Vector(redWire, blueWire)
  }
  
}
