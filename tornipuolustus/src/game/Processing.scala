import processing.core.{PApplet} //PConstants
import game.Logic
import o1.Pos
import scala.collection.mutable.Buffer
import scala.math.{pow,sqrt}

class Processing extends PApplet {
//  var lvlStarted = false                 //oneway flag
  
  val highlightcolor = color(200, 50)    //the color the buttons will change to when hovered over
  val towerRangeColor = color(100, 120)  //color of the tower's range when hovering the tower
  val faultyPlacementColor = color(255, 0, 0, 180) //same as the previous, but this color is used when the placement is invalid
  
  val towerBuyButtonSize = 50            //constant: the size of the purchase buttons
  val buyButtons = Buffer[Pos]()         //for easier highlighting with multiple buttons
  val colors = Buffer[Int]()             //to make it a bit easier to select the colors
  var buttonHovered:Option[Pos] = None   //option to check which button was pressed
  val towerSize = 50                     //constant: the actual size of the towers
  
  val startBX = 200  //these are the start button's constructors
  val startBY = 10
  val startW = 150
  val startH = 50
  
  
  //TOWER PURCHASE MENU DRAW INFO
  val basicTowerPos = new Pos(725, 75)   //position of towers in the purchase menu
  val basicTowerColor = color(102, 77, 32)//the color of the tower
    buyButtons += basicTowerPos  //add the basic tower's pos to the buy button buffer
    colors += basicTowerColor    //add the color to the buffer of colors for easier accessing
  
  val tackShooterPos = new Pos(775, 75)
  val tackShooterColor = color(224, 78, 187)
    buyButtons += tackShooterPos
    colors += tackShooterColor
    
  val bombTowerPos = new Pos(725, 125)
  val bombTowerColor = color(135, 0, 0)
    buyButtons += bombTowerPos
    colors += bombTowerColor
    
    
  
  override def setup() {
    background(0, 200, 0)
    frameRate(game.Logic.frameRate)
    game.Logic.currentLevel.init()  //run only once
  }
  
  override def settings() {
    size(800,600)
  }
  
