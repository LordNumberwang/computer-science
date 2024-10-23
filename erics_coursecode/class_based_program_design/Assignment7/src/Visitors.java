import java.util.function.*;
import tester.*;

interface IArith {
  public <R> R accept(IArithVisitor<R> visitor);
}

class Const implements IArith {
  double num;
  Const(double num) {
    this.num = num;
  }
  @Override
  public <R> R accept(IArithVisitor<R> visitor) {
    // TODO Auto-generated method stub
    return visitor.visitConst(this);
  }
}

class UnaryFormula implements IArith {
  Function<Double,Double> func;
  String name;
  IArith child;
  
  UnaryFormula(Function<Double,Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnary(this);
  }
}

class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;
  
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }
  @Override
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinary(this);
  }
}

interface IArithVisitor<R> extends Function<IArith,R> {
  R apply(IArith arith);
  R visitConst(Const c);
  R visitUnary(UnaryFormula uf);
  R visitBinary(BinaryFormula bf);
}

class EvalVisitor implements IArithVisitor<Double> {
  public Double apply(IArith arith) {
    return arith.accept(this);
  }

  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitUnary(UnaryFormula uf) {
    return uf.func.apply(this.apply(uf.child));
  }

  public Double visitBinary(BinaryFormula bf) {
    return bf.func.apply(this.apply(bf.left), this.apply(bf.right));
  }
}

class PrintVisitor implements IArithVisitor<String> {
  public String apply(IArith arith) {
    return arith.accept(this);
  }

  @Override
  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  @Override
  public String visitUnary(UnaryFormula uf) {
    return "(" + uf.name + " " + this.apply(uf.child) + ")";
  }

  @Override
  public String visitBinary(BinaryFormula bf) {
    return "(" + bf.name + " " + this.apply(bf.left) + " " + this.apply(bf.right) +")";
  }
}

class DoublerVisitor implements IArithVisitor<IArith> {
  public IArith apply(IArith arith) {
    return arith.accept(this);
  }
  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }
  public IArith visitUnary(UnaryFormula uf) {
    return new UnaryFormula(uf.func, uf.name, this.apply(uf.child));
  }
  public IArith visitBinary(BinaryFormula bf) {
    return new BinaryFormula(bf.func, bf.name, this.apply(bf.left), this.apply(bf.right));
  }
}

class NoNegativeVisitor implements IArithVisitor<Boolean> {
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }

  public Boolean visitConst(Const c) {
    return c.num > 0;
  }

  public Boolean visitUnary(UnaryFormula uf) {
    // order matters here, descend to lowest levels first then come up and eval current
    return this.apply(uf.child) && (new EvalVisitor().visitUnary(uf) > 0);
  }

  public Boolean visitBinary(BinaryFormula bf) {
    // order matters here, descend to lowest levels first then come up and eval current
    return this.apply(bf.left) && this.apply(bf.right) && 
        (new EvalVisitor().visitBinary(bf) > 0); 
  }
}

class ExamplesVisitors {
  class Plus implements BiFunction<Double,Double,Double> {
    public Double apply(Double t, Double u) {
      return t+u;
    }
  }
  class Minus implements BiFunction<Double,Double,Double> {
    public Double apply(Double t, Double u) {
      return t-u;
    }
  }
  class Mul implements BiFunction<Double,Double,Double> {
    public Double apply(Double t, Double u) {
      return t*u;
    }
  }
  class Div implements BiFunction<Double,Double,Double> {
    public Double apply(Double t, Double u) {
      return t/u;
    }
  }
  class Neg implements Function<Double,Double> {
    public Double apply(Double t) {
      return t*-1;
    }
  }
  class Sqr implements Function<Double,Double> {
    public Double apply(Double t) {
      return t*t;
    }
  }
  
  //"(div (plus 1.0 2.0) (neg 1.5))"
  IArith expr1 = new BinaryFormula(new Div(), "div", 
      new BinaryFormula(new Plus(),"plus",new Const(1.0),new Const(2.0)), 
      new UnaryFormula(new Neg(),"neg",new Const(1.5)));
  //"(mul (minus 7.5 1.5) (sqr 2.0))"
  IArith expr2 = new BinaryFormula(new Mul(), "mul",
      new BinaryFormula(new Minus(),"minus", new Const(7.5),new Const(1.5)),
      new UnaryFormula(new Sqr(),"sqr", new Const(2.0)));
  IArith expr1_doubled = new BinaryFormula(new Div(), "div", 
      new BinaryFormula(new Plus(),"plus",new Const(2.0),new Const(4.0)), 
      new UnaryFormula(new Neg(),"neg",new Const(3.0)));
  IArith expr2_doubled = new BinaryFormula(new Mul(), "mul",
      new BinaryFormula(new Minus(),"minus", new Const(15.0),new Const(3.0)),
      new UnaryFormula(new Sqr(),"sqr", new Const(4.0)));

  void testEvalVisitor(Tester t) {
    t.checkInexact(new EvalVisitor().apply(expr1),-2.0, 0.05);
    t.checkInexact(new EvalVisitor().apply(expr2),24.0, 0.05);
  }
  
  void testPrintVisitor(Tester t) {
    t.checkExpect(new PrintVisitor().apply(expr1),"(div (plus 1.0 2.0) (neg 1.5))");
    t.checkExpect(new PrintVisitor().apply(expr2),"(mul (minus 7.5 1.5) (sqr 2.0))");
  }
  
  void testDoublerVisitor(Tester t) {
    t.checkExpect(new DoublerVisitor().apply(expr1), expr1_doubled);
    t.checkExpect(new DoublerVisitor().apply(expr2), expr2_doubled);
  }
  
  void testNegativeVisitor(Tester t) {
    t.checkExpect(new NoNegativeVisitor().apply(expr1), false);
    t.checkExpect(new NoNegativeVisitor().apply(expr2), true);
  }
}