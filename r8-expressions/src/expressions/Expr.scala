
/**
 * @author: Tommi Junttila
 * Only for use in the course CS-A1120. Other use and distribution not allowed.
 */

package expressions

import scala.collection.immutable.Map

/**
 * The abstract base class for expressions.
 */
abstract class Expr {
  /* Overriding these operators enable us to construct expressions with infix notation */
  def +(other: Expr) = Add(this, other)
  def -(other: Expr) = Subtract(this, other)
  def *(other: Expr) = Multiply(this, other)
  def unary_-() = Negate(this)

  /**
   * Returns the set of variable names appearing in this expression
   */
  final def vars: Set[String] = this match {
    case Var(n) => Set(n)
    case Num(v) => Set()
    case Multiply(l, r) => l.vars ++ r.vars
    case Add(l, r) => l.vars ++ r.vars
    case Subtract(l, r) => l.vars ++ r.vars
    case Negate(t) => t.vars
  }

  /** The exception returned by "evaluate" when a variable in the expression is not assigned a value. */
  class VariableNotAssignedException(message: String) extends java.lang.RuntimeException(message)

  /**
   * Evaluate the expression in the point p, where p is a map
   * associating each variable name in the expression into a Double value.
   */
  final def evaluate(p: Map[String, Double]): Double = this match {
    case Var(n) => p.get(n) match {
      case Some(v) => v
      case None => throw new VariableNotAssignedException(n)
    }
    case Num(v) => v
    case Multiply(l, r) => l.evaluate(p) * r.evaluate(p)
    case Add(l, r) => l.evaluate(p) + r.evaluate(p)
    case Subtract(l, r) => l.evaluate(p) - r.evaluate(p)
    case Negate(t) => -t.evaluate(p)
  }

  /**
   * Similar to evaluate except that not all the variables names must have
   * an associated value in p. Therefore, instead of a Double value, a new expression
   * is returned such that (i) all the variable names who are associated with a value are
   * replaced with that value, while (ii) all the other variable names stay intact.
   * For instance, if p maps x to 3.0 but does not mention y, then partialEvaluate(p)
   * on 2.0*x+y*x should return an expression equivalent to 3.0*y+6.0.
   */
  final def partialEvaluate(p: Map[String, Double]): Expr = this match {
    case Var(n) => p.get(n) match {
      case Some(v) => Num(v)
      case None => Var(n)
    }
    case Num(v) => v
    case Multiply(l, r) => l.evaluate(p) * r.evaluate(p)
    case Add(l, r) => l.evaluate(p) + r.evaluate(p)
    case Subtract(l, r) => l.evaluate(p) - r.evaluate(p)
    case Negate(t) => -t.evaluate(p)
  }

  /**
   * Produce the partial derivative of the expression with respect to the variable v.
   * Such a partial derivative is the derivative of the expression obtained when considering
   * the variables other than v as constants.
   * For instance, the partial derivative of 3*x*x*y+z*x+z*z w.r.t x is 6*x*y+z
   * and that of y*(x*z + 3*y) w.r.t. y is x*z+6*y. 
   *
   * If you need to recall the differentiation rules,
   * please see, for instance, the "sum rule" and "product rule" in
   * http://en.wikipedia.org/wiki/Differentiation_rules
   */
  final def partialDerivative(v: String): Expr = this match {
      case Var(n) => {
        if(n == v) 1 
        else 0    
      }
      case Num(t) => 0
      case Multiply(l, r) => l * r.partialDerivative(v) + r * l.partialDerivative(v)
      case Add(l, r) => l.partialDerivative(v) + r.partialDerivative(v)
      case Subtract(l, r) => l.partialDerivative(v) - r.partialDerivative(v)
      case Negate(t) =>   - t.partialDerivative(v)
      
  }
  
