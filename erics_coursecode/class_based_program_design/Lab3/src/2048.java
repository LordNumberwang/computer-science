import tester.Tester;

interface IGamePiece {
  IGamePiece merge(IGamePiece otherPiece);
  int getValue();
  public boolean isValid();
}

abstract class AGamePiece implements IGamePiece {
  int value;

  public abstract boolean isValid();
  public abstract int getValue();
  public IGamePiece merge(IGamePiece otherPiece) {
    return new MergeTile(this, otherPiece);
  }
}

class BaseTile extends AGamePiece {
  BaseTile(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public boolean isValid() {
    return (this.value > 0);
  }
}

class MergeTile extends AGamePiece {
  IGamePiece piece1, piece2;
  
  MergeTile(IGamePiece piece1, IGamePiece piece2) {
    this.piece1 = piece1;
    this.piece2 = piece2;
  }
  
  public int getValue() {
    if (this.value > 0) {
      return this.value;
    } else {
      //Memoize result to value property
      this.value = piece1.getValue() + piece2.getValue();
      return this.value;
    }
  }

  public boolean isValid() {
    return (this.piece1.getValue() == this.piece2.getValue());
  }
}

class ExamplesGamePieces {
  IGamePiece two = new BaseTile(2);
  IGamePiece four = new MergeTile(two, two);
  IGamePiece six = new MergeTile(two, four);

  boolean testGetValue(Tester t) {
    return t.checkExpect(two.getValue(),2) &&
        t.checkExpect(four.getValue(),4) &&
        t.checkExpect(six.getValue(), 6);
  }
  
  boolean testIsValid(Tester t) {
    return t.checkExpect(two.isValid(), true) &&
        t.checkExpect(four.isValid(), true) &&
        t.checkExpect(six.isValid(), false);
  }
}