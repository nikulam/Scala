package o1.flappy
import o1._
class Bug(nytPos: Pos) {
 
  private var yVelocity = 0.0
  private var atmPos = nytPos
  val radius = BugRadius
  
  def pos = this.atmPos
  
  def flap(muutos: Double){
    this.yVelocity = - muutos
  }
  def fall(){
    if(this.pos.y < 350){   
     this.yVelocity = this.yVelocity + FallingSpeed
    }
    this.move(this.yVelocity)
 }
  def move(apu: Double) { 
    this.atmPos = this.atmPos.addY(apu).clampY(0 , 350)
    }
  
  def isInBounds = {
    this.pos.y > 0 && this.pos.y < 350
  }
  override def toString = "center at" + " " + (pos.x, pos.y) + "," + " " + "radius " + radius
} 
