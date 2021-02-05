package game

import scala.collection.mutable.Buffer

object Logic{
  var currentLevel = leveldata.Level2      //change this to play other levels
  var frameRate = 60F     //how about a feature that allows you to speed up or slow down the game (without breaking the tower's fire rate)?
  
  var playerHealth = 50  //game ends if playerHealth drops to 0
  var playerMoney = 300 //player's resources to buy towers
  var playerMode = ""   //the mode the player is currently in, ie. tower buying mode(tbm) to drag the tower with the cursor or "idle" when there is no wave going
  var isLost = false    //if health drops to 0, you lose
  
  var waveInProgress = false
  var waveNumber = 0
  
  var animationBuffer = Buffer[data.Animations]()  //holds the animation positions to be drawn
  
  def startWave() = {  //the method to call when you want to start the wave
    waveNumber += 1
    playerMode = ""
    waveInProgress = true
    currentLevel.nextWave
  }
  
  
  
  def tick() = { //the game updates 60 times a second
    if (!isLost) {
    currentLevel.spawn()
    
    val eBuf = currentLevel.enemyBuffer //easier way of accessing the level's buffer of enemies
    eBuf.foreach(_.move())              //movement of enemies each tick
    
    val tBuf = currentLevel.towerBuffer
    tBuf.foreach(_.readyCheck)
    tBuf.foreach(t => t.projectiles.foreach( p => if (p != null) p.move()))
    
    
    val passedEnemies = eBuf.filter(_.endReached == true) //the enemies that has gone through the path
    var healthLost = passedEnemies.map(_.health).sum
    playerHealth -= healthLost                            //remove player health equal to the amount of passed enemies
    eBuf --= passedEnemies                                //remove the passed enemies from the buffer of enemies
    if (playerHealth <= 0) isLost = true    //lose
    
    if (currentLevel.enemyBuffer.isEmpty && currentLevel.spawnQueue.isEmpty && waveInProgress == true) {   //wave end check
      playerMode = ""
      playerMoney += 50 + waveNumber
      waveInProgress = false
    }
    
    animationBuffer.foreach( a => if(a != null) a.age() )
    }
  }
}