//CS 2510, Assignment 3

import tester.*;

//to represent a list of Strings
interface ILoString {
 // combine all Strings in this list into one
 String combine();
 ILoString insert(String thatString);
 ILoString insert(ILoString sortedStrs);
 ILoString sort();
 boolean isSorted();
 boolean isNextSorted(String lastStr);
 ILoString interleave(ILoString otherStrings);
 ILoString merge(ILoString otherStrings);
 ILoString reverse();
 ILoString revHelper(ILoString revList);
 boolean isDoubledList();
 boolean isDoubledHelper(String lastString);
 boolean isPalindromeList();
}

//to represent an empty list of Strings
class MtLoString implements ILoString {
 MtLoString(){}
 
 // combine all Strings in this list into one
 public String combine() {
    return "";
 }
 
 public ILoString insert(String thatString) {
   return new ConsLoString(thatString,this);
 }
 
 public ILoString insert(ILoString sorted) {
   return sorted;
 }
 
 public ILoString sort() {
   return this;
 }
 
 public boolean isSorted() {
   return true;
 }
 
 public boolean isNextSorted(String lastStr) {
   return true;
 }
 
 public ILoString interleave(ILoString otherStrings) {
   return otherStrings;
 }
 
 public ILoString merge(ILoString otherStrings) {
   return otherStrings;
 }
 
 public ILoString reverse() {
   return this;
 }
 
 public ILoString revHelper(ILoString revList) {
   return revList;
 }
 public boolean isDoubledList() {
   return true;
 }
 public boolean isDoubledHelper(String last) {
   return false;
 }
 public boolean isPalindromeList() {
   return true;
 }
}

//to represent a nonempty list of Strings
class ConsLoString implements ILoString {
 String first;
 ILoString rest;
 
 ConsLoString(String first, ILoString rest){
     this.first = first;
     this.rest = rest;
 }
 
 /*
  METHODS FOR FIELDS
  ... this.first.concat(String) ...        -- String
  ... this.first.compareTo(String) ...     -- int
  ... this.rest.combine() ...              -- String
  ... this.rest.insert(String) ...         -- ILoString
  */
 
 // combine all Strings in this list into one
 public String combine(){
     return this.first.concat(this.rest.combine());
 }
 
 // return new list alphabetically, case insensitive
 public ILoString sort() {
   return this.rest.insert(new ConsLoString(this.first, new MtLoString()));
 }
 
 public ILoString insert(String thatString) {
   if (this.first.toLowerCase().compareTo(thatString.toLowerCase()) > 0) {
     return new ConsLoString(thatString, this);
   } else {
     return new ConsLoString(this.first, this.rest.insert(thatString));
   }
 }
 
 public ILoString insert(ILoString sortedStr) {
   return this.rest.insert(sortedStr.insert(this.first));
 }
 
 public boolean isSorted() {
   // check first with accumulator, pass first to rest.sortedHelper
   return this.isNextSorted(this.first);
 }

 //Is Sorted's helper function using recursive accumulator
 public boolean isNextSorted(String lastStr) {
   if (lastStr.toLowerCase().compareTo(this.first.toLowerCase()) > 0) {
     return false;
   } else {
     return this.rest.isNextSorted(this.first);
   }
 }
 
 public ILoString interleave(ILoString otherStrings) {
   return new ConsLoString(this.first, otherStrings.interleave(this.rest));
   //
 }
 /*
  * Design the method merge that takes this sorted list of Strings and a given sorted list of Strings, 
  * and produces a sorted list of Strings that contains all items in both original lists, including duplicates. 
  */
 public ILoString merge(ILoString otherStrings) {
   //Assume both are sorted. I could explicitly call this with a helper function but this is a long enough assignment.
   return this.insert(otherStrings);
 }
 
 public ILoString reverse() {
   return this.rest.revHelper(new ConsLoString(this.first, new MtLoString()));
 }
 
 public ILoString revHelper(ILoString revList) {
   return this.rest.revHelper(new ConsLoString(this.first, revList));
 }
 
 public boolean isDoubledList() {
   return this.rest.isDoubledHelper(this.first);
 }
 
 public boolean isDoubledHelper(String lastString) {
   if (this.first == lastString) {
     return this.rest.isDoubledList();
   } else {
     return false;
   }
 }

 public boolean isPalindromeList() {
   return this.interleave(this.reverse()).isDoubledList();
 }
}

//to represent examples for lists of strings
class ExamplesStrings{
 
