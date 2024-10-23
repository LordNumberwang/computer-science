import tester.*;
import java.util.Comparator;
import java.util.function.*;

abstract class ABST<T> {
  Comparator<T> order;
  
  abstract ABST<T> insert(T other);
  abstract boolean present(T other);
  abstract T getLeftMost();
  abstract ABST<T> getRight();
  public boolean sameLeaf(Leaf<T> otherLeaf) { return false; }
  public boolean sameNode(Node<T> otherNode) { return false; }
  abstract boolean sameTree(ABST<T> otherTree);
  abstract boolean sameData(ABST<T> otherTree);
  abstract IList<T> buildList();
}

class Leaf<T> extends ABST<T> {
  Leaf(Comparator<T> order) {
    this.order = order;
  };
  
  public ABST<T> insert(T other) {
    return new Node<T>(order, other, new Leaf<T>(order), new Leaf<T>(order));
  }
  
  public boolean present(T item) {
    return false;
  }

  public T getLeftMost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }
  
  public boolean sameLeaf(Leaf<T> otherLeaf) { return true; }

  public boolean sameTree(ABST<T> otherTree) {
    return otherTree.sameLeaf(this);
  }

  public boolean sameData(ABST<T> otherTree) {
    return otherTree.sameLeaf(this);
  }

  public IList<T> buildList() {
    return new MtList<T>();
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;
  
  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    this.data = data;
    this.left = left;
    this.right = right;
    this.order = order;
  }
  
  public ABST<T> insert(T other) {
    if (order.compare(other, data) < 0) {
      return new Node<T>(order, data, left.insert(other), right);
    } else {
      return new Node<T>(order, data, left, right.insert(other));
    }
  }
  
  public boolean present(T other) {
    if (order.compare(other, data) == 0) {
      return true;
    } else if (order.compare(other, data) < 0) {
      return left.present(other);
    } else {
      return right.present(other);
    }
  }

  public T getLeftMost() {
    if (left instanceof Leaf) {
      return data;
    } else {
      return left.getLeftMost();
    }
  }

  public ABST<T> getRight() {
    if (left instanceof Leaf) {
      return right;
    } else {
      return new Node<T>(order, data, left.getRight(), right);
    }
  }
  
  public boolean sameNode(Node<T> otherNode) {
    return (this.order.equals(otherNode.order)) && 
        (this.data.equals(otherNode.data)) &&
        (this.left.sameTree(otherNode.left)) &&
        (this.right.sameTree(otherNode.right));
  }

  public boolean sameTree(ABST<T> otherTree) {
    return otherTree.sameNode(this);
  }

  public boolean sameData(ABST<T> otherTree) {
    // done via check lefts most, then with both right trees (remaining)... loop.
    return (!otherTree.sameLeaf(new Leaf<T>(order))) && //protect from Leaf.getLeftMost error
        (this.getLeftMost().equals(otherTree.getLeftMost())) &&
        (this.getRight().sameData(otherTree.getRight()));
  }

  public IList<T> buildList() {
    return new ConsList<T>(getLeftMost(), getRight().buildList());
  }
}

class ExamplesABST {
  Book A = new Book("Design Patterns", "Erich Gamma", 59);
  Book B = new Book("Head First Design Patterns", "Eric Freeman", 44);
  Book C = new Book("Practical Object-Oriented Design", "Sandi Metz", 36);
  Book D = new Book("Classic Computer Science Problems in Java", "David Kopec", 41);
  Book E = new Book("Designing Data-Intensive Applications", "Martin Kleppman", 37);
  Book F = new Book("Grokking Algorithms", "Aditya Bhargava", 39);
  Book G = new Book("Programming Pearls", "Jon Bentley", 45);
  Book H = new Book("Grokking Algorithms", "Plagiarist", 60);
  Book I = new Book("Pride and Prejudice", "Plagiarist", 60);
  // new BooksByTitle() => D,A,E,F,B,C,G
  // new BooksByAuthor() => F,D,B,A,G,E,C
  // new BooksByPrice() => C,E,F,D,B,G,A
  Comparator<Book> order = new BooksByTitle();
  ABST<Book> leaf = new Leaf<Book>(order);
  ABST<Book> validTitleABST = new Node<Book>(order, B, 
      new Node<Book>(order, E, 
          new Node<Book>(order, A, 
              new Node<Book>(order, D, leaf, leaf),
              leaf),
          new Node<Book>(order, F, leaf, leaf)),
      new Node<Book>(order, C,
          leaf,
          new Node<Book>(order, G, leaf, leaf))
      );
  /*
   * Designing my sample BST for books.
   * 
   * By Title
   *    VALID          INVALID
   *      B               E
         / \            /   \
        E   C          A     C
       / \    \       / \   / \
      A   F    G     D   F B   G
     /
    D
   *
   * By Author
   *    VALID          INVALID
   *      G               B
         / \            /   \
        B   A          D     A
       / \    \       / \   / \
      A   E    C     F   E G   C
     /
    F
   *
   */
  //

