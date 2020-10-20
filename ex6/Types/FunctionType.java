package oop.ex6.main.Types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * represents a function that is declared in the global scope. a function has parameters it expects to receive,
 * and can be called from (almost) anywhere in the program
 */
public class FunctionType {

    // an ordered set containing the function's parameters
    private LinkedHashMap<String, JavaType> parameters = new LinkedHashMap<>();

    /**
     * Class constructor. receives an Ordered set of JavaType variables and assigns them to the function
     * @param functionParameters the parameters to be assigned
     */
    public FunctionType(LinkedHashMap<String, JavaType> functionParameters) {
        parameters.putAll(functionParameters);
    }

    /**
     *
     * @return the function's parameters
     */
    public LinkedHashMap<String, JavaType> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        String str = "";
        for (String key : parameters.keySet()) {
            str += parameters.get(key).toString();
            str += "\n";
            str += "also, its name is: " + key;
            str += "\n";
        }
        return str;
    }

    /**
     * receives a list that holds types of variables that are tried to be inserted via a function call to the method.
     * The function checks if the types are compatible, IN ORDER, to the function's expected parameter types
     * (for instance, if the function expects to receive a double and a char, and it receives a list that has "int" and
     * then "char" the return value will be true).
     * @param other the list of types to be checked
     * @return true if the list of types are acceptable as input parameters for the function, and false otherwise
     */
    public boolean sameSignature(ArrayList<String> other) {
        //first off, we'll check if the parameters were ok
        if (other == null)
            return false;
        //first, let's check that if the are both just no param functions, we'll automatically return true
        if (other.size() == 0 && parameters.size() == 0)
            return true;
        //now, if they are of not the same size, we'll return false, as they can't have the same signature
        if (other.size() != parameters.size())
            return false;
        //now we know that they are of the same size, we'll go over them both to see if the signature of the types the
        //function receives is the same.
        Iterator<JavaType> first = parameters.values().iterator();
        Iterator<String> second = other.iterator();
        while (second.hasNext()) {
            JavaType var = first.next();
            if (!JavaType.contains(JavaType.compatibleTypes.get(var.getType()), second.next()))
                return false;
        }
        return true;
    }

}
