package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when the syntax is invalid.
 */
public class SyntaxException extends ValidatorException  {
    public SyntaxException(String line) {
        super("bad syntax in line"+line);
    }
}
