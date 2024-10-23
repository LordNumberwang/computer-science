import java.util.List;
import tester.*;

/*
    It should always be the case that any Student who is enrolled in a Course should appear 
    in the list of Students for that Course, 
    and the Course should likewise appear in the Student’s list of Courses.

    It should always be the case that the Instructor for any Course 
    should have that Course appear in the Instructor’s list of Courses.
 */

class Course {
  String name;
  Instructor prof;
  List<Student> students;
  
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = null;
    prof.courses.add(this);
  }
  
  void addStudent(Student s) {
    students.add(s);
  }
}

class Instructor {
  String name;
  List<Course> courses;
  
  Instructor(String name) {
    this.name = name;
    this.courses = null;
  }
}

class Student {
  String name;
  int id;
  List<Course> courses;
  
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = null;
  }
  
  void enroll(Course c) {
    this.courses.add(c);
    c.addStudent(this);
  }
  
  //Design a method boolean classmates(Student c) 
  //that determines whether the given Student is in any of the same classes as this Student.
  boolean classmates(Student c) {
    //skipping this and the rest of Assignment7
    return true;
  }
}

class ExamplesRegistrar {
  Instructor prof1 = new Instructor("Dr. Margulies");
  Instructor prof2 = new Instructor("Dr. Pietrovito");
  Instructor prof3 = new Instructor("Dr. Leidy");
  Student rob = new Student("Rob", 1);
  Student liz = new Student("Liz", 2);
  Student alex = new Student("Alex", 3);
  
  void init() {
    Course Bme100 = new Course("Bme 100", prof1);
    Course Chem101 = new Course("Chem 101",prof2);
    Course Chem102 = new Course("Chem 102",prof2);
    Course Math104 = new Course("Math 104",prof3);
    rob.enroll(Chem101);
    rob.enroll(Bme100);
    rob.enroll(Chem102);
    liz.enroll(Chem101);
    liz.enroll(Math104);
    alex.enroll(Math104);
  }
  
  void testAll(Tester t){
    init();
    //Design a method boolean classmates(Student c) 
    //that determines whether the given Student is in any of the same classes as this Student.
  }
  
}