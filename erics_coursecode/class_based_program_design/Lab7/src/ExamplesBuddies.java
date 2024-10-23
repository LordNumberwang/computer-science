import tester.*;


// runs tests for the buddies problem
public class ExamplesBuddies {  
  Person ann = new Person("Ann");
  Person bob = new Person("Bob");
  Person cole = new Person("Cole");
  Person dan = new Person("Dan");
  Person ed = new Person("Ed");
  Person fay = new Person("Fay");
  Person gabi = new Person("Gabi");
  Person hank = new Person("Hank");
  Person jan = new Person("Jan");
  Person kim = new Person("Kim");
  Person len = new Person("Len");

  void initData() {
    ann.addBuddy(bob); ann.addBuddy(cole);
    bob.addBuddy(ann); bob.addBuddy(ed); bob.addBuddy(hank);
    cole.addBuddy(dan);
    dan.addBuddy(cole);
    ed.addBuddy(fay);
    fay.addBuddy(ed); fay.addBuddy(gabi);
    gabi.addBuddy(ed); gabi.addBuddy(fay);
    jan.addBuddy(kim); jan.addBuddy(len);
    kim.addBuddy(jan); kim.addBuddy(len);
    len.addBuddy(jan); len.addBuddy(kim);
  }
  
  void testhasDirectBuddy(Tester t) {
    initData();
    t.checkExpect(ann.hasDirectBuddy(cole), true);
    t.checkExpect(ann.hasDirectBuddy(ed), false);
  }
  
  void testCountDirectBuddy(Tester t) {
    initData();
    t.checkExpect(jan.countCommonBuddies(kim), 1);
    t.checkExpect(ann.countCommonBuddies(hank), 0);
    Person yone = new Person("Yone");
    Person zed = new Person("Zed");
    yone.addBuddy(ann); yone.addBuddy(bob); yone.addBuddy(ed); yone.addBuddy(fay); yone.addBuddy(gabi); yone.addBuddy(kim);
    zed.addBuddy(ann); zed.addBuddy(bob); zed.addBuddy(ed); zed.addBuddy(fay); zed.addBuddy(gabi); zed.addBuddy(jan);
    t.checkExpect(yone.countCommonBuddies(zed), 5);
  }
  
  void testAddParty(Tester t) {
    initData();
    ILoBuddy testParty = ann.buddies.addToParty(ann);
    t.checkExpect(testParty.count(), 3);
    t.checkExpect(testParty.hasBuddy(ann), true);
    t.checkExpect(testParty.hasBuddy(bob), true);
    t.checkExpect(testParty.hasBuddy(cole), true);
  }
  
  void testHasExtendedBuddy(Tester t) {
    initData();
    t.checkExpect(jan.hasExtendedBuddy(ann), false);
    t.checkExpect(jan.hasExtendedBuddy(len), true);
    t.checkExpect(ann.hasExtendedBuddy(hank), true);
    t.checkExpect(ann.hasExtendedBuddy(gabi), true);
    t.checkExpect(ann.hasExtendedBuddy(len), false);
  }
  
  void testPartyCount(Tester t) {
    initData();
    t.checkExpect(jan.partyCount(), 3);
    t.checkExpect(ann.partyCount(), 8);
  }
}