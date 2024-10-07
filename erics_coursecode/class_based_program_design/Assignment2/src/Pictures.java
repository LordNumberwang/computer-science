import tester.Tester;

interface IPicture { 
  int getWidth();
  int countShapes();
  int comboDepth();
  IPicture mirror();
  String pictureRecipe(int depth);
}

interface IOperation { 
  int calcWidth();
  int calcShapeCount();
  int calcDepth();
  IOperation reverse();
  String expandRecipe(int depth);
}

class Shape implements IPicture {
  String kind;
  int size;
  
  Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }
  
  public int getWidth() {
    return this.size;
  }
  
  public int countShapes() {
    return 1;
  }
  
  public int comboDepth() {
    return 0;
  }
  
  public IPicture mirror() {
    return this;
  }
  
  public String pictureRecipe(int depth) {
    return this.kind;
  }
}

class Combo implements IPicture {
  String name;
  IOperation operation;
  
  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }
  
  public int getWidth() {
    return this.operation.calcWidth();
  }
  
  public int countShapes() {
    return this.operation.calcShapeCount();
  }
  
  public int comboDepth() {
    return this.operation.calcDepth();
  }
  
  public IPicture mirror() {
    return new Combo(name, this.operation.reverse());
  }
  
  public String pictureRecipe(int depth) {
    if (depth <= 0) {
      return this.name;
    } else {
      return this.operation.expandRecipe(depth);
    }
  }
}

class Scale implements IOperation {
  IPicture pic;
  
  Scale(IPicture pic) {
    this.pic = pic;
  }
  
  public int calcWidth() {
    return 2 * this.pic.getWidth();
  }
  
  public int calcShapeCount() {
    return this.pic.countShapes();
  }
  
  public int calcDepth() {
    return 1 + this.pic.comboDepth();
  }
  
  public IOperation reverse() {
    return new Scale(this.pic.mirror());
  }
  
  public String expandRecipe(int depth) {
    return "scale(" + this.pic.pictureRecipe(depth-1) + ")";      
  }
}

class Beside implements IOperation {
  IPicture leftPic;
  IPicture rightPic;
  
  Beside(IPicture leftPic, IPicture rightPic) {
    this.leftPic = leftPic;
    this.rightPic = rightPic;
  }
  
  public int calcWidth() {
    return this.leftPic.getWidth() + this.rightPic.getWidth();
  }
  
  public int calcShapeCount() {
    return this.leftPic.countShapes() + this.rightPic.countShapes();
  }
  
  public int calcDepth() {
    int leftDepth = this.leftPic.comboDepth();
    int rightDepth = this.rightPic.comboDepth();
    if (leftDepth > rightDepth) {
      return 1 + leftDepth;
    } else {
      return 1 + rightDepth;
    }
  }
  
  public IOperation reverse() {
    return new Beside(this.rightPic.mirror(), this.leftPic.mirror());
  }

  public String expandRecipe(int depth) {
    return "beside(" + this.leftPic.pictureRecipe(depth-1) + ", " + this.rightPic.pictureRecipe(depth-1) + ")";
  }
}

class Overlay implements IOperation {
  IPicture pic1;
  IPicture pic2;
  
  Overlay(IPicture pic1, IPicture pic2) {
    this.pic1 = pic1;
    this.pic2 = pic2;
  }
  
  public int calcWidth() {
    int pic1Width = this.pic1.getWidth(); //memoize
    int pic2Width = this.pic2.getWidth();
    
    if (pic1Width > pic2Width) {
      return pic1Width;
    } else {
      return pic2Width;
    }
  }
  
  public int calcShapeCount() {
    return this.pic1.countShapes() + this.pic2.countShapes();
  }
  
  public int calcDepth() {
    int pic1Depth = this.pic1.comboDepth();
    int pic2Depth = this.pic2.comboDepth();
    if (pic1Depth > pic2Depth) {
      return 1 + pic1Depth;
    } else {
      return 1 + pic2Depth;
    }
  }
  
  public IOperation reverse() {
    return new Overlay(this.pic1.mirror(), this.pic2.mirror());
  }
  
  public String expandRecipe(int depth) {
    return "overlay(" + this.pic1.pictureRecipe(depth-1) + ", " + this.pic2.pictureRecipe(depth-1) + ")";
  }
}

class ExamplesPicture {
  IPicture circle = new Shape("circle", 20);
  IPicture square = new Shape("square", 30);
  IPicture bigCircle = new Combo("big circle", new Scale(circle));
  IPicture squareOnCircle = new Combo("square on circle", new Overlay(square,bigCircle));
  IPicture doubleSquareOnCircle = new Combo("double square on circle", new Beside(squareOnCircle, squareOnCircle));
  IPicture triangle = new Shape("triangle",15);

  IPicture bigSquare = new Combo("big square", new Scale(square));
  IPicture triOnSquare = new Combo("triangle on square", new Overlay(triangle,square));
  IPicture triBesideBigSquare = new Combo("triangle beside big square", new Beside(triangle,bigSquare));
  
  /* For Mirroring Test Case
   * Test Case          => Mirrored
   * C = (A | B)        => C' = (B | A)
   * D = ((A|B) | B)  => D' = ((B) | (B|A))
   * E = (((A|B) | B) | (A|B)) => E' = ((B|A) | (B | (B|A)))
   */
  
  IPicture a = new Combo("A", new Scale(circle));
  IPicture b = new Combo("B", new Scale(square));
  IPicture c = new Combo("C", new Beside(a,b));
  IPicture cMirror = new Combo("C", new Beside(b,a));
  IPicture d = new Combo("D",new Beside(c,b));
  IPicture dMirror = new Combo("D", new Beside(b,cMirror));
  IPicture e = new Combo("E", new Beside(d,c));
  IPicture eMirror = new Combo("E", new Beside(cMirror,dMirror));
  
  boolean testGetWidth(Tester t) {
    return t.checkExpect(circle.getWidth(),20) &&
        t.checkExpect(bigCircle.getWidth(),40) &&
        t.checkExpect(squareOnCircle.getWidth(), 40) &&
        t.checkExpect(doubleSquareOnCircle.getWidth(), 80);
  }
  
  boolean testCalcWidth(Tester t) {
    return t.checkExpect(doubleSquareOnCircle.countShapes(), 4) &&
        t.checkExpect(squareOnCircle.countShapes(), 2) &&
        t.checkExpect(triBesideBigSquare.countShapes(), 2);
  }
  
  boolean testComboDepth(Tester t) {
    return t.checkExpect(doubleSquareOnCircle.comboDepth(), 3);
  }
  
  boolean testMirror(Tester t) {
    return t.checkExpect(c.mirror(), cMirror) &&
        t.checkExpect(d.mirror(), dMirror) &&
        t.checkExpect(e.mirror(), eMirror);
  }
  
  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(doubleSquareOnCircle.pictureRecipe(0), "double square on circle") &&
        t.checkExpect(doubleSquareOnCircle.pictureRecipe(1), "beside(square on circle, square on circle)") &&
        t.checkExpect(squareOnCircle.pictureRecipe(1), "overlay(square, big circle)") &&
        t.checkExpect(doubleSquareOnCircle.pictureRecipe(2), "beside(overlay(square, big circle), overlay(square, big circle))") &&
        t.checkExpect(doubleSquareOnCircle.pictureRecipe(3), "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))") &&
        t.checkExpect(doubleSquareOnCircle.pictureRecipe(20), "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))");
  }
}