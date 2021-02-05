package expressions

/**
 * Playground for testing with expressions. Can be run as Scala application.
 */
object ExpressionTest extends App {
  import Expressions._

  // Tehtävä 16.1

  // Toteuta alla olevat lausekkeet a, b, c ja d.

  // Tehtävässä käytetyt muuttujat:
  def x = Variable("x")
  def y = Variable("y")
  def z = Variable("z")

  // a) 2x

  def func1 = Mul(Const(2), this.x)

  // b) (2x)^3   eli  2x * 2x * 2x
  // Käytä edellistä tulosta apuna.

  def func2 = Mul(Mul(this.func1, this.func1), this.func1)

  // c) 3 x y + x (x + 7 z)

  def func3 = Add(Mul(Mul(Const(3), this.x), this.y), Mul(this.x, Add(this.x, Mul(Const(7), this.z))))

  // d) x^2 + 2 x y + y^2

  def func4 = Add(Mul(this.x, this.x), Add(Mul(Mul(Const(2), this.x), this.y), Mul(this.y, this.y)))

}

/**
 * Implementations for expression handling.
 */
object Expressions {

  // Tehtävä 16.2

  def prettyprint(e: Exp): String = {
    e match {
      case x: Const            => x.c.toString()
      case v: Variable         => v.name
      case Add(e: Exp, f: Exp) => "( " + prettyprint(e) + " + " + prettyprint(f) + " )"
      case Mul(e: Exp, f: Exp) if e == f => prettyprint(e) + "^2"
      case Mul(e, Mul(f, g)) if e == f && f == g => prettyprint(e) + "^3"
      case Mul(Mul(e, f), g) if e == f && f == g => prettyprint(e) + "^3"
      case Mul(e: Exp, f: Exp) => "( " + prettyprint(e) + " * " + prettyprint(f) + " )"
      
      
        
    }
  }

  // Tehtävä 16.3

  def bind(e: Exp, v: Variable, a: Double): Exp = {
    e match {
      case c: Const                  => c
      case matchVar: Variable        => if(matchVar == v) Const(a) else e
      case add @ Add(e: Exp, f: Exp) => Add(bind(e,v,a), bind(f,v,a))
      case mul @ Mul(e: Exp, f: Exp) => Mul(bind(e,v,a), bind(f,v,a))
    }
  }

  // Tehtävä 16.4

  def derivate(e: Exp, d: Variable): Exp = {
    e match {
      case Const(_)            => Const(0)
      case v: Variable         => if(v == d) Const(1) else Const(0)
      case Add(e: Exp, f: Exp) => Add(derivate(e, d), derivate(f, d))
      case Mul(e: Exp, f: Exp) => Add(Mul(e, derivate(f, d)), Mul(f, derivate(e, d)))
    }
  }

}