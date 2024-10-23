import tester.*;
import java.util.function.*;

//Represents functions of signature A -> R, for some argument type A and
//result type R
interface IFunc<A, R> {
  R apply(A input);
}

interface IBiFunc<A1,A2,R> {
  R apply(A1 arg1, A2 arg2);
}

//generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);
  
  // foldr/reduce over a list, given a starting value.
  // Return a single end value of the same type as the starting value.
  // return U, when given a function of T,U->U
  <U> U foldr(IBiFunc<T,U,U> f, U baseVal);
  <U> U realFoldr(BiFunction<T,U,U> f, U baseVal);
}

//empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <U> U foldr(IBiFunc<T, U, U> f, U baseVal) {
    return baseVal;
  }

  public <U> U realFoldr(BiFunction<T, U, U> f, U baseVal) {
    return baseVal;
  }
}

//non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  @Override
  public <U> U foldr(IBiFunc<T, U, U> f, U baseVal) {
    return this.rest.foldr(f, f.apply(this.first, baseVal));
  }

  @Override
  public <U> U realFoldr(BiFunction<T, U, U> f, U baseVal) {
    return this.rest.realFoldr(f, f.apply(this.first, baseVal));
  }
}

class ExamplesVisitor {
  ConsList<Integer> testInts = new ConsList<Integer>(5, 
      new ConsList<Integer>(10,
          new ConsList<Integer>(2, new MtList<Integer>())));
  ConsList<String> testStrs = new ConsList<String>("5",
      new ConsList<String>("10",
          new ConsList<String>("2", new MtList<String>())));
  
  class IntToString implements IFunc<Integer, String> {
    public String apply(Integer input) {
      return input.toString();
    }
  }
  class SumInts implements IBiFunc<Integer,Integer,Integer> {
    public Integer apply(Integer int1, Integer int2) {
      return int1+int2;
    }
  }  
  class SumIntAlt implements BiFunction<Integer,Integer,Integer> {
    public Integer apply(Integer int1, Integer int2) {
      return int1+int2;
    }
  }
  
  boolean testMap(Tester t) {
    return t.checkExpect(testInts.map(new IntToString()), testStrs);
  }
  
  boolean testFoldr(Tester t) {
    return t.checkExpect(testInts.foldr(new SumInts(), 0), 17);
  }
  
  boolean testRealFoldr(Tester t) {
    return t.checkExpect(testInts.realFoldr(new SumIntAlt(), 0), 17);
  }
  
  JSON jsBlank = new JSONBlank();
  JSON jsNum = new JSONNumber(123);
  JSON jsBool = new JSONBool(true);
  JSON jsStr = new JSONString("weee");
  IList<JSON> jsList = new ConsList<JSON>(jsBlank,
      new ConsList<JSON>(jsNum, 
          new ConsList<JSON>(jsBool,
              new ConsList<JSON>(jsStr, new MtList<JSON>()))));
  //Map over a list of JSON and produce all of their numbers as a test.
  IList<Integer> testJsVisitors = new ConsList<Integer>(0, 
      new ConsList<Integer>(123, 
          new ConsList<Integer>(1,
              new ConsList<Integer>(4, new MtList<Integer>()))));

  boolean testMapJsonVisitor(Tester t) {
    return t.checkExpect(jsList.map(new JSONToNumber()),testJsVisitors);
  }
}