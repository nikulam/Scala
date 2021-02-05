
package polynomials

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for polynomials.
 *
 */

class UnitTests {

  @Test def testTask1() {
    val x = new Variable("x")
    for(x0 <- -10 to 10) {
      x.set(x0)
      assertEquals("Set value " ++ x0.toString ++ " to x " 
                   ++ "but got " ++ x.value.toString ++ " back",
                   x0,
		   x.value)
    }
    for(x0 <- -10 to 10) {
      val c = new Constant(x0)
      assertEquals("Created constant with value " ++ x0.toString
                   ++ "but constant reports value " ++ c.value.toString,
                   x0,
		   c.value)
    }
  }

  @Test def testTasks12() {
    val x = new Variable("x")
    val poly = new Sum(new Product(x,x), new Constant(1)) // x*x+1
    for(x0 <- -10 to 10) {
      x.set(x0)
      assertEquals("Evaluated p(x) = " ++ poly.asString 
                   ++ " at x = " ++ x0.toString 
                   ++ " and got " ++ poly.value.toString
                   ++ "; however, should have gotten " 
                   ++ (x0*x0+1).toString,
                   x0*x0+1,
		   poly.value)
    }
  }

  @Test def testTasks123Univariate() {
    val x = new Variable("x")
    val poly = x*x + new Constant(1)
    for(x0 <- -10 to 10) {
      x.set(x0)
      assertEquals("Evaluated p(x) = " ++ poly.asString 
                   ++ " at x = " ++ x0.toString 
                   ++ " and got " ++ poly.value.toString
                   ++ "; however, should have gotten " 
                   ++ (x0*x0+1).toString,
                   x0*x0+1,
		   poly.value)
    }
  }

  @Test def testTasks123Univariate2() {
    val x = new Variable("x")
    val poly = x*(x + new Constant(1))+x*(x+x+x)*x+x
    for(x0 <- -10 to 10) {
      x.set(x0)
      assertEquals("Evaluated p(x) = " ++ poly.asString 
                   ++ " at x = " ++ x0.toString 
                   ++ " and got " ++ poly.value.toString
                   ++ "; however, should have gotten " 
                   ++ (x0*(x0+1)+x0*(x0+x0+x0)*x0+x0).toString,		   
                   x0*(x0+1)+x0*(x0+x0+x0)*x0+x0,
                   poly.value)
    }
  }  

  @Test def testTasks123Multivariate() {
    val x = new Variable("x")
    val y = new Variable("y")
    val poly = x*(y + new Constant(1))+y+x*new Constant(2)
    for(x0 <- -10 to 10) {
      x.set(x0)
      for(y0 <- -10 to 10) {
        y.set(y0)
        assertEquals("Evaluated p(x,y) = " ++ poly.asString 
                     ++ " at x = " ++ x0.toString ++ ", y = " ++ y0.toString
                     ++ " and got " ++ poly.value.toString
                     ++ "; however, should have gotten " 
                     ++ (x0*(y0+1)+y0+2*x0).toString,
                     x0*(y0+1)+y0+2*x0,
		     poly.value)
      }
    }
  }  
}


