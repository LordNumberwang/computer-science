import tester.*;
import java.util.function.*;

class Deque<T> {
  Sentinel<T> header;
  
  Deque() {
    this.header = new Sentinel<T>();
  }
  
  Deque(Sentinel<T> head) {
    this.header = head;
  }
  
  public int size() {
    return header.countHelper(header);
  }
  
  //Design the method addAtHead for the class Deque that consumes a value of type T and inserts it 
  //at the front of the list. Be sure to fix up all the links correctly!
  public void addAtHead(T data) {
    new Node<T>(data,header,header.next);
  }
  
  //Design the method addAtTail for the class Deque that consumes a value of type T and inserts it 
  //at the tail of this list. Again, be sure to fix up all the links correctly!
  public void addAtTail(T data) {
    new Node<T>(data, header.prev, header);
  }
  
  /*
   * Design the method removeFromHead for the class Deque that removes the first node from this Deque. 
   * Throw a RuntimeException if an attempt is made to remove from an empty list. 
   * Be sure to fix up all the links correctly! 
   * As with ArrayList’s remove method, return the item that’s been removed from the list.
   */
  public ANode<T> removeFromHead() {
    if (header.next == header) {
      throw new RuntimeException("Empty List");
    } else {
      ANode<T> oldFirst = header.next;
      header.next = oldFirst.next;
      header.next.prev = ((ANode<T>) header);
      return oldFirst;
    }
  }
  
  public ANode<T> removeFromTail() {
    if (header.next == header) {
      throw new RuntimeException("Empty List");
    } else {
      ANode<T> oldLast = header.prev;
      header.prev = oldLast.prev;
      header.prev.next = ((ANode<T>) header);
      return oldLast;
    }
  }
  
  /*
   * Design the method find for the class Deque that takes an Predicate<T> 
   * and produces the first node in this Deque for which the given predicate returns true.
   */
  public ANode<T> find(Predicate<T> pred) {
    return header.next.findHelper(pred);
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  abstract int countHelper(Sentinel<T> head);
  public T getData() { 
    return null;
  }
  abstract ANode<T> findHelper(Predicate<T> pred);
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  public int countHelper(Sentinel<T> head) {
    if (this == next) {
      return 0;
    } else {
      return next.countHelper(head);
    }
  }
  
  public ANode<T> findHelper(Predicate<T> pred) {
    return this;
  }
}


class Node<T> extends ANode<T> {
  T data;
  
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }
  Node(T data, ANode<T> aPrev, ANode<T> aNext) {
    this(data);
    if (aPrev == null || aNext == null) {
      throw new IllegalArgumentException("Received a null node pointer");
    }
    this.next = aNext;
    this.prev = aPrev;
    aNext.prev = this;
    aPrev.next = this;
  }
  
  public T getData() { 
    return data;
  }
  
  public int countHelper(Sentinel<T> head) {
    if (next == head) {
      return 1;
    } else {
      return 1 + next.countHelper(head);
    }
  }
  
  public ANode<T> findHelper(Predicate<T> pred) {
    if (pred.test(data)) {
      //Honestly the question should say return null, if not found. 
      //Returning the Sentinel here is bad design, in my opinion.
      return this;
    } else {
      return next.findHelper(pred);
    }
  }
}

class ExamplesDeque {

  Deque<String> deque1 = new Deque<String>();

  ANode<String> s2 = new Sentinel<String>();
  ANode<String> s2Node1 = new Node<String>("abc",s2,s2);
  ANode<String> s2Node2 = new Node<String>("bcd",s2Node1,s2);
  ANode<String> s2Node3 = new Node<String>("cde",s2Node2,s2);
  ANode<String> s2Node4 = new Node<String>("def",s2Node3,s2);
  Deque<String> deque2 = new Deque<String>((Sentinel<String>) s2);
  
  ANode<String> s3 = new Sentinel<String>();
  ANode<String> s3Node1 = new Node<String>("bravo",s3,s3);
  ANode<String> s3Node2 = new Node<String>("delta",s3Node1,s3);
  ANode<String> s3Node3 = new Node<String>("charlie",s3Node2,s3);
  ANode<String> s3Node4 = new Node<String>("echo",s3Node3,s3);
  ANode<String> s3Node5 = new Node<String>("alpha",s3Node4,s3);
  ANode<String> s3Node6 = new Node<String>("foxtrot",s3Node5,s3);
  Deque<String> deque3 = new Deque<String>((Sentinel<String>) s3);
  
  void testSampleData(Tester t) {
    t.checkExpect(s2Node1.prev, s2);
    t.checkExpect(s2Node3.next, s2Node4);
    t.checkExpect(s2Node3.prev, s2Node2);
    t.checkExpect(s2Node4.next, s2);
  }
  
  void testCount(Tester t) {
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 6);
  }
  
  void testAddAtHead(Tester t) {
    String data = "test";
    String data2 = "test2";
    String data3 = "test3";
    Deque<String> dq1 = new Deque<String>();
    dq1.addAtHead(data);
    dq1.addAtHead(data2);
    dq1.addAtHead(data3);
    ANode<String> dq1FirstNode = dq1.header.next;
    ANode<String> dq1LastNode = dq1.header.prev;
    ANode<String> dq1MidNode = dq1.header.next.next;

    t.checkExpect(((Node<String>) dq1FirstNode).data, data3);
    t.checkExpect(((Node<String>) dq1MidNode).data, data2);
    t.checkExpect(((Node<String>) dq1LastNode).data, data);
  }
  
  void testAddAtTail(Tester t) {
    String data = "test";
    String data2 = "test2";
    String data3 = "test3";
    Deque<String> dq1 = new Deque<String>();
    dq1.addAtTail(data);
    dq1.addAtTail(data2);
    dq1.addAtTail(data3);
    ANode<String> dq1FirstNode = dq1.header.next;
    ANode<String> dq1LastNode = dq1.header.prev;
    ANode<String> dq1MidNode = dq1.header.next.next;

    t.checkExpect(((Node<String>) dq1FirstNode).data, data);
    t.checkExpect(((Node<String>) dq1MidNode).data, data2);
    t.checkExpect(((Node<String>) dq1LastNode).data, data3);
  }
  
  void testRemoveFromHead(Tester t) {
    String data = "test";
    String data2 = "test2";
    String data3 = "test3";
    Deque<String> dq1 = new Deque<String>();
    dq1.addAtHead(data);
    dq1.addAtHead(data2);
    dq1.addAtHead(data3);
    ANode<String> removed = dq1.removeFromHead();
    t.checkExpect(removed.getData(), data3);
    t.checkExpect(dq1.header.next.getData(), data2);
    t.checkExpect(dq1.header.next.prev, dq1.header);
    
    ANode<String> removed2 = dq1.removeFromHead();
    ANode<String> removed3 = dq1.removeFromHead();
    t.checkExpect(dq1.header.next.getData(), null);
    t.checkExpect(dq1.header.prev.getData(), null);
  }
  
  void testFind(Tester t) {
    class testSearch implements Predicate<String> {
      public boolean test(String t) {
        return t.length() > 5;
      }
    }
    t.checkExpect(deque2.find(new testSearch()), s2);
    t.checkExpect(deque3.find(new testSearch()), s3Node3);
  }
}