  /**
   * Simplifies the expression with some simple rules like "1.0*r equals r".
   */
  final def simplify: Expr = {
    // First, (recursively) simplify sub-expressions
    val subresult = this match {
      case Multiply(l, r) => Multiply(l.simplify, r.simplify)
      case Add(l, r) => Add(l.simplify, r.simplify)
      case Subtract(l, r) => Subtract(l.simplify, r.simplify)
      case Negate(t) => Negate(t.simplify)
      case _ => this // Handles Var and Num
    }
    // Then simplify this sub-expression by applying some simple rules
    subresult match {
      case Multiply(Num(1.0), r) => r
      case Multiply(l, Num(1.0)) => l
      case Multiply(Num(0.0), _) => Num(0.0)
      case Multiply(_, Num(0.0)) => Num(0.0)
      case Multiply(Num(v1), Num(v2)) => Num(v1 * v2)
      case Add(Num(0.0), r) => r
      case Add(l, Num(0.0)) => l
      case Add(Num(v1), Num(v2)) => Num(v1 + v2)
      case Subtract(Num(0.0), r) => Negate(r)
      case Subtract(l, Num(0.0)) => l
      case Subtract(Num(v1), Num(v2)) => Num(v1 - v2)
      case Negate(Num(v)) => Num(-v)
      case _ => subresult // Could not simplify
    }
  }

  /**
   * Is this expression a product? ("term" is also sometimes used as a synonum for "product")
   * An expression is a product if
   * it is a product of variables, constant numbers, and their negations.
   * For instance, x*-4*y*x is a product but x*(2+y) is not.
   */
  final def isProduct: Boolean = this match {
    case Multiply(l, r) => l.isProduct && r.isProduct
    case Negate(Num(_)) => true
    case Negate(Var(_)) => true
    case Var(_) => true
    case Num(_) => true
    case _ => false
  }

  /**
   * Check if the expression is a sum of products.
   * If it is, we say that it is in "sum of products" (SOP) normal form.
   * For instance, 2*x + 7 + x*-y is in SOP form but 2*(x + y) + 7 is not.
   */
  final def isInSOP: Boolean = this match {
    case Add(l, r) => l.isInSOP && r.isInSOP
    case _ => this.isProduct
  }

  /**
   * Expand this expression into "sum of products" (SOP) normal form (see the definition of
   * product and SOP above in the documentation of the "isProduct" and "isInSOP" methods).
   * That is, the expressions x*(3*x - y) should be expanded to x*3*x + -1*x*y, or to something
   * equivalent, that is a sum (+) of products (x*3*x and 1-*x*y in this case).
   *
   * Hint: one way to solve this is to make two inner functions
   * - one helper function that negates a product (e.g., translates "x*-y*y" to "-x*-y*y"), and
   * - one that (recursively) forms a list of the products in the SOP form of an expression.
   *   For instance, if the products in the SOP form of e are ("2.0*x*y", "z*-3.0*-y"),
   *     then what are the products in the SOP form of the expression -e?
   *   Perform similar analysis for other operators.
   *
   * You do not need to minimize the result expression,
   * it only has to be in SOP form and equal to the original expression.
   */
  final def expand: Expr = ???
}

/** Variable expression, like "x" or "y". */
case class Var(name: String) extends Expr {
  override def toString = name
}
/** Constant expression like "2.1" or "-1.0" */
case class Num(value: Double) extends Expr {
  override def toString = value.toString
}
/** Expression formed by multiplying two expressions, like "x * (y+3)" */
case class Multiply(left: Expr, right: Expr) extends Expr {
  override def toString = "(" + left + "*" + right + ")"
}
/** Expression formed by adding two expressions, like "x + (y*3)" */
case class Add(left: Expr, right: Expr) extends Expr {
  override def toString = "(" + left + " + " + right + ")"
}
/** Expression formed by subtracting an expression from another, "x - (y*3)" */
case class Subtract(left: Expr, right: Expr) extends Expr {
  override def toString = "(" + left + " - " + right + ")"
}
/** Negation of an expression, like "-((3*y)+z)" */
case class Negate(p: Expr) extends Expr {
  override def toString = "-" + p
}


