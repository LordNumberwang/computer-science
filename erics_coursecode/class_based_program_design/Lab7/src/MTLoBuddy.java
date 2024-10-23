
// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
    MTLoBuddy() {}
    
    public boolean hasBuddy(Person that) {
      return false;
    }
    
    public int countCommonBuddies(ILoBuddy buddyList) {
      return 0;
    }
    public boolean hasIndirectBuddy(Person that) {
      return false;
    }

    public ILoBuddy addToParty(Person p) {
      return new ConsLoBuddy(p, this);
    }

    public ILoBuddy addToParty(ILoBuddy party) {
      return party;
    }

    public ILoBuddy buildParty(ILoBuddy buddyList) {
      return buddyList;
    }
    
    public int count() {
      return 0;
    }
}
