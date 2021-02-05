import processing.core._
import processing.core.PApplet


class Map(path: String) extends PApplet {
  
  val goal = route2.takeRight(1)(0)
  
  def route1 = {
    var a = Array[PVector]()
    var b = Array[PVector]()
    for(i <- 0 until path.length) {
      if(path(i) == '<' || path(i) == '^') {
        b = b :+ new PVector(i % 17 * 50, i / 17 * 50)
      }
      else {
        a = a ++ b.reverse
        b = Array[PVector]()
        if(path(i) == '>' || path(i) == '|') {
          a = a :+ new PVector(i % 17 * 50, i / 17 * 50)
        }
      }
    }
    a
  }
  
  // Route2 is used for enemies movement.
  def route2 = {
    route1.map(n => new PVector(n.x + 25, n.y + 25))
  }
  
  
  def everyPix = {
    var c = Array[PVector]()
    val r = route1.map(n => new PVector(n.x + 25, n.y + 25))
    var i = 0
    var next = r(0)
    while(i < r.length - 1) {
      val diff = new PVector(r(i+1).x - r(i).x, r(i+1).y - r(i).y)
      
        if(diff.x > 0) {
          (0 until 50).map(n => c = c :+ new PVector(r(i).x + n, r(i).y))
        }
        else if(diff.x < 0) {
          (0 until 50).map(n => c = c :+ new PVector(r(i).x - n, r(i).y))
        }  
        else if(diff.y > 0) {
          (0 until 50).map(n => c = c :+ new PVector(r(i).x, r(i).y + n))
        }
        else if(diff.y < 0) {
          (0 until 50).map(n => c = c :+ new PVector(r(i).x, r(i).y - n))
        }
      i += 1
    }
    c
  }
}
