package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when there is a "final" modifier
 * before a variable name, but not a valid declaration.
 */
public class EmptyFinalDeclarationException extends ValidatorException  {
    public EmptyFinalDeclarationException() {
        super("Error: no value assigned to a \"final\" variable");
    }
}
