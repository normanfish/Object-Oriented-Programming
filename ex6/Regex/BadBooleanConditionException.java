package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when a condition block parameter
 * is invalid.
 */
public class BadBooleanConditionException extends ValidatorException  {
    public BadBooleanConditionException() {
        super("Error: not a boolean condition in if/while block");
    }
}
