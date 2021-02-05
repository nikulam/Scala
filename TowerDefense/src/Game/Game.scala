import processing.core._
import io.Source._

class Game extends PApplet {
  
   val map1 = """
.................
.................
>>>>>............
....|....>>>>>>..
....|....^....|..
....|....^....|..
....|....^....|..
....|....^....|..
....>>>>>>....|..
..............|..
..............|..
.....|<<<<<<<<|..
.....|...........
.....|...........
.....|>>>>>>>>>>>
.................
.................""".filter(n => n == '.' || n == '>' || n == '<' || n == '|' || n == '^')

  val map = new Map(map1)
  
  var levelDone = true
  var waveNumber = 0
  var waveDescription = ""
  var ended = false
   
  val player = new Player
  
  var enemies = Array[Enemy]()  
  var towers = Array[Tower]()
  var ammos = Array[Ammo]()
  
  var vihut = Array[Enemy]()
  
  def spawnEnemies = {
    if(!vihut.isEmpty) {
      enemies = enemies :+ vihut(0)
      vihut = vihut.drop(1)
    }
  }
  def nextWave = {
    val source = fromFile("waves.txt")
    val lines = source.getLines.mkString("\n")
    source.close()
    var waveData = lines.split("e")
    var currentWaveData = waveData(waveNumber).trim.split("\n")
    waveDescription = currentWaveData(0)
    val enemyInfo = currentWaveData(1).trim().split(":")
    if(waveDescription == "fin") ended = true
    else {
    for(i <- 0 until enemyInfo(1).toInt) {
      enemyInfo(0) match {
        case "1" => vihut = vihut :+ new BasicEnemy
        case "2" => vihut = vihut :+ new FastEnemy
        case "3" => vihut = vihut :+ new StrongEnemy
      }
    }
    }
  }
  
  
  
}