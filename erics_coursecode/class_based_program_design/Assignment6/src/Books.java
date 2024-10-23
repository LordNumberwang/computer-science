import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.*;
import java.util.function.Predicate;

class Book {
  String title;
  String author;
  int price;
  
  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

//generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(Function<T, U> f);
  // foldr/reduce over a list, given a starting value.
  // Return a single end value of the same type as the starting value.
  // return U, when given a function of T,U->U
  <U> U realFoldr(BiFunction<T,U,U> f, U baseVal);

  IList<T> filter(Predicate<T> pred);
  IList<T> sort(Comparator<T> comp);
  int length();
}

class MtList<T> implements IList<T> {
  public int length() { return 0; }
  @Override
  public <U> IList<U> map(Function<T, U> f) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public <U> U realFoldr(BiFunction<T, U, U> f, U baseVal) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public IList<T> filter(Predicate<T> pred) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public IList<T> sort(Comparator<T> comp) {
    // TODO Auto-generated method stub
    return null;
  }
}
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  @Override
  public <U> IList<U> map(Function<T, U> f) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public <U> U realFoldr(BiFunction<T, U, U> f, U baseVal) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public IList<T> filter(Predicate<T> pred) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public IList<T> sort(Comparator<T> comp) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public int length() {
    // TODO Auto-generated method stub
    return 0;
  }
}

//Showing what a minimal Comparator<T> would look like. 
interface IMyComparator<T> {
  int compare(T t1, T t2);
}

//Using Java's inbuilt Comparator<T> instead since it is good practice to do so.
class BooksByTitle implements Comparator<Book> {
  @Override
  public int compare(Book o1, Book o2) {
    return o1.title.compareTo(o2.title);
  }
}

class BooksByAuthor implements Comparator<Book> {
  @Override
  public int compare(Book o1, Book o2) {
    if (o1.price < o2.price) {
      return -1;
    } else if (o1 == o2) {
      return 0;
    } else {
      return 1;
    }
  }
}

class BooksByPrice implements Comparator<Book> {
  @Override
  public int compare(Book o1, Book o2) {
    return o1.author.compareTo(o2.author);
  }
}