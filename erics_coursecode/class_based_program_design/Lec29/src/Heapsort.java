import java.util.*;
import tester.*;

class Heap {
  ArrayList<Integer> heapList;
  
  Heap(ArrayList<Integer> unsorted) {
    heapList = unsorted;
    sortheap();
  }
  
  public void buildHeap() {
    for (int i=(heapList.size()/2)-1; i >= 0; i--) {
      downheap(i,heapList.size());
    }
  }
  
  public void swap(int idx1, int idx2) {
    int temp = heapList.get(idx1);
    heapList.set(idx1, heapList.get(idx2));
    heapList.set(idx2, temp);
  }
  
  public void upheap(int idx) {
    int ancIdx = ((idx-1) / 2);
    if (heapList.get(idx) > heapList.get(ancIdx)) {
      swap(idx, ancIdx);
      upheap(ancIdx);      
    }
  }

  public void downheap(int idx, int n) {
    int lIdx = 2 * idx + 1;
    int rIdx = 2 * idx + 2;
    int bigIdx = idx;
    
    if (lIdx < n && heapList.get(lIdx) > heapList.get(idx)) {
      bigIdx = lIdx;
    }
    
    if (rIdx < n && heapList.get(rIdx) > heapList.get(bigIdx)) {
      bigIdx= rIdx;
    }
    if (bigIdx != idx) {
      swap(idx,bigIdx);
      downheap(bigIdx, n);
    }
  }
  
  public void removeMax(int n) {
    swap(0,n-1);
    downheap(0,n-1);
  }
  
  public void sortheap() {
    buildHeap();
    for (int n=heapList.size(); n>1; n--) {
      removeMax(n);
    }
  }
}


class ExamplesHeapsort{
  ArrayList<Integer> test1 = new ArrayList<Integer>();
  
  void init() {
    test1.add(4); test1.add(17); test1.add(7); test1.add(12);
    test1.add(15); test1.add(5); test1.add(79); test1.add(7);
  }
  
  void testSwap(Tester t) {
//    init();
//    Heap heap1 = new Heap(test1);
//    int oldval = heap1.heapList.get(1);
//    int oldval2 = heap1.heapList.get(6);
//    
//    t.checkExpect(heap1.heapList.get(1), 17);
//    t.checkExpect(heap1.heapList.get(6), 79);
//
//    heap1.swap(1,6);
//    t.checkExpect(heap1.heapList.get(1), 79);
//    t.checkExpect(heap1.heapList.get(6), 17);
  }
  
  /*
   *         Sorted:
   *         79
   *      15    17
   *   12   7   5  7  
   *  4
   */
  
  void testUpheap(Tester t) {
//    init();
//    Heap heap1 = new Heap(test1);
//    t.checkExpect(heap1.heapList.toString(),"[4, 17, 7, 12, 15, 5, 79, 7]");
//    heap1.upheap(6);
//    t.checkExpect(heap1.heapList.toString(),"[79, 17, 4, 12, 15, 5, 7, 7]");
  }

  void testDownheap(Tester t) {
//    init();
//    Heap heap1 = new Heap(test1);
//    t.checkExpect(heap1.heapList.toString(),"[4, 17, 7, 12, 15, 5, 79, 7]");
//    heap1.downheap(0);
//    t.checkExpect(heap1.heapList.toString(),"[17, 15, 7, 12, 4, 5, 79, 7]");
  }
  
  void testSort(Tester t) {
    init();
    Heap sorted = new Heap(test1);
    t.checkExpect(sorted.heapList.toString(), "[4, 5, 7, 7, 12, 15, 17, 79]");
  }
  
}