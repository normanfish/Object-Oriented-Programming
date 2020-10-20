package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when a variable line includes more
 * than one "equals" "=" sign.
 */
public class MoreThanOneEqualsException extends ValidatorException  {
    public MoreThanOneEqualsException() {
        super("Error: More than one \"=\" sign detected");
    }
}
