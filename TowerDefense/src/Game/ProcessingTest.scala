import processing.core._
import scala.math._

class Processing extends PApplet {
   
 val p = new Game
 var isPlacing = false
 var toBePlaced: Option[Tower] = None
 val basicColor = color(0,0,255)
 val sniperColor = color(240,230,140)
 val gatlingColor = color(139,0,139)
 
  //Draws map.
  def mapDisp = {
    for(i <- p.map.route1) {
      noStroke
      fill(255, 140, 0)
      rect(i.x, i.y, 50, 50)
    }
  }
  //Draws and moves every enemy in game.
  def enemyDisp = {
    p.enemies.map(n => if(n.isDestroyed) p.player.money += n.value)
    p.enemies = p.enemies.filter(n => !n.isDestroyed && !n.reachedGoal)
    for(e <- p.enemies) {
      stroke(0)
      fill(abs(e.hp - 255),0,0)
      circle(e.location.x, e.location.y, e.size)
      e.move(p.map)
      if(e.reachedGoal) p.player.hp -= 1
    }
    if(!p.enemies.isEmpty) p.levelDone = false
    else p.levelDone = true
  }
  
  //Draws every tower in game and adds ammos to the game if tower has a target.
  def towerDisp = {
    for(t <- p.towers) {
      if(t.name == "basic") fill(basicColor)
      else if(t.name == "sniper") fill(sniperColor)
      else if(t.name == "gatling") fill(gatlingColor)
      rect(t.location.x, t.location.y, 50, 50)
      t.setEnemies(p.enemies)
      t.findTarget
      if(frameCount % (60.0 / t.fireRate).toInt == 0 && t.hasTarget) {
        if(t.name == "basic") p.ammos = p.ammos :+ new Ammo(t.target.get, new PVector(t.location.x + 25, t.location.y + 25), 25, 7)
        else if(t.name == "sniper") p.ammos = p.ammos :+ new Ammo(t.target.get, new PVector(t.location.x + 25, t.location.y + 25), 50, 10)
        else if(t.name == "gatling") p.ammos = p.ammos :+ new Ammo(t.target.get, new PVector(t.location.x + 25, t.location.y + 25), 20, 8)
      }
    }
  }
  
  //Draws and moves every ammo in game.
  def ammoDisp: Any = {
    p.ammos = p.ammos.filter(!_.hasHit)
    for(a <- p.ammos) {
      fill(0,255,0)
      circle(a.location.x, a.location.y, 25)
      a.fly
      if(a.hasHit) {
        a.target.getHit(a)
      }
    }
  }
  //Draws menu bar.
  def menuBar = {
    fill(0)
    rect(800,0,300,800)
    textSize(50)
    fill(255)
    text("Towers:", 800, 50, 200,200)
    fill(basicColor)
    rect(850, 150, 50, 50)
    fill(sniperColor)
    rect(925, 150, 50, 50)
    fill(gatlingColor)
    rect(1000, 150, 50, 50)
  }
  
  //Draws player info.
  def playerInfo {
    textSize(50)
    fill(255)
    text("HP: " + p.player.hp, 800, 500, 200, 200)
    fill(0,255,0)
    text("MONEY: " + p.player.money, 800, 600, 200, 200)
    if(p.levelDone) {
      stroke(255)
      fill(0)
      rect(800,400,290,70)
      fill(255)
      text("NEXT WAVE", 800,400,285,100)
    }
  }
  
  //If mouse is pressed over tower pics in menu bar then choose that tower to ble placed.
  override def mousePressed = {
    if(mouseX > 850 && mouseX < 900 && mouseY > 150 && mouseY < 200) {
      isPlacing = true
      toBePlaced = Some(new BasicTower)
    }
    if(mouseX > 925 && mouseX < 975 && mouseY > 150 && mouseY < 200) {
      isPlacing = true
      toBePlaced = Some(new SniperTower)
    }
    if(mouseX > 1000 && mouseX < 1050 && mouseY > 150 && mouseY < 200) {
      isPlacing = true
      toBePlaced = Some(new GatlingTower)
    }
  }
  //If placement is legal, place tower to that place.
  override def mouseReleased = {
    if(isPlacing && p.map.everyPix.forall(_.dist(new PVector(mouseX, mouseY)) > 50) && p.towers.forall(_.location.dist(new PVector(mouseX - 25, mouseY - 25)) > 50) && mouseX < 775) {
      toBePlaced match {
        case Some(torni) => {
          if(p.player.money >= torni.price) {
            torni.setLocation(new PVector(mouseX - 25, mouseY - 25))
            p.towers = p.towers :+ torni
            p.player.money -= torni.price
          }
        }
      }
    }
    isPlacing = false
  }
  //Shows the specs of the tower.
  def towerHover = {
    var torni: Option[Tower] = None
    if(mouseX > 850 && mouseX < 900 && mouseY > 150 && mouseY < 200) {
      torni = Some(new BasicTower)
    }
    else if(mouseX > 925 && mouseX < 975 && mouseY > 150 && mouseY < 200) {
      torni = Some(new SniperTower)
    }
    else if(mouseX > 1000 && mouseX < 1050 && mouseY > 150 && mouseY < 200) {
      torni = Some(new GatlingTower)
    }
    torni match {
      case Some(juttu: Tower) => {
        textSize(20)
        fill(255)
        text(juttu.description + "\nPrice: " + juttu.price + "\nRange: " + juttu.range + "\nFire rate: " + juttu.fireRate , 850,250)
      }
      case None => {}
    }
  }
  //Shows the range of the tower as a circle when placing.
  def placeTower = {
    if(isPlacing) {  
      toBePlaced match {
        case Some(torni) => {
          noStroke
          if(torni.name == "basic") fill(basicColor)
          else if(torni.name == "sniper") fill(sniperColor)
          else if(torni.name == "gatling") fill(gatlingColor)
          rect(mouseX - 25, mouseY - 25, 50, 50)
          if(p.map.everyPix.forall(_.dist(new PVector(mouseX, mouseY)) > 50) && p.towers.forall(_.location.dist(new PVector(mouseX - 25, mouseY - 25)) > 50) && mouseX < 775 && p.player.money >= torni.price) {
            fill(0,0,0,50)
            circle(mouseX, mouseY, torni.range * 2)    
          }
          else if(mouseX < 775) {
            fill(255,0,0,50)
            circle(mouseX, mouseY, torni.range * 2)
          }
        }
      }
    }
  }
  //Checks if the game has been won or lost.
  def endGame = {
    if(p.player.hp <= 0) {
      fill(0)
      rect(0,0,1100, 800)
      fill(255)
      textSize(100)
      text("Game Over", 300, 400)
    }
    else if(p.ended) {
      fill(0)
      rect(0,0,1100, 800)
      fill(255)
      textSize(100)
      text("Game Won", 300, 400)
    }
  }
  //Checks if NEXT WAVE-button is clicked.
  override def mouseClicked = {
    if(p.levelDone && mouseX > 800 && mouseX < 1090 && mouseY > 400 && mouseY < 470) {
      p.nextWave
      p.waveNumber += 1
    }
  }
  override def setup() {
    background(0,75, 0)
  }
  
  override def settings() {
    size(1100, 800)
  }
  override def draw() {
    background(0, 75, 0)
    mapDisp
    enemyDisp
    towerDisp
    ammoDisp
    menuBar
    playerInfo
    placeTower
    if(frameCount % 5 == 0) p.spawnEnemies
    fill(255)
    towerHover
    endGame
  }
}
  
