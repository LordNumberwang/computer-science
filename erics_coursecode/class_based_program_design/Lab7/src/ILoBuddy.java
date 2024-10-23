
// represents a list of Person's buddies
interface ILoBuddy {
  //
  boolean hasBuddy(Person that);
  int countCommonBuddies(ILoBuddy buddyList);

  ILoBuddy addToParty(Person p);
  ILoBuddy addToParty(ILoBuddy buddyList);
  ILoBuddy buildParty(ILoBuddy party);
  int count();
}