package leveldata
import o1.Pos
import scala.collection.mutable.Queue

object TestLevel extends data.Level {  
/*
* a simple level with one 90 degree turn. should look something like this:
* 
* 	OOOOO
* 	OOOOO
* 	PPPOO
* 	OOPOO
* 	OOPOO
* 
* where P = path, O = turret placement area
*/
  
  
  
  val waypoints = Array(new Pos(0, 300), new Pos(400, 300), new Pos(400, 600))
  var start = waypoints(0)
  var finish = waypoints(waypoints.size-1)
  
  
}