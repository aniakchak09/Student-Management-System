### Introduction

This project tries to implement a management system for handling students, courses, grades, and
assignments. Most of the work is done by the Secretariat class, which is part of a larger system that manages
undergraduate (Licenta) and master's (Master) students,
along with corresponding courses. This class holds 4 ArrayLists to store the information about the students and courses.
I chose this data structure because it allows fast access to the elements, and it is easy to iterate through.

### Features

#### Student Management

+ Add Students: Enroll students in either the undergraduate or master's program.
+ Duplicate Handling: Prevents the addition of duplicate students and logs errors.

#### Course Management

+ Add Courses: Create and manage undergraduate and master's courses.
+ Assign Students: Automatically assigns students to courses based on preferences and available spots.
+ Grade Assignment: Input and manage student grades, including the ability to contest and update grades.

#### Reporting

+ Generate Reports: Produces reports containing sorted student grades and course-specific student lists.
+ Individual Student Details: Outputs detailed information about a specific student.

### Usage

The main method reads the commands from a file and executes the corresponding method from Secretariat.