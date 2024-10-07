import tester.*;

class Person {
  int age;
  String name;
  String gender;
  Address address;
  //Really I'd want enum ('male', 'female', 'non-binary')
  
  Person(String name, int age, String gender, Address address) {
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.address = address;
  }
}

class Address {
  String city;
  String state; //really want an enum instead for all 50 abbreviations

  Address(String city, String state) {
    this.city = city;
    this.state = state;
  }
}

class ExamplesPerson {
  Person tim = new Person("Tim", 23, "Male", new Address("Boston", "MA"));
  Person kate = new Person("Kate", 22, "Female", new Address("Warwick", "RI"));
  Person rebecca = new Person("Rebecca", 31, "Non-binary", new Address("Nashua", "NH"));
  Person john = new Person("John",38, "Male", new Address("Minneapolis", "MN"));
  Person alex = new Person("Alex", 35, "Male", new Address("Chattanooga", "TN"));

}

