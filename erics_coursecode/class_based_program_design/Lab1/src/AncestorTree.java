interface IAncestorTree {
  
}

class Unknown implements IAncestorTree {
  Unknown () {}
}


class PersonWithAncestors implements IAncestorTree {
  Person person;
  IAncestorTree mother;
  IAncestorTree father;
  
  PersonWithAncestors(Person person, IAncestorTree mother, IAncestorTree father) {
    this.person = person;
    this.mother = mother;
    this.father = father;
  }
}

class ExamplesAncestorTree {
  Person tim = new Person("Tim", 23, "Male", new Address("Boston", "MA"));
  Person kate = new Person("Kate", 22, "Female", new Address("Warwick", "RI"));
  Person rebecca = new Person("Rebecca", 31, "Non-binary", new Address("Nashua", "NH"));
  Person john = new Person("John",38, "Male", new Address("Minneapolis", "MN"));
  Person alex = new Person("Alex", 35, "Male", new Address("Chattanooga","TN"));

  PersonWithAncestors timTree = new PersonWithAncestors(tim, new Unknown(), new Unknown());
  PersonWithAncestors rebTree = new PersonWithAncestors(rebecca, new Unknown(), new Unknown());
  PersonWithAncestors johnTree = new PersonWithAncestors(john, timTree, rebTree);
  PersonWithAncestors kateTree = new PersonWithAncestors(kate, new Unknown(), new Unknown());
  PersonWithAncestors alexTree = new PersonWithAncestors(alex, johnTree,kateTree);
}