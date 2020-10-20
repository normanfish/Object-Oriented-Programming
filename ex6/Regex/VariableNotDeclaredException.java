package oop.ex6.main.Regex;


/**
 * A sub-class of the ValidatorException class. This Exception is thrown when there is an attempt to assign
 * a variable that was not previously declared.
 */
public class VariableNotDeclaredException extends ValidatorException {
    public VariableNotDeclaredException() {
        super("Error: a variable that was assigned to that was never declared");
    }
}
