package oop.ex6.main.Regex;

import oop.ex6.main.Types.FunctionType;
import oop.ex6.main.Types.JavaType;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * The class instances validate the syntax of inner scope variables and conditional statements.
 */


public class SecondRunnerValidator {
    /* The input String to check*/
    private String inputString;
    /* An ArrayList of variables from all scopes until current point*/
    public ArrayList<LinkedHashMap<String, JavaType>> variableSet;
    /* Dictionary of methods*/
    public TreeMap<String, FunctionType> functionSet;
    /* current scope number */
    public int scope;
    /* Matcher object for Pattern of general boolean statement */
    public static Matcher generalConditionMatcher;
    /* Regex Pattern for general condition statement */
    public static final Pattern generalCondition = Pattern.compile("[ \\t\\r]*(if|while)[ \\t\\r]*" +
            "(\\()[ \\t\\r]*((.)*)[ \\t\\r]*(\\))[ \\t\\r]*(\\{)[ \\t\\r]*");
    /* Regex Pattern for boolean reserved word - "true"/ "false" */
    public static final Pattern booleanReservedWord = Pattern.compile("[ \\t\\r]*(true|false)[ \\t\\r]*");
    /* Regex Pattern for boolean variable */
    public static final Pattern booleanVar = Pattern.compile("[ \\t\\r]*((.)*)[ \\t\\r]*[ " +
            "\\t\\r]*");
    /* Regex Pattern for method called in inner scope */
    public static final Pattern methodCallRegex = Pattern.compile(
            "[ \\t\\r]*([A-Za-z]+[A-Za-z_0-9]*)[ \\t\\r]*(\\()[ \\t\\r]*((.)*)[ \\t\\r]*(\\))[ \\t\\r]*;[ \\t\\r]*");

    /**
     * The class constructor.
     * @param input String input to validate.
     * @param varSetInput ArrayList of dictionaries with variables of all scopes
     * @param funcSetInput Dictionary of all methods in global scope
     * @param scopeInput current scope
     */
    public SecondRunnerValidator(String input, ArrayList<LinkedHashMap<String, JavaType>> varSetInput, TreeMap<String,
            FunctionType> funcSetInput, int scopeInput) {
        this.inputString = input;
        this.variableSet = varSetInput;
        this.functionSet = funcSetInput;
        this.scope = scopeInput;
        this.generalConditionMatcher = this.generalCondition.matcher(inputString);
    }

    /**
     * This method updates the String input and also the updates scope and updated ArrayList of all scopes
     * @param input new String to check
     * @param scopeInput updates scope
     * @param newVarDict updates dictionary with variables from all scopes
     */
    public void update(String input, int scopeInput, ArrayList<LinkedHashMap<String, JavaType>> newVarDict) {
        scope = scopeInput;
        inputString = input;
        this.generalConditionMatcher = this.generalCondition.matcher(inputString);
        this.variableSet = newVarDict;
    }

    /**
     * This method checks if a single boolean variable/ word is valid
     * @param singleBlock String of single condition block
     * @return if syntax valid - returns "true"; otherwise - "false"
     */
    public boolean singleBoolBlockChecker(String singleBlock) {
        Matcher singleReservedWordMatcher = booleanReservedWord.matcher(singleBlock);
        Matcher singleBoolValMatcher = booleanVar.matcher(singleBlock);
        if (singleReservedWordMatcher.matches()) {
            return true;
        }
        if (!singleBoolValMatcher.matches()) {
            return false;
        }
        String boolVar = singleBlock.trim();
        int scopeOfRelevantVar = wasBoolDeclared(boolVar);
        if (scopeOfRelevantVar != -1) { // it is a variable
            String boolVarType = variableSet.get(scopeOfRelevantVar).get(boolVar).getType();
            boolean wasInit = variableSet.get(scopeOfRelevantVar).get(boolVar).isInitialized();
            if (JavaType.contains(JavaType.compatibleTypes.get("boolean"), boolVarType)&&wasInit) {
                return true;
            }
            return false;
        }
        if (JavaType.isDouble(boolVar) || JavaType.isInt(boolVar)) {
            return true;
        }
        return false;
    }

