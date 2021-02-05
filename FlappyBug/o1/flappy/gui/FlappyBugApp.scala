package o1.flappy.gui
import o1._
import o1.flappy._
// This class is introduced in Chapter 2.7.
object FlappyBugApp extends App {
  val sky        = rectangle(ViewWidth, ViewHeight,  LightBlue)
  val ground     = rectangle(ViewWidth, GroundDepth, SandyBrown)
  val trunk      = rectangle(30, 250, SaddleBrown)
  val foliage    = circle(200, ForestGreen) 
  val tree       = trunk.onto(foliage, TopCenter, Center)
  val rootedTree = tree.onto(ground, BottomCenter, new Pos(ViewWidth / 2, 30))
  val scenery    = sky.place(rootedTree, BottomLeft, BottomLeft)
  
  val bugPic = Pic("ladybug.png")
  def rockPic(obstacle: Obstacle) = Pic("obstacle.png").scaleTo(obstacle.radius * 2)
  
   val peli = new Game
  
   var gui = new View(peli, "FlappyBug") {
   var background = scenery
  
   def makePic = {
     var kuva: Pic = background.place(bugPic, peli.bug.pos) 
     peli.obstacles.foldLeft(kuva)((result, n) => result.place(rockPic(n), n.pos))
   }
   
   override def onKeyDown(painettu: Key) = {
     if(painettu == Key.Space) peli.activateBug()
}
     
   override def onTick() = {
    peli.timePasses()
    this.background = this.background.shiftLeft(2)
   }
   override def isDone() = peli.isLost
   
  }
    gui.start()
    
    
}