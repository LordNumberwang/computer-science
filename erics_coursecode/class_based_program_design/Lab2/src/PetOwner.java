import tester.Tester;

// to represent a pet owner
class PetOwner {
  String name;
    IPet pet;
    int age;
 
  PetOwner(String name, IPet pet, int age) {
    this.name = name;
    this.pet = pet;
    this.age = age;
  }
    
  public boolean isOlder(PetOwner other) {
    return (this.age > other.age);
  }
  
  public boolean sameNamePet(String name) {
    return (this.pet.nameMatches(name));
  }
  
  public PetOwner perishPet() {
    return new PetOwner(this.name, new NoPet(),this.age);
  }
}

// to represent a pet
interface IPet { 
  boolean nameMatches(String name);
}
 
// to represent a pet cat
class Cat implements IPet {
    String name;
    String kind;
    boolean longhaired;
 
    Cat(String name, String kind, boolean longhaired) {
        this.name = name;
        this.kind = kind;
        this.longhaired = longhaired;
    }
    public boolean nameMatches(String name) {
      return (this.name == name);
    }
}
 
// to represent a pet dog
class Dog implements IPet {
  String name;
  String kind;
  boolean male;
 
  Dog(String name, String kind, boolean male) {
    this.name = name;
    this.kind = kind;
    this.male = male;
  }
  
  public boolean nameMatches(String name) {
    return (this.name == name);
  }
}

class NoPet implements IPet {
  public boolean nameMatches(String name) {
    return false;
  }
}

class ExamplesPetOwners {
  IPet rossini = new Dog("Rossini","Bichon Frise", true);
  IPet billius = new Cat("Billius", "Tabby", false);
  IPet bayla = new Dog("Bayla","Husky",false);
  IPet mochi = new Cat("Mochi","Void",false);

  PetOwner jojo = new PetOwner("Jojo", rossini, 42);
  PetOwner danielle = new PetOwner("Danielle", billius, 34);
  PetOwner alex = new PetOwner("Alex", bayla, 40);
  PetOwner max = new PetOwner("Max", mochi, 34);
  PetOwner unluckyPerson = jojo.perishPet();
  
  boolean testIsOlder (Tester t) {
    return t.checkExpect(jojo.isOlder(alex), true) &&
        t.checkExpect(max.isOlder(danielle), false);
  }
  
  boolean testSameNamePet (Tester t) {
    return t.checkExpect(danielle.sameNamePet("Billius"), true) &&
        t.checkExpect(alex.sameNamePet("Mochi"), false) &&
        t.checkExpect(max.sameNamePet("Mochi"), true);
  }

  boolean testPerishPet (Tester t) {
    return t.checkExpect(unluckyPerson.pet, new NoPet());
  }
  
}