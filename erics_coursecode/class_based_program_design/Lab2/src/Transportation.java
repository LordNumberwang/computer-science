// Represents a mode of transportation

import tester.Tester;

interface IMOT {
  boolean isMoreFuelEfficientThan(int mpg);
}
 
// Represents a bicycle as a mode of transportation
class Bicycle implements IMOT {
  String brand;
 
  Bicycle(String brand) {
    this.brand = brand;
  }
  
  public boolean isMoreFuelEfficientThan(int mpg) {
    return true;
  }
}
 
// Represents a car as a mode of transportation
class Car implements IMOT {
  String make;
  int mpg; // represents the fuel efficiency in miles per gallon
 
  Car(String make, int mpg) {
    this.make = make;
    this.mpg = mpg;
  }
  
  public boolean isMoreFuelEfficientThan(int mpg) {
    return (this.mpg >= mpg);
  }
}
 
// Keeps track of how a person is transported
class Person {
  String name;
  IMOT mot;
 
  Person(String name, IMOT mot) {
    this.name = name;
    this.mot = mot;
  }
  
  boolean motMeetsFuelEfficiency(int mpg) {
    return this.mot.isMoreFuelEfficientThan(mpg);
  }
}

class ExamplesPerson {
  IMOT diamondback = new Bicycle("Diamondback");
  IMOT toyota = new Car("Toyota", 30);
  IMOT lamborghini = new Car("Lamborghini", 17);
 
  Person bob = new Person("Bob", diamondback);
  Person ben = new Person("Ben", toyota);
  Person becca = new Person("Becca", lamborghini);
  
  boolean testBob(Tester t) {
    return t.checkExpect(bob.name, "Bob") &&
        t.checkExpect(bob.mot, diamondback);
  }
  
  boolean testFuelEfficiency(Tester t) {
    return t.checkExpect(bob.motMeetsFuelEfficiency(20),true) &&
        t.checkExpect(ben.motMeetsFuelEfficiency(20), true) &&
        t.checkExpect(becca.motMeetsFuelEfficiency(20), false);
  }
}