 ILoString mary = new ConsLoString("Mary ",
                 new ConsLoString("had ",
                     new ConsLoString("a ",
                         new ConsLoString("little ",
                             new ConsLoString("lamb.", new MtLoString())))));
 ILoString unsorted = new ConsLoString("BBB",
     new ConsLoString("aAa",
         new ConsLoString("dDD", new MtLoString())));
 ILoString wellSorted = new ConsLoString("aAa",
     new ConsLoString("BBB",
         new ConsLoString("dDD", new MtLoString())));
 
 ILoString mergeOne = new ConsLoString("A", new ConsLoString("B",
         new ConsLoString("C", new ConsLoString("D",
                 new ConsLoString("E", new ConsLoString("F", new MtLoString()))))));
 ILoString mergeTwo = new ConsLoString("D", new ConsLoString("E", 
     new ConsLoString("F", new ConsLoString("G", new MtLoString()))));
 
 ILoString notDoubled = new ConsLoString("A", new ConsLoString("A", new ConsLoString("B", 
     new MtLoString())));
 
 // test the method combine for the lists of Strings
 boolean testCombine(Tester t){
     return 
         t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
 }
 
 boolean testSort(Tester t) {
   return
       t.checkExpect(this.mary.sort(), new ConsLoString("a ",
           new ConsLoString("had ", 
               new ConsLoString("lamb.",
                   new ConsLoString("little ", 
                       new ConsLoString("Mary ", new MtLoString()))))));
 }
 
 //Add test for 
 boolean testIsSorted(Tester t) {
   return
       t.checkExpect(this.mary.isSorted(), false) &&
       t.checkExpect(this.mary.sort().isSorted(), true) &&
       t.checkExpect(unsorted.isSorted(), false) &&
       t.checkExpect(wellSorted.isSorted(), true);
 }
 
 boolean testInterleave(Tester t) {
   return
       t.checkExpect(mary.interleave(unsorted), 
           new ConsLoString("Mary ", new ConsLoString("BBB",
                   new ConsLoString("had ", new ConsLoString("aAa",
                       new ConsLoString("a ", new ConsLoString("dDD",
                           new ConsLoString("little ",
                               new ConsLoString("lamb.", new MtLoString()))))))))) &&
       t.checkExpect(wellSorted.interleave(mary), 
           new ConsLoString("aAa", new ConsLoString("Mary ",
               new ConsLoString("BBB", new ConsLoString("had ",
                   new ConsLoString("dDD", new ConsLoString("a ",
                       new ConsLoString("little ",
                           new ConsLoString("lamb.", new MtLoString())))))))));
 }
 
 /*
  * Merge test notes:
  * Can you construct an example of two lists such that interleaving them and merging them produce different results? 
  * Can you construct another example where the two results are the same?)
  * Example: A,B,C,D,E,F with D,E,F,G => (A,D,B,E,C,F,D,G,E,F,empty) vs (A,B,C,D,D,E,E,F,F,G,empty)
  * Same example is just one list interleaved with itself.

  */
 boolean testMerge(Tester t) {
   return
       t.checkExpect(mergeOne.merge(mergeTwo), 
           new ConsLoString("A", new ConsLoString("B", new ConsLoString("C",
               new ConsLoString("D", new ConsLoString("D", 
                   new ConsLoString("E", new ConsLoString("E", 
                       new ConsLoString("F", new ConsLoString("F",
                           new ConsLoString("G", new MtLoString())))))))))));
 }
 
 boolean testReverse(Tester t) {
   return t.checkExpect(mary.reverse(),
           new ConsLoString("lamb.", new ConsLoString("little ", 
               new ConsLoString("a ", new ConsLoString("had ", 
                   new ConsLoString("Mary ", new MtLoString()))))));
 }
 
 boolean testIsDoubledList(Tester t) {
   return t.checkExpect(mary.interleave(mary).isDoubledList(),true) && 
       t.checkExpect(notDoubled.isDoubledList(), false);
 }
 
 boolean testIsPalindromeList(Tester t) {
   return t.checkExpect(new ConsLoString("A", new ConsLoString("B", 
               new ConsLoString("C", new ConsLoString("B", 
                   new ConsLoString("A", new MtLoString()))))).isPalindromeList(), true) &&
       t.checkExpect(new ConsLoString("A", new ConsLoString("B", 
           new ConsLoString("C", new ConsLoString("B", new MtLoString())))).isPalindromeList(), false);
 }
}