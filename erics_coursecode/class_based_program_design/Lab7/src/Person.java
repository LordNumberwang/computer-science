
// represents a Person with a user name and a list of buddies
class Person {

    String username;
    ILoBuddy buddies;

    Person(String username) {
        this.username = username;
        this.buddies = new MTLoBuddy();
    }

    // returns true if this Person has that as a direct buddy
    boolean hasDirectBuddy(Person that) {
      //return person in buddies.hasBuddy(that)?  
      return buddies.hasBuddy(that);
    }

    // returns the number of people who will show up at the party 
    // given by this person
    int partyCount(){
      ILoBuddy partyList = buddies.buildParty(new ConsLoBuddy(this, new MTLoBuddy()));
      return partyList.count();
    }

    // returns the number of people that are direct buddies 
    // of both this and that person
    int countCommonBuddies(Person that) {
      return this.buddies.countCommonBuddies(that.buddies);
    }

    // will the given person be invited to a party 
    // organized by this person?
    boolean hasExtendedBuddy(Person that) {
      ILoBuddy partyList = buddies.buildParty(new ConsLoBuddy(this, new MTLoBuddy()));
      return partyList.hasBuddy(that);
    }

    void addBuddy(Person buddy) {
      if (this.buddies.hasBuddy(buddy)) {
        return; //Handle dupes
      } else {
        this.buddies = new ConsLoBuddy(buddy, buddies);
      }
    }
}