  override def draw() {
    background(0, 200, 0)
    
    //Draw path
    for (i <- 0 to game.Logic.currentLevel.waypoints.size-2) {
      val wp = game.Logic.currentLevel.waypoints
      strokeWeight(50)
      stroke(209, 229, 82)
      line(wp(i).x.toFloat, wp(i).y.toFloat, wp(i+1).x.toFloat, wp(i+1).y.toFloat)
    }
    strokeWeight(0)
    noStroke()
    
    //Draw enemies
    val enemies = game.Logic.currentLevel.enemyBuffer
    stroke(0)
    fill(0) //health > 4
    enemies.filter(_.health > 4).foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, x.scale) )
    fill(255,255,0) //health = 4
    enemies.filter(_.health == 4).foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, x.scale) )
    fill(0,200,0) //health = 3
    enemies.filter(_.health == 3).foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, x.scale) )
    fill(0,0,200) //health = 2
    enemies.filter(_.health == 2).foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, x.scale) )
    fill(200, 0, 0)  //health = 1
    enemies.filter(_.health == 1).foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, x.scale) )
    
    
    
    
    //Draw (placed) towers
    fill(basicTowerColor)
    game.Logic.currentLevel.towerBuffer.filter( t => t.name == "Basic tower").foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, towerSize) )
    fill(tackShooterColor)
    game.Logic.currentLevel.towerBuffer.filter( t => t.name == "Tack shooter").foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, towerSize) )
    fill(bombTowerColor)
    game.Logic.currentLevel.towerBuffer.filter( t => t.name == "Bomb tower").foreach( x => circle(x.pos.x.toFloat, x.pos.y.toFloat, towerSize) )
    
    //Draw animations
    fill(255)
    game.Logic.animationBuffer.filter( a => a.name == "pop" ).foreach( a => circle(a.pos.x.toFloat, a.pos.y.toFloat, a.size) )
    fill(155, 77, 77, 100)
    game.Logic.animationBuffer.filter( a => a.name == "explosion" ).foreach( a => circle(a.pos.x.toFloat, a.pos.y.toFloat, a.size) )
    
    //Draw projectiles
    fill(0)
    game.Logic.currentLevel.towerBuffer.foreach( t => t.projectiles.filter( p => p.projectileType == "dart" )foreach( p => circle(p.pos.x.toFloat, p.pos.y.toFloat, p.hitbox*2) ) )
    fill(255, 194, 73)
    game.Logic.currentLevel.towerBuffer.foreach( t => t.projectiles.filter( p => p.projectileType == "bomb" )foreach( p => circle(p.pos.x.toFloat, p.pos.y.toFloat, p.hitbox*2) ) )
    
    //Draw player info: health and money
    fill(100)
    textSize(20)
    text("Health: " + game.Logic.playerHealth, 20, 30)
    text("Money: " + game.Logic.playerMoney, 20, 60)
    
    //Draw buttons
      //Tower list, but hide the list when buying a tower
    if (game.Logic.playerMode == "") {
      fill(50, 100)           //first a background for the list
      rect(700, 0, 200, 600)  //the area for towers on the right
      stroke(0)
      textSize(20)
      fill(155)
      text("Towers", 715, 30)
    
      for ( i <- 0 until buyButtons.size) { //the actual tower buttons
        val col = colors(i)
        val pos = buyButtons(i)
        fill(col)
        circle(pos.x.toFloat, pos.y.toFloat, towerSize)
      }
      
//      fill(basicTowerColor)        //Basic tower on the first place
//      circle(basicTowerPos.x.toFloat,  basicTowerPos.y.toFloat, 50)
//      fill(tackShooterColor)
//      circle(tackShooterPos.x.toFloat, tackShooterPos.y.toFloat, 50)
//      fill(bombTowerColor)
//      circle(bombTowerPos.x.toFloat, bombTowerPos.y.toFloat, 50)
      
      if (game.Logic.waveInProgress == false) {   //display the start button when a wave is not in progres
        fill(0, 100, 20)
        rect(startBX, startBY, startW, startH)    
        fill(255)
        text("START WAVE", 210, 40)
      }
    }
    
    towerHover
    
    update(mouseX, mouseY)   
    
    if (game.Logic.isLost) { //because why not
      fill(200,0,0)
      textSize(100)
      text("GIT GUD", 200, 300)
    }
    
    game.Logic.tick() //advance a tick

  }
  
  override def mousePressed() {
    if (overButtonRect(startBX, startBY, startW, startH) && game.Logic.waveInProgress == false) {
      game.Logic.startWave()
      println("starting wave " + game.Logic.waveNumber)
    }
    val prevMode = game.Logic.playerMode
    
    //TOWER BUY MODES
    val a = new towers.Basic
    if (overButtonCirclePos(basicTowerPos)) {
      game.Logic.playerMode = "tbm-basic"    //tower buy mode, with basic tower
    } else if (game.Logic.playerMode == "tbm-basic") {
      val a = new towers.Basic
      a.place(new Pos(mouseX, mouseY))
      game.Logic.playerMode = prevMode
    } 
    else if (overButtonCirclePos(tackShooterPos)) {
      game.Logic.playerMode = "tbm-tack"
    } else if (game.Logic.playerMode == "tbm-tack") {
      val a = new towers.TackShooter
      a.place(new Pos(mouseX, mouseY))
      game.Logic.playerMode = prevMode
    }
    else if (overButtonCirclePos(bombTowerPos)) {
      game.Logic.playerMode = "tbm-bomb"
    } else if (game.Logic.playerMode == "tbm-bomb") {
      val a = new towers.BombTower
      a.place(new Pos(mouseX, mouseY))
      game.Logic.playerMode = prevMode
    } 
  }
  
  override def keyPressed() {
    if (key == "q"(0) && game.Logic.playerMode.split("-")(0) == "tbm") {
      game.Logic.playerMode = ""
    }
  }
  
  def towerHover = { //when you're choosing the position you're placing the tower to, this method is used to show the range and the tower while hovering
    var temp: Option[data.Tower] = None
    var index: Int = 0
    game.Logic.playerMode.split("-").last match {
      case "basic" => {
        val a = new towers.Basic
        temp = Some(a)
        index = 0
//        if (a.placeable(new Pos(mouseX, mouseY))) fill(towerRangeColor) else fill(faultyPlacementColor)
//        circle(mouseX, mouseY, a.range.toFloat * 2)   //when hovering, this displays a transparent circle with the radius of the range of the tower
//        fill(basicTowerColor)
//        circle(mouseX, mouseY, towerSize)             //when hovering, this displays the tower
//        textSize(20)
//        fill(0)
//        text("Placing " + a.name + ". Press Q to cancel.", 250, 20)  //hovering info
      }
      case "tack" => {
        val a = new towers.TackShooter
        temp = Some(a)
        index = 1
//        if (a.placeable(new Pos(mouseX, mouseY))) fill(towerRangeColor) else fill(faultyPlacementColor)
//        circle(mouseX, mouseY, a.range.toFloat * 2)
//        fill(tackShooterColor)
//        circle(mouseX, mouseY, towerSize) 
//        textSize(20)
//        fill(0)
//        text("Placing " + a.name + ". Press Q to cancel.", 250, 20)
      }
      case "bomb" => {
        val a = new towers.BombTower
        temp = Some(a)
        index = 2
//        if (a.placeable(new Pos(mouseX, mouseY))) fill(towerRangeColor) else fill(faultyPlacementColor)
//        circle(mouseX, mouseY, a.range.toFloat * 2)
//        fill(bombTowerColor)
//        circle(mouseX, mouseY, towerSize) 
//        textSize(20)
//        fill(0)
//        text("Placing " + a.name + ". Press Q to cancel.", 250, 20)
      }
      case _ => 
    }
    
    if (!temp.isEmpty) {
      if (temp.get.placeable(new Pos(mouseX, mouseY))) fill(towerRangeColor) else fill(faultyPlacementColor)  //get the range indicator's color: normal color if position is valid
          circle(mouseX, mouseY, temp.get.range.toFloat * 2)
          fill(colors(index))
          circle(mouseX, mouseY, towerSize) 
          textSize(20)
          fill(0)
          text("Placing " + temp.get.name + ". Press Q to cancel.", 250, 20)
    }
  }
  
  def update(x: Int, y: Int) {  //this method highlights the button the mouse is currently hovering on.
    buttonHovered = buyButtons.map( b => (b, overButtonCircle(b.x.toInt, b.y.toInt, towerBuyButtonSize)) ).filter( b => b._2 == true).map(b => b._1).headOption  //the button currently hovered over
    if (!buttonHovered.isEmpty && game.Logic.playerMode.split("-")(0) != "tbm") {                           //if a button is being hovered...
      val b = buttonHovered.get                             //...choose the button which is being hovered...
//      if (b.distance(basicTowerPos) == 0) {
      if (buyButtons.exists( button => b.distance(button) == 0 )) {
        fill(highlightcolor) //transparent white
        noStroke()
        circle(b.x.toFloat, b.y.toFloat, towerBuyButtonSize)  //...and create a circle over the hovered button.
        
        var textBoxHeight = 0
        val a: data.Tower = 
          if (b.distance(basicTowerPos) == 0 ) { // choose which tower to create so we can print out the info
          textBoxHeight = 5*24
          new towers.Basic
        } else if (b.distance(tackShooterPos) == 0 ) {
          textBoxHeight = 4*24
          new towers.TackShooter
        } else if (b.distance(bombTowerPos) == 0 ) {
          textBoxHeight = 6*24
          new towers.BombTower
        } else new towers.Basic 
        
        fill(50, 50, 50, 100) //background for info text
        rect(b.x.toFloat - 150, b.y.toFloat - 50, 120, textBoxHeight)  //a line of text with size 15 has a height of around 24
        fill(250)
        textSize(15)
        text(a.name + " - " + a.desc + "\nPrice: " + a.price, b.x.toFloat - 150, b.y.toFloat - 50, 120, 150)
      }
    }
  }
  
  
  def overButtonCircle(x: Int, y: Int, dia: Int): Boolean = {   //use this to check if mouse is over a certain round button
    val relX = x - mouseX  //relative x coordinate to the button
    val relY = y - mouseY  //relative y coordinate to the button
    val distance = sqrt(pow(relX, 2) + pow(relY, 2))
    if (distance < dia/2.0) {
      return true
    } else return false
  }
  
  def overButtonRect(x: Int, y: Int, width: Int, height: Int): Boolean = {   //use this to check if mouse is over a certain rectangular button
    if (mouseX >= x && mouseX <= x+width && 
        mouseY >= y && mouseY <= y+height  ) {
      return true
    } else return false
  }
  
  def overButtonCirclePos(pos: Pos): Boolean = {   //use this to check if mouse is over a certain round button, with pos coordinates (used to check which tower the player is buying)
    val relX = pos.x - mouseX  //relative x coordinate to the button
    val relY = pos.y - mouseY  //relative y coordinate to the button
    val distance = sqrt(pow(relX, 2) + pow(relY, 2))
    if (distance < towerBuyButtonSize/2.0) {
      return true
    } else return false
  }
}