  ABST<Book> L1 = new Node<Book>(order, B, leaf, leaf);
  ABST<Book> L2 = new Node<Book>(order, B,
      new Node<Book>(order, E, leaf, leaf),
      leaf);
  ABST<Book> LDupe = new Node<Book>(order, B,
      leaf,
      new Node<Book>(order, B, leaf, leaf));
  ABST<Book> L3 = new Node<Book>(order, B,
      leaf,
      new Node<Book>(order, C, leaf, leaf));
  
  boolean testInsert(Tester t) {
    return t.checkExpect(L1.insert(E), L2) &&
        t.checkExpect(L1.insert(B), LDupe) &&
        t.checkExpect(L1.insert(E).insert(C).insert(A).insert(F).insert(G).insert(D), validTitleABST);
  }
  
  boolean testPresent(Tester t) {
    return t.checkExpect(validTitleABST.present(H), true) &&
        t.checkExpect(validTitleABST.present(I), false);
  }
  
  boolean testGetLeftMost(Tester t) {
    return t.checkExpect(validTitleABST.getLeftMost(), D)
        && t.checkExpect(new Node<Book>(order,B, leaf, leaf)
            .getLeftMost(), B)
        && t.checkExpect(new Node<Book>(order,A, leaf, leaf).
            insert(C).insert(B).getLeftMost()
            , A);
  }
  ABST<Book> LG = new Node<Book>(order,G, leaf,leaf);
  ABST<Book> LUpper = new Node<Book>(order, C, L1, LG);
  ABST<Book> LUpper2 = new Node<Book>(order, C, L1, LG);
  
  boolean testGetRight(Tester t) {
    return t.checkExpect(L1.getRight(), leaf)
        && t.checkExpect(L2.getRight(), L1)
        && t.checkExpect(LDupe.getRight(), L1)
        && t.checkExpect(LUpper.getRight(), new Node<Book>(order,C,leaf,LG))
        && t.checkExpect(new Node<Book>(order,C,L2,LG).getRight(), new Node<Book>(order,C,L1,LG))
        && t.checkExpect(new Node<Book>(order,C,L3,LG).getRight(), new Node<Book>(order,C,new Node<Book>(order, C,
            leaf,
            leaf),LG));
  }
  //D,A,E,F,B sorted 5
  ABST<Book> treeD = new Node<Book>(order,D,leaf,leaf);
  ABST<Book> bstA = new Node<Book>(order,E,
      new Node<Book>(order,A,treeD,leaf),
      new Node<Book>(order,F,leaf,leaf));
  ABST<Book> bstB = new Node<Book>(order,E,
      new Node<Book>(order,A,treeD,leaf),
      new Node<Book>(order,F,leaf,leaf));
  ABST<Book> bstC = new Node<Book>(order,A,
      treeD,
      new Node<Book>(order,F,
          new Node<Book>(order,E,leaf,leaf),
          leaf));
  ABST<Book> bstD = new Node<Book>(order,E,
      treeD,
      new Node<Book>(order,F,
          leaf,
          new Node<Book>(order,B,leaf,leaf)));  
  
  boolean testSameTree(Tester t) {
    return t.checkExpect(bstA.sameTree(bstB), true)
        && t.checkExpect(bstA.sameTree(bstC), false)
        && t.checkExpect(bstA.sameTree(bstD), false);
  }
  
  boolean testSameData(Tester t) {
    return t.checkExpect(bstA.sameData(bstB), true)
        && t.checkExpect(bstA.sameData(bstC), true)
        && t.checkExpect(bstA.sameData(bstD), false);
  }

  IList<Book> sortedBookList = new ConsList<Book>(D, 
      new ConsList<Book>(A, new ConsList<Book>(E,
          new ConsList<Book>(F, new ConsList<Book>(B, 
              new ConsList<Book>(C, new ConsList<Book>(G, 
                  new MtList<Book>())))))));
  //Compare with validTitleABST sorted
  
  boolean testBuildList(Tester t) {
    return t.checkExpect(validTitleABST.buildList(), sortedBookList);
  }
}