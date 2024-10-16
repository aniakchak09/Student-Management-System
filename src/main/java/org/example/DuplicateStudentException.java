package org.example;

public class DuplicateStudentException extends Exception {
    public DuplicateStudentException(String message) {
        System.out.println(message);
    }
}
