package oop.ex6.main.Regex;



/**
 * A sub-class of the ValidatorException class. This Exception is thrown when there is an attempt to
 * initialize a variable which already exists in the same scope.
 */
public class VariableAlreadyExistsException extends ValidatorException  {
    public VariableAlreadyExistsException() {
        super("Error: tried to initialize a basic variable that has already been initialized in this scope");
    }
}
