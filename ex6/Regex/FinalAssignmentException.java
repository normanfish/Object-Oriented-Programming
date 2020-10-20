package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when there is an attempt to assign
 * a value to a variable, while using the "final" modifier.
 */
public class FinalAssignmentException extends ValidatorException  {
    public FinalAssignmentException() {
        super("Error: tried to assign a value to a \"final\" variable");
    }
}
