package leveldata
import o1.Pos
import scala.collection.mutable.Queue

object Level2 extends data.Level {  
/*
* Should look something like this:
* 
* 	OOOOOO
* 	OPPPPO
*   OPOOPO
* ->PPOPPO
* 	OOOPOO
* 	OOOPOO
* 		 V
* 
* where P = path, O = turret placement area
*/
  
  
  
  val waypoints = Array(new Pos(0, 300), new Pos(200, 300), new Pos(200, 100), new Pos(600, 100), new Pos(600, 200), new Pos(400, 200), new Pos(400, 600))
  var start = waypoints(0)
  var finish = waypoints(waypoints.size-1)
  
//  spawnQueue = Queue(new enemies.Target(this, 1), new enemies.Target(this, 2), new enemies.Target(this, 3), new enemies.Target(this, 3), new enemies.Target(this, 3))
//  spawnCooldown = 30
  
}