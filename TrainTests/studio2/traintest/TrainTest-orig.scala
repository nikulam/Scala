package studio2.traintest

import scala.collection.mutable.Map
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.Assertions._
import studio2.train._


/**
 * This helper class is quite useful when testing the Train- and TrainCar-classes.
 * 
 * It has a number for easy identification and allows setting place and free counts
 * without extra logic, making some functions in the TrainCar trait easier to test directly.
 */

class TestCar(val carNumber: Int, places: Int, free: Int) extends TrainCar {
  def numberOfPlaces     = places
  def numberOfFreePlaces = free
  def reservePlaces(numberOfPeople: Int) = ???    
}

/**
 * Tests for the Train project from Chapter 6.3
 * 
 * Your job is to fill in the test methods in this class.
 * 
 * The project is the Train-project from chapter 6.3. We have given you a copy with
 * the original errors, so that you can test against the errors while writing your tests. 
 */

@RunWith(classOf[JUnitRunner])
class TrainTest extends FlatSpec with Matchers {
    
  
  // Fill in the tests below. The first one is given as an example. 
  
  
  "Train addCar" should "allow adding the maximum number of cars" in {
    val train = new Train("HKI-OULU")

    for (i <- 1 to Train.MaximumLength) {
      val success = train.addCar(new TestCar(i,0,0))
      assert(success, "Train failed to add a car")
    }
  }

  // Replace the texts xxx and yyy with something infromational and place the proper testing code
  // with asserts inside. Remember to run the tests in Eclipse before submitting.
  
  "Train.car" should "Return None if called for a parameter bigger than car.length" in {
    val train = new Train("M")
    
    train.addCar(new TestCar(1,0,0))
   
    val success = train.car(5)
    assert(success == None, "Did not return None")
    
  }
  "TrainCar.fullness" should "Return a double if the result of division isn't sharp. " in {
    val testcar = new TestCar(1, 501, 333)
    val success = 100.0 * (501 - 333) / 501
    assert(success == testcar.fullness, "No double")
  }

   
  "SleepingCar.reservePlaces" should "Reserve places." in {
    val sleepingCar = new SleepingCar(1)
    sleepingCar.reservePlaces(1)
    assert(sleepingCar.emptyCabinCount == 0, "No places were reserved.")
  }
  
  "SleepingCar.numberOfFreePlaces" should "Return bedsPerCabin * emptyCabinCount." in {
    val sleepingCar = new SleepingCar(10)
    val success = 3 * sleepingCar.emptyCabinCount
    assert (success == sleepingCar.numberOfFreePlaces, "No go")
  }

  "SleepingCar.reserveCabin" should "Return true if cabin was reserved." in {
    val sleepingCar = new SleepingCar(1)
    val success = sleepingCar.reserveCabin(1)
    
    assert(success == true, "false did not equal true")
  }

  "SittingCar.reservePlaces" should "Actually reserve places. " in {
    val car = new SittingCar(10, 6)
    val before = car.numberOfFreePlaces
    car.reservePlaces(6)
    val after = car.numberOfFreePlaces
    assert(before != after, "It did not reserve any seats.")
  }

  "SittingCar.xxx" should "yyy" in {
    val car = new SittingCar(10, 6)
    var success = car.reservePlaces(0)
    assert(success == true, "Didn't return true")
    
  }


  
}