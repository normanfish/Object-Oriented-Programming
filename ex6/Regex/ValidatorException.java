package oop.ex6.main.Regex;


/**
 * The general super-class of 10 different syntax-related Exceptions thrown by the validator. Each
 * sub-class has a unique  message indicating the reason it was thrown.
 */
public class ValidatorException extends Exception {

    ValidatorException(String msg){
        super(msg);
    }

}
