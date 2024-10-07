import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

class NBullets extends World {
  int width;  //width of canvas
  int height; //height of canvas
  double gameSpeed; //inverse of FPS, suggested 1.0/28.0

  int bulletsLeft; //total number of bullets until game end
  int shipsDestroyed; //ships destroyed
  int shipSpawn; //ship spawn rate in ticks, e.g. 3 ticks per spawn
  int maxSpawn; //max spawn per tick

  //As well as the bullets and the ships, 
  //the player must also be able to see how many bullets are left
  //how many ships have been destroyed so far.
  
  NBullets(int bullets, int width, int height, double gameSpeed) {
    this.bulletsLeft = bullets;
    if ( width < 0 || height < 0 ) {
      throw new IllegalArgumentException("Invalid arguments passed to constructor.");
    }
    this.width = width;
    this.height = height;
    this.gameSpeed = gameSpeed;
    this.shipsDestroyed = 0;
    //add other creation criteria as needed
  }
  
  NBullets(int bullets) {
    //convenience constructor
    this(bullets, 
        500, 300, //canvas widthxheight 500x300
        1.0/28.0) //gamespeed in 1/FPS.
    ;
  }

  //WorldScene called per frame of the game runtime
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(this.width, this.height);
    //TODO add stuff once we know what we're drawing...
    return ws;
  }
  
  public boolean bigBang(double gameSpeed) {
    this.gameSpeed = gameSpeed;
    return super.bigBang(this.width, this.height, gameSpeed);
  }

  public boolean bigBang() {
    return super.bigBang(this.width, this.height, this.gameSpeed);
  }
  
  public WorldEnd worldEnds() {
    if (isGameOver()) {
      return new WorldEnd(true, this.makeScene());
      //TODO if desired, game end screen
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
  
  public boolean isGameOver() {
    //TODO implement: game end
    /*
     * Your game must end when there are no more bullets to fire 
     * and there are no bullets left on the screen.
     */
    return true;
  }

  public NBullets onKeyEvent(String key) {
    //did we press the space update the final tick of the game by 10. 
    if (key.equals(" ") && this.bulletsLeft > 0 ) {
      //I'd rather just update this object and return it
      //TODO create a new Bullet()
      this.bulletsLeft -= 1;
    }
    return this;
 }
}

class ExamplesNBullets {
  boolean testBigBang(Tester t) {
  NBullets world = new NBullets(10);
  //width, height, tickrate = 0.5 means every 0.5 seconds the onTick method will get called.
    return world.bigBang();
  }
}