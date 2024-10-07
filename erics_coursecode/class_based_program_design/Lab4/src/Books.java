import tester.*;

interface IBook {
  int daysOverdue(int day);
  boolean isOverdue(int day);
  double computeFine(int day);
}

abstract class ABook implements IBook {
  String title;
  int dayTaken;
  double FINE_PER_DAY = 0.1;
  int DUE_IN_DAYS = 14;
  
  ABook(String title, int dayTaken) {
    this.title = title;
    this.dayTaken = dayTaken;
  }
  
  public boolean isOverdue(int day) {
    return day > (dayTaken + DUE_IN_DAYS);
  }
  
  public int daysOverdue(int day) {
    return (day - dayTaken - DUE_IN_DAYS);
  }

  public double computeFine(int day) {
    return daysOverdue(day) * FINE_PER_DAY;
  }
}

class Book extends ABook {
  String author;
  
  Book(String title, int dayTaken, String author) {
    super(title, dayTaken);
    this.author = author;
  }
}

class RefBook extends ABook {
  int DUE_IN_DAYS = 2;
  
  RefBook(String title, int dayTaken) {
    super(title, dayTaken);
  }
  
  public boolean isOverdue(int day) {
    return day > (dayTaken + DUE_IN_DAYS);
  }
  
  public int daysOverdue(int day) {
    return (day - dayTaken - DUE_IN_DAYS);
  }
}

class AudioBook extends ABook {
  String author;
  double FINE_PER_DAY = 0.2;
  
  AudioBook(String title, int dayTaken, String author) {
    super(title, dayTaken);
    this.author = author;
  }
  
  public double computeFine(int day) {
    return this.daysOverdue(day) * FINE_PER_DAY;
  }
}

class ExamplesBook {
  IBook myBook = new Book("Standard book", 20, "Charles Dickens");
  IBook myReference = new RefBook("Thesaurus", 15);
  IBook myAudioBook = new AudioBook("A Feast for Crows", 10, "George RR Martin");
  
  boolean testDaysOverdue(Tester t) {
    return t.checkExpect(myBook.daysOverdue(36), 2) &&
        t.checkExpect(myReference.daysOverdue(20), 3) &&
        t.checkExpect(myAudioBook.daysOverdue(26), 2);
  }

  boolean testIsOverdue(Tester t) {
    return t.checkExpect(myBook.isOverdue(36), true) &&
        t.checkExpect(myBook.isOverdue(34), false) &&
        t.checkExpect(myReference.isOverdue(20), true) &&
        t.checkExpect(myReference.isOverdue(17), false) &&
        t.checkExpect(myAudioBook.isOverdue(26), true) &&
        t.checkExpect(myAudioBook.isOverdue(24), false);
  }
  
  boolean testcomputeFine(Tester t) {
    return t.checkInexact(myBook.computeFine(36), 0.2, 0.005) &&
        t.checkInexact(myReference.computeFine(20), 0.3, 0.005) &&
        t.checkInexact(myAudioBook.computeFine(26), 0.4, 0.005);
  }
}