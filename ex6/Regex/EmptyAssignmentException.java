package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when there is an attempt to commit
 * an empty assignment.
 */
public class EmptyAssignmentException extends ValidatorException  {
    public EmptyAssignmentException() {
        super("Error: no value assigned variable");
    }
}
