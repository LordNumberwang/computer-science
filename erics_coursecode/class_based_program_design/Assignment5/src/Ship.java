import javalib.worldimages.Posn;

public class Ship {
  //spawnRate defined in world
  
  Posn position;
  Posn velocity;
  
  //define a ship's position, velocity
  Ship(Posn pos, Posn v) {
    this.position = pos;
    this.velocity = v;
  }
  
  //Convenience constructor... choose left or right.
  Ship(String direction) {
//    if (direction == "left") {
//      this(, new Posn());
//    } else if (direction == "right") {
//      this(, new Posn());
//    }
  }
}
