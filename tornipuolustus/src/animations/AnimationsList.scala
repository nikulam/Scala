package animations

import o1.Pos

class Pop(val pos: Pos) extends data.Animations {
  val maxLifetime = 3
  val name = "pop"
  val size = 30F
}

class Explosion(val pos: Pos, val size: Float) extends data.Animations {
  val maxLifetime = 5
  val name = "explosion"
}