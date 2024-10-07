import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import javalib.worldcanvas.WorldCanvas;
import java.awt.Color;          // general colors (as triples of red,green,blue values)
                                // and predefined colors (Color.RED, Color.GRAY, etc.)

class MyPosn extends Posn {
  MyPosn(int x, int y) {
    super(x,y);
  }
  
  MyPosn(Posn p) {
    this(p.x, p.y);
  }
  
  public MyPosn add(MyPosn that) {
    return new MyPosn(this.x + that.x, this.y + that.y);
  }
  
  public boolean isOffscreen(int width, int height) {
    return (this.x > width || this.x < 0 || 
        this.y > height || this.y < 0);
  }
}

class Circle {
  int radius; //radius in pixels
  MyPosn position; //in pixels
  MyPosn velocity; //in pixels/tick
  Color color; //e.g. Color.RED
  OutlineMode fill; //either OutlineMode.OUTLINE or OutlineMode.SOLID
  
  Circle(int radius, MyPosn posn, MyPosn v, Color color, OutlineMode fill) {
    this.radius = radius;
    this.position = posn;
    this.velocity = v;
    this.color = color;
    this.fill = fill;
  }
  
  Circle(MyPosn posn, MyPosn v) {
    this(5, posn, v, Color.RED, OutlineMode.SOLID); //convenience constructor for red circles.
  }
  
  public Circle move() {
    return new Circle(this.radius, this.position.add(this.velocity), 
        this.velocity, this.color, this.fill);
  }
  
  public boolean isOffscreen(int width, int height) {
    return this.position.isOffscreen(width, height);
  }
  
  public WorldImage draw() {
    WorldImage circImage = new CircleImage(this.radius, this.fill, this.color);
    return circImage;
  }
  
  public WorldScene place(WorldScene ws) {
    return ws.placeImageXY(this.draw(), this.position.x, this.position.y);
    //Note not checking for off screen here - removal implemented in List later.
  }
}

interface ILoCircle {
  ILoCircle moveAll();
  ILoCircle removeOffScreen(int width, int height);
  int countOffscreen(int width, int height);
  WorldScene placeAll(WorldScene ws);
  WorldScene placeAllHelper(WorldScene ws);
}

class MtCircle implements ILoCircle {
  MtCircle() {};
  
  public ILoCircle moveAll() {
    return this;
  }
  
  public ILoCircle removeOffScreen(int width, int height) {
    return this;
  }
  
  public int countOffscreen(int width, int height) {
    return 0;
  }
  
  public WorldScene placeAll(WorldScene ws) {
    return ws;
  }
  public WorldScene placeAllHelper(WorldScene ws) {
    return ws;
  }
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;
  
  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
  }
  
  public ILoCircle removeOffScreen(int width, int height) {
    if (this.first.isOffscreen(width, height)) {
      return this.rest.removeOffScreen(width, height);
    } else {
      return new ConsLoCircle(this.first, this.rest.removeOffScreen(width, height));
    }
  }
  
  public int countOffscreen(int width, int height) {
    if (this.first.isOffscreen(width, height)) {
      return 1 + this.rest.countOffscreen(width, height);
    } else {
      return this.rest.countOffscreen(width, height);
    }
  }

  //placeAll - places a list of circles on a given WorldScene.
  public WorldScene placeAll(WorldScene ws) {
    //use a helper to avoid calling this removal repeatedly
    return this.removeOffScreen(ws.width, ws.height).placeAllHelper(ws); 
  }
  public WorldScene placeAllHelper(WorldScene ws) {
    return this.rest.placeAllHelper(this.first.place(ws));
  }
}

class CircleWorld extends World {
  int width;
  int height;
  int remainingCircles;
  ILoCircle circles;
  
  CircleWorld(int width, int height, int numCircles, ILoCircle startCircles) {
    this.width = width;
    this.height = height;
    this.remainingCircles = numCircles;
    this.circles = startCircles;
  }
  
  CircleWorld(int numCircles) {
    this(500,500,numCircles,new MtCircle());
  }
  
  public WorldScene getEmptyScene() {
    return new WorldScene(this.width, this.height);
  }

  public WorldScene makeScene() {
    return circles.placeAll(this.getEmptyScene());
  };
  
  public boolean bigBang(double speed) {
    return super.bigBang(this.width, this.height, speed);
  }
  
  public World onMouseClicked(Posn pos) {
    this.circles = new ConsLoCircle(new Circle(new MyPosn(pos), new MyPosn(0,-10)), this.circles);
    return this;
  }
  
  public World onTick() {
    this.circles = this.circles.moveAll();
    this.remainingCircles = this.remainingCircles - this.circles.countOffscreen(this.width, this.height);
    this.circles = this.circles.removeOffScreen(this.width, this.height);
    return this;
  }
  
  public WorldEnd worldEnds() {
    if (this.remainingCircles < 1) {
      return new WorldEnd(true, this.makeScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

class ExamplesImageTesting {
  Circle redCircle = new Circle(new MyPosn(100,320), new MyPosn(5,1));
  Circle blueCircle = new Circle(10, new MyPosn(200,110), new MyPosn(1,3), Color.BLUE, OutlineMode.SOLID);
  Circle greenCircle = new Circle(15, new MyPosn(50,110), new MyPosn(1,3), Color.GREEN, OutlineMode.SOLID);
  ILoCircle myCircles = new ConsLoCircle(redCircle, 
      new ConsLoCircle(blueCircle,
          new ConsLoCircle(greenCircle, new MtCircle())));
  
  boolean testImages(Tester t) {
    return t.checkExpect(new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY),
        new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY));
  }
  
  boolean testIsOffscreen(Tester t) {
    return t.checkExpect(redCircle.isOffscreen(5, 5), true) &&
        t.checkExpect(blueCircle.isOffscreen(500, 500), false);
  }
  
  boolean testCountOffscreen(Tester t) {
    return t.checkExpect(myCircles.countOffscreen(500, 500), 0);
  }

  boolean testMoveAll(Tester t) {
    return t.checkExpect(redCircle.move(), new Circle(5, new MyPosn(100,320), new MyPosn(5,1), Color.RED, OutlineMode.SOLID));
  }
  //boolean test
  
  //Display sample images:
  //  boolean testDrawCircle(Tester t) {
  //    WorldCanvas c = new WorldCanvas(500, 500);
  //    WorldScene s = new WorldScene(500, 500);
  //    return c.drawScene(redCircle.place(s))
  //        && c.show();
  //  }
  
//  boolean testDrawCircles(Tester t) {
//    WorldCanvas c = new WorldCanvas(500, 500);
//    WorldScene s = new WorldScene(500, 500);
//    return c.drawScene(myCircles.placeAll(s))
//        && c.show();
//  }
  
  boolean testCircleWorld(Tester t) {
    CircleWorld cw = new CircleWorld(3);
    return cw.bigBang(1.0 / 30.0);
  }
}