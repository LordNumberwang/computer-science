// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

    Person first;
    ILoBuddy rest;

    ConsLoBuddy(Person first, ILoBuddy rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean hasBuddy(Person aBuddy) {
      if (first == aBuddy) {
        return true;
      } else {
        return rest.hasBuddy(aBuddy);
      }
    }
    
    public int countCommonBuddies(ILoBuddy buddyList) {
      int cnt = this.rest.countCommonBuddies(buddyList);
      if (buddyList.hasBuddy(this.first)) {
        return 1 + cnt;
      } else {
        return cnt;
      }
    }
    
    public ILoBuddy addToParty(Person p) {
      if (this.hasBuddy(p)) {
        return this;
      } else {
        return new ConsLoBuddy(p, this);
      }
    }
    
    public ILoBuddy addToParty(ILoBuddy party) {
      return this.rest.addToParty(party.addToParty(this.first));
    }

    public ILoBuddy buildParty(ILoBuddy party) {
      ILoBuddy worklist = this.rest;
      //first remove from worklist in party.
      if (!party.hasBuddy(this.first)) {
        worklist = worklist.addToParty(this.first.buddies);
        return worklist.buildParty(party.addToParty(first));
      } else {
        return worklist.buildParty(party);
      }
    }

    public int count() {
      return 1 + this.rest.count();
    }
}
