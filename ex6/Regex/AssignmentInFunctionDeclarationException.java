package oop.ex6.main.Regex;

/**
 * A sub-class of the ValidatorException class. This Exception is thrown when a variable is attempted to be
 * assigned in a function declaration line.
 */
public class AssignmentInFunctionDeclarationException extends ValidatorException {
    public AssignmentInFunctionDeclarationException() {
        super("Error: Tried assigning a variable  ( used a \"=\" sign)  in a  function declaration line");
    }
}
