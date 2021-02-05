package data

import o1.Pos
import scala.collection.mutable.{Buffer, Queue}
import io.Source._

trait Level {
  
  val waypoints: Array[Pos]  //waypoints guide enemies through the path. There should be a waypoint on every corner.
  
  //invalid tower position calculation (path width = 50 pixels)
  //these are each of the lines' end points paired.
  var pathLines: Array[(Pos, Pos)] = Array()
  
  def pathLineEqCalc: Unit = {  //give the path lines in form ax + by + c = 0. we're storing a, b, c here and the lines' endpoints.
    for (i <- 0 until pathLines.size) {           //a = (y1-y2), b = (x2-x1), c = (x1*y2 - x2*y1)
      val a = pathLines(i)._1.y - pathLines(i)._2.y
      val b = pathLines(i)._2.x - pathLines(i)._1.x
      val c = pathLines(i)._1.x * pathLines(i)._2.y - pathLines(i)._2.x * pathLines(i)._1.y
      pathLineEqs = pathLineEqs :+ (a.toInt, b.toInt, c.toInt, pathLines(i)._1, pathLines(i)._2)
    }
  }
  
  var pathLineEqs: Array[(Int, Int, Int, Pos, Pos)] = Array()  //keeps each line of path in a line equation + the line's starting and end point (to calculate invalid positions)
  
  var start: Pos //start of the path
  
  var finish: Pos //end of the path
  
  def init() {  //when starting a level, this "initializes" said level. this is because it'll be easier to create custom levels.
    for (i <- 0 until waypoints.size-1) {
      pathLines = pathLines :+ (waypoints(i), waypoints(i+1)) //add the paths' starting and end points as pairs
    }
    start = waypoints(0) //the starting pos is always the first waypoint
    finish = waypoints(waypoints.size-1) //the end pos is always the last
    pathLineEqCalc //see this method
  }
  
  var spawnCooldown: Int = 10 //default spawn cooldown (in frames)
  
  var spawnCooldownCounter: Int = 0 //counter for spawn cooldown
  
  def spawn() { //call this every tick to spawn in the enemies
    if (spawnCooldownCounter >= spawnCooldown && !spawnQueue.isEmpty){
      enemyBuffer += spawnQueue.dequeue
      spawnCooldownCounter = 0
    } else spawnCooldownCounter += 1
  }
  
  var spawnQueue: Queue[Enemy] = Queue()   //the wave's enemies are first put into queue and then spawned by putting them into the enemyBuffer
  
  var enemyBuffer: Buffer[Enemy] = Buffer() //initially the buffers will be empty. enemies already on screen are in the enemyBuffer
  
  var towerBuffer: Buffer[Tower] = Buffer() //the towers on the map
  
  def nextWave {  //start of a wave
    val source = fromFile("waves.txt") //read the waves.txt
    val lines = source.getLines.mkString("\n")
    source.close()
    
    var waveData = lines.split("e")  //each wave is separated by e
    //pick the wave currently in
    var currentWaveData = waveData(game.Logic.waveNumber-1).trim.split("\n") //(0) = "W" + waveNumber  (1) = "cooldown:" + spawnCooldown  (2+) = enemyHealth + ":" + amount
    //set the spawn cooldown for this wave
    game.Logic.currentLevel.spawnCooldown = 
      currentWaveData(1)
      .split(":")(1).toInt
    
    currentWaveData = currentWaveData.drop(2)  //after this there is only information about the health and amounts of the enemies
    for (i <- 0 until currentWaveData.size) {  //for each line of spawn instructions...
      val health = currentWaveData(i).split(":")(0).toInt
      val amount = currentWaveData(i).split(":")(1).toInt
      for (a <- 1 to amount) {                                //...queue the corresponding enemies
        val add = new enemies.Target(this, health)
        spawnQueue.enqueue(add)
      }
    }
  }
}