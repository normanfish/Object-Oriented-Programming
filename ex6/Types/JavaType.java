package oop.ex6.main.Types;

import oop.ex6.main.Regex.EmptyFinalDeclarationException;
import oop.ex6.main.Regex.FinalAssignmentException;

import java.util.TreeMap;

/**
 * represents any of the 5 basic java types (int, double,char,string,bool).
 * each type can be assigned a value from it's type only.
 */
public class JavaType {


    /*the type name*/
    private String type;
    /* the value's data*/
    private String data;
    /* a boolean variable that keeps track of if the variable was declared from a method line or from any scope*/
    private boolean fromParamList = false;
    /*the scope from which the type came from (1 is global)*/
    private int scope;
    /*true if the variable was initialized with an initial value, and false otherwise.*/
    private boolean wasInitialized;
    /* true if the variable was initialized with a "final" declaration or not*/
    private boolean isFinal;
    /* a dictionary holding the types that one can assign to each type*/
    public static TreeMap<String, String[]> compatibleTypes = initializeTypes();

    /**
     * initializes the type compatibility dictionary
     *
     * @return a tree map containing the type compatibilities
     */
    public static TreeMap<String, String[]> initializeTypes() {
        TreeMap<String, String[]> temp = new TreeMap<>();
        temp.put("int", new String[]{"int"});
        temp.put("double", new String[]{"int", "double"});
        temp.put("char", new String[]{"char"});
        temp.put("boolean", new String[]{"int", "double", "boolean"});
        temp.put("String", new String[]{"String"});
        return temp;
    }

    /**
     * the default constructor. Used in case of simple declaration (int a;)
     *
     * @param typeName    the type which was declared
     * @param whatScope   the scope from which it came
     * @param isParameter true if the variable was declared from a function declaration line, and false other
     */
    public JavaType(String typeName, int whatScope, boolean isParameter) {
        type = typeName;
        scope = whatScope;
        wasInitialized = false;
        isFinal = false;
        data = null;
        fromParamList = isParameter;
    }

    /**
     * @return the type of the variable
     */
    public String getType() {
        return type;
    }

    /**
     * constructor with a value that is not another variable. This constructor is to be used in one of the following
     * 1. a value was declared with the "final" keyword, in the global scope (i.e. not in a funciton)
     * in this case it MUST be initialized (the function checks for it).
     * 2. a value was declared and initialized (int a=5);
     *
     * @param whatScope     the scope from which it came
     * @param typeName      the type of variable
     * @param initialValue  the initial value, could be null
     * @param initWithFinal true if the variable was declared with the "final" prefix, false otherwise.
     * @throws EmptyFinalDeclarationException in case we tried to declare and empty final variable
     * @throws ClassCastException             in case of casting an improper type to a variable
     */
    public JavaType(String typeName, String initialValue, boolean initWithFinal, int whatScope)
            throws EmptyFinalDeclarationException, ClassCastException {
        //check if the value was declared with "final" and not initialized
        if (initialValue == null && initWithFinal) {
            throw new EmptyFinalDeclarationException();

        }
        scope = whatScope;
        type = typeName;
        isFinal = initWithFinal;
        //tries to assign the initial value to the variable. might throw classcast exception
        assign(initialValue);
    }

    /**
     * constructor with a value that is another variable. This constructor is to be used in one of the following
     * 1. a value was declared with the "final" keyword. in this case it MUST be initialized (the function checks for it).
     * 2. a value was declared and initialized (int a=b);
     *
     * @param whatScope     the scope from which it came
     * @param typeName      the type of variable
     * @param other         the other variable, from which we will assign the value
     * @param initWithFinal true if the variable was declared with the "final" prefix, false otherwise.
     * @throws EmptyFinalDeclarationException in case we tried to declare and empty final variable
     * @throws ClassCastException             in case of casting an improper type to a variable
     */
    public JavaType(String typeName, JavaType other, boolean initWithFinal, int whatScope) throws EmptyFinalDeclarationException,
            ClassCastException {
        //first, we'll check if the value was declared with "final" and the other variable is not initialized
        if (!other.isInitialized() && initWithFinal) {
            throw new EmptyFinalDeclarationException();
        }
        scope = whatScope;
        type = typeName;
        isFinal = initWithFinal;
        //tries to assign the initial value from the other parameter to the variable. might throw classcast exception
        if (!other.fromParamList)
            assign(other.getData());
        else {
            data = null;
            wasInitialized = true;
        }
    }

