package towers

import data._ 
import o1.world._

class TargetDart(val origin: Tower, override val target: Pos) extends data.Projectile {
  val projectileType = "dart"
  pos = origin.pos
  val speed: Int = 30
  val maxLifetime = (origin.range / this.speed).toInt + 1  //+ 1 because felt like the lifetime calculation was lacking.
                                                           //also maxLifetime is calculated here because for some reason it caused errors in projectile trait
}

class DirectionDart(val origin: Tower, override val direction: Direction) extends data.Projectile {
  val projectileType = "dart"
  pos = origin.pos
  val speed: Int = 30
  val maxLifetime = (origin.range / this.speed).toInt + 1 
}

class Bomb(val origin: Tower, override val target: Pos) extends data.Projectile {
  val projectileType = "bomb"
  val speed: Int = 14
  val maxLifetime = (origin.range / this.speed).toInt + 1
  override val hitbox = 20
  val explosionRange = 500
  
  override def hit() {
    if(!origin.level.enemyBuffer.isEmpty) {
      if (origin.level.enemyBuffer.exists(e => this.pos.distance(e.pos) <= (hitbox + e.scale/2) )) {
        val hitEnemies = origin.level.enemyBuffer.filter( e => this.pos.distance(e.pos) <= explosionRange )
        hitEnemies.foreach( e => e.hit(damage) )
        origin.projectiles -= this
        new animations.Explosion(this.pos, explosionRange*2)
      }
    }
  }
}