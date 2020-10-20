package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when a method does not include a
 * "return" statement in its inner scope.
 */
public class NoReturnValueException extends ValidatorException  {
    public NoReturnValueException() {
        super("Error: no return line for function!");
    }
}