    /**
     * a constructor to be used when making a javatype object from a function parameter line. This case is special,
     * as we can allow final uninitialized variables.
     *
     * @param whatScope     the scope from which it came
     * @param typeName      the type of variable
     * @param initWithFinal true if the variable was declared with the "final" prefix, false otherwise.
     * @throws EmptyFinalDeclarationException in case we tried to declare and empty final variable
     * @throws ClassCastException             in case of casting an improper type to a variable
     */
    public JavaType(String typeName, boolean initWithFinal, int whatScope) throws EmptyFinalDeclarationException, ClassCastException {
        scope = whatScope;
        type = typeName;
        isFinal = initWithFinal;
        data = null;
        wasInitialized = true;
        fromParamList = true;
    }

    /**
     * assigns a new value to the variable
     *
     * @param val the value to be assigned
     * @throws ClassCastException       if the value can't be assigned to the variable
     * @throws FinalAssignmentException if the variable was declared as a "final
     */
    public void update(String val) throws ClassCastException, FinalAssignmentException {
        //first off, we'll check if we're trying to assign a value to a final variable
        if (isFinal)
            throw new FinalAssignmentException();
        //next, we'll try to assign the value in the "assign" function, which throws its own errors
        assign(val);
    }


    /**
     * assigns a new value to the variable from another variable
     *
     * @param other the other variable, from which we will assign the value
     * @throws ClassCastException       if the value can't be assigned to the variable
     * @throws FinalAssignmentException if the variable was declared as a "final
     */
    public void update(JavaType other) throws ClassCastException, FinalAssignmentException {
        //check if we're trying to assign a value to a final variable
        if (isFinal || !other.wasInitialized)
            throw new FinalAssignmentException();
        //try to assign the value in the "assign" function, which throws its own errors
        assign(other.getData());
    }

    /**
     * receives a string representing a value to enter the variable. if the value is the wrong type, then the function
     * throws an error, and if the type matches, then updates the data of the variable.
     *
     * @param val the string representing the value to be entered to the variable
     * @throws ClassCastException in case the types don't match
     */
    private void assign(String val) throws ClassCastException {
        if (!areComparable(val)) {
            throw new ClassCastException();
        }
        data = val;
        wasInitialized = true;
    }

    /**
     * returns whether two types are "compatible", as defined in the excersize
     *
     * @param newVal a string representing a new value
     * @return true if newVal can be assigned and compared to the variable, and false otherwise
     */
    private boolean areComparable(String newVal) {
        if (returnType(newVal) == null)
            return false;
        return contains(compatibleTypes.get(type), returnType(newVal));
    }

    /**
     * returns the type of a string
     *
     * @param val the string
     * @return which type of java the string represents
     */
    public static String returnType(String val) {
        if (val != null) {
            if (isInt(val))
                return "int";
            if (isDouble(val))
                return "double";
            if (isBoolean(val))
                return "boolean";
            if (isChar(val))
                return "char";
            if (isString(val))
                return "String";
        }
        return null;
    }

    /**
     * checks if the string holds and int.
     *
     * @param val the string to check
     * @return true if the string holds an int type, and false otherwise.
     */
    public static boolean isInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks if the string holds and double.
     *
     * @param val the string to check
     * @return true if the string holds an double type, and false otherwise.
     */
    public static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return (!val.endsWith("f") && !val.endsWith("D"));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks if the string holds a boolean.
     *
     * @param val the string to check
     * @return true if the string holds a boolean type, and false otherwise.
     */
    private static boolean isBoolean(String val) {
        return (val.equals("true") || val.equals("false"));
    }

    /**
     * checks if the string holds a char type.
     *
     * @param val the string to check
     * @return true if the string holds a char type, and false otherwise.
     */
    private static boolean isChar(String val) {
        final String regexNotEmpty = "'.'";
        final String regexEmpty = "''";
        return val.matches(regexNotEmpty) || val.matches(regexEmpty);
    }

    /**
     * checks if the string holds a string type(double "" at the beginning and end).
     *
     * @param val the string to check
     * @return true if the string holds a string type, and false otherwise.
     */
    private static boolean isString(String val) {
        final String regex = "\".*\"";
        return val.matches(regex);
    }

    /**
     * a static method that checks whether a string appears in an array of strings
     *
     * @param array the array of strings
     * @param val   the string we want to search for
     * @return true if var appears in array at least once, false otherwise
     */
    public static boolean contains(String[] array, String val) {
        for (String word : array) {
            if (word.equals(val))
                return true;
        }
        return false;
    }

    /**
     * @return the scope from which the variable was declared
     */
    public int getScope() {
        return scope;
    }

    /**
     * @return the data currently held by the variable
     */
    private String getData() {
        return data;
    }

    /**
     * @return true if the variable was initialized somewhere, and false otherwise.
     */
    public boolean isInitialized() {
        return wasInitialized;
    }


    public String toString() {
        return "This is a variable of type: " + type + " and it currently has the value: " + data + ".\n" +
                "This is a final variable -> " + isFinal + " and it came from scope: " + scope + " " + isInitialized();
    }
}