    /**
     * This method receives a variable name and checks if it was declared in current or previous scopes
     * @param varName String representing variable name
     * @return number of innermost scope with a name matching input variable's name
     */
    private int wasBoolDeclared(String varName) {
        for (int i = scope; i >= 0; i--) {
            if (variableSet.get(i).containsKey(varName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method checks if all boolean syntax is valid, both for single unit variables and multiple
     * boolean variables.
     * @return "true" if all is valid syntactically; Otherwise - "false".
     */
    public boolean checkBooleanSyntax() { // check general syntax structure
        if (!generalValidityChecker()) {
            return false;
        }
        String boolCondInput = this.generalConditionMatcher.group(3);
        if (!boolCondInput.contains("||") && !boolCondInput.contains("&&")) { // no commas
            return singleBoolBlockChecker(boolCondInput);
        }
        ArrayList<String> condBlockArray = combinedConditionBlocks(boolCondInput);
        for (String conditionBlock : condBlockArray) {
            if (singleBoolBlockChecker(conditionBlock)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * This method receives a single String with "OR" and "AND" boolean operators, and returns an ArrayList
     * of all sub-variables between the operators.
     * @param fullString String with multiple boolean variables and keywords
     * @return ArrayList of all variables separated
     */
    private ArrayList<String> combinedConditionBlocks(String fullString) {
        String[] orOpStrings = fullString.split("\\|\\|");
        ArrayList<String> allConditions = new ArrayList<>();
        for (String str : orOpStrings) {
            if (!str.contains("&&")) {
                allConditions.add(str);
                continue;
            }
            String[] andConds = str.split("(&&)");
            for (String localString : andConds) {
                allConditions.add(localString);
            }
        }
        return allConditions;
    }


    /**
     * A switchboard, checks to see what sort of line was received with the regex filter, and then handles
     * each case by directing to a different function.
     * @return "true" if input String matches general structure Pattern; otherwise - "false"
     */
    public boolean generalValidityChecker() {
        return generalConditionMatcher.matches();
    }

    public boolean getParamsandCheck(String funcName, List<String> funcParamNames) {
        if (!functionSet.containsKey(funcName))
            return false;
        ArrayList<String> paramTypes = getTypes(funcParamNames);
        if (paramTypes == null)
            return false;
        return functionSet.get(funcName).sameSignature(paramTypes);
    }

    /**
     * This method receives an input String indicating a method signature and returns an ArrayList of
     * String representing the method parameters, with no commas.
     * @return ArrayList representing the method parameters input.
     */
    public ArrayList<String> getCalledMeth() {
        Matcher methodCallMatcher = methodCallRegex.matcher(inputString);
        ArrayList<String> calledMethodParams = new ArrayList<>();
        methodCallMatcher.matches();
        String methName = methodCallMatcher.group(1);
        calledMethodParams.add(methName);
        String params = methodCallMatcher.group(3);
        if (!params.contains(",")) {
            String spacelessParam = params.trim();
            if (spacelessParam.matches("[ \\t\\r]*")) {
                calledMethodParams.add(null);
                return calledMethodParams;
            }
            calledMethodParams.add(spacelessParam);
            return calledMethodParams;
        }
        String[] splitParams = params.split(",");
        for (String singleParam : splitParams) {
            String spacelessParam = singleParam.trim();
            calledMethodParams.add(spacelessParam);
        }
        return calledMethodParams;
    }

    /**
     * This method checks if the String representing a method called is valid syntactically.
     * @return "true" if syntactically valid; otherwise - "false"
     */
    public boolean checkMethodCallSyntax() {
        Matcher methodCallMatcher = methodCallRegex.matcher(inputString);
        return methodCallMatcher.matches();
    }

    /**
     * This method receives a List of Strings representing data of parameters, and returns an ArrayList of
     * Strings indicating the types if the data held by the parameter.
     * @param funcLiterals List of Strings indicating parameter names
     * @return ArrayList<String> of Strings indicating types of data held by the String input
     */
    private ArrayList<String> getTypes(List<String> funcLiterals) {
        ArrayList<String> types = new ArrayList<>();
        if (funcLiterals.get(0) == null)
            return types;
        for (String funcParameter : funcLiterals) {
            String type = JavaType.returnType(funcParameter);
            if (type != null) {
                types.add(type);
            } else {
                type = findTypeofParam(funcParameter);
                if (type == null)
                    return null;
                types.add(type);
            }
        }
        return types;
    }

    /**
     * This method receives a String indicating a variable and returns a String representing the variable
     * Type.
     * @param name String - variable name
     * @return String indicating the variable type
     */
    private String findTypeofParam(String name) {
        for (int i = scope; i >= 0; i--) {
            if (variableSet.get(i).containsKey(name)&&variableSet.get(i).get(name).isInitialized())
                return variableSet.get(i).get(name).getType();
        }
        return null;
    }


}
