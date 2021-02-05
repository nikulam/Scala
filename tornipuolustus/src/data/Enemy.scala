package data

import o1.world._

//Enemies color tells their health and speed. Higher level enemies have more health and move faster.
//Colors from worst to highest (health increases by 1 per level, base is 1): red, blue, green, yellow

trait Enemy {
  
  var lifetime = 0
  var health: Int  = 1                             //health is always an integer
  def scale: Float = 30 + (health * 10)            //the scale of the enemy (used to tell processing how large the circle has to be)
  
  def speed: Double   = health + (health * 0.5)    //speed depends on the health
  var progress:Double = 0                          //progress stat used for tower targeting
  var endReached: Boolean = false
  
  
  var pos: Pos = level.start
  val level: Level
  
  val waypoint: Iterator[Pos] = level.waypoints.toIterator
  var target: Pos = waypoint.next
  
  def hit(dmg: Int) = {  //this enemy being hit
    val dmgTaken = math.min(dmg, this.health)
    this.health -= dmg
    if (this.health <= 0) level.enemyBuffer -= this
    game.Logic.playerMoney += dmgTaken
    
    new animations.Pop(this.pos) //the fancy hit animation!
  }
  
  def move() = {
    //move towards target by speed
    lifetime += 1
    progress = this.progress + this.speed
    val dir = this.pos.directionOf(this.target)
    val newPos = new Pos((dir.dx*speed) +this.pos.x,    (dir.dy*speed) +this.pos.y)   //TODO: diagonal movement: currently the enemies move faster diagonally(?)
    this.pos = newPos
    if(pos.distance(target) <= 5 && waypoint.hasNext) target = waypoint.next
    if(pos.distance(level.waypoints(level.waypoints.size-1)) <= 5) endReached = true
  }
  
  
}