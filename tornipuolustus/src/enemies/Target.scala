package enemies
import data.Enemy 
import data.Level

//Enemies from weakest to strongest: Red, blue, green, yellow. 

class Target(val level: Level, hp: Int) extends Enemy {
  health = hp
}

