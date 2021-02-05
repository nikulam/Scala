package data

import o1.Pos

trait Animations {
  val maxLifetime: Int  //how long the animation is displayed
  var lifetime = 0      //counter
  val name: String      //ie. "pop" for bloon popping, "explosion" for explosions (primarily from bombs)...
  val pos: Pos
  val size: Float
  game.Logic.animationBuffer += this
  
  def age() { //make the animation older
    this.lifetime += 1
    if (this.lifetime >= this.maxLifetime) game.Logic.animationBuffer -= this
  }
}

