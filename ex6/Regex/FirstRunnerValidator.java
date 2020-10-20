package oop.ex6.main.Regex;


import oop.ex6.main.Types.JavaType;
import oop.ex6.main.Types.FunctionType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * The class instances validate the syntax of program variables and program methods.
 */

public class FirstRunnerValidator {

    /* The String input received and checked*/
    String inputString;
    /* The dictionary representing variables in the current scope*/
    LinkedHashMap<String, JavaType> variableSet;
    /* An ArrayList representing the variables in all scopes */
    ArrayList<LinkedHashMap<String, JavaType>> fullVariableSet;
    /* A dictionary representing all program methods*/
    TreeMap<String, FunctionType> methodSet;
    /* Stack for counting scopes, by parenthesis*/
    Stack<Integer> parenthasisCounterStack;
    /* boolean flag indicating if we are currently evaluating a function declaration line*/
    boolean isMethod;
    /* int indicating current scope*/
    int scope;
    /* Regex Pattern of variable*/
    public static final Pattern generalStructureRegex = Pattern.compile(
            "[ \\t\\r]*(final?[ \\t\\r]+)?(int|boolean|double|char|String)[ \\t\\r]+((.)+);[ \\t\\r]*");
    /* Matcher object for general variable String*/
    public static Matcher generalStructureMatcher;
    /* Matcher object for variable assignment*/
    public static Matcher privateStructureMatcher;
    /* Matcher object for method signature check*/
    public static Matcher methodGeneralNameMatcher;
    /* Regex Pattern of variable name*/
    public static final Pattern nameRegex = Pattern.compile("[ \\t\\r]*(([A-Za-z]+[A-Za-z_0-9]*)|(_[A-Za-z_0-9]+)|" +
            "(__[A-Za-z_0-9]*))[ \\t\\r]*");
    /* Regex Pattern of assigned variable name*/
    public static final Pattern nameRegexWithValue = Pattern.compile("[ \\t\\r]*(([A-Za-z]+[A-Za-z_0-9]*)|" +
            "(_[A-Za-z_0-9]+)|(__[A-Za-z_0-9]*))[ \\t\\r]*(=)[ \\t\\r]*" + "(.+)[ \\t\\r]*");
    /* Regex Pattern of assigned variable, without type*/
    public static final Pattern subnameRegexWithValue = Pattern.compile("" +
            "([ \\t\\r]*(((\\w)+)[ \\t\\r]*=[ \\t\\r]*((\\w)+)[ \\t\\r]*));[ \\t\\r]*");
    /* Regex Pattern of method signature name*/
    public static final Pattern methodGeneralName = Pattern.compile("[ \\t\\r]*(void)[ \\t\\r]+" +
            "([A-Za-z]+[A-Za-z_0-9]*)[ \\t\\r]*(\\()[ \\t\\r]*((.)*)[ \\t\\r]*(\\))[ \\t\\r]*(\\{)[ \\t\\r]*");
    /* Regex Pattern for String ending with closed curly bracket "}" */
    public static final Pattern endsWithClosedCurlyBrackets = Pattern.compile(".*}[ \\t\\r]*$");
    /* Regex Pattern for String ending with open curly bracket "{" */
    public static final Pattern endsWithOpenCurlyBrackets = Pattern.compile(".*\\{[ \\t\\r]*$");
    /* Regex Pattern for return line */
    public static final Pattern returnLine = Pattern.compile("[ \\t\\r]*(return)[ \\t\\r]*;[ \\t\\r]*$");

    /**
     * class constructor - used to initialize the regexRepository
     *
     * @param input           - the input line
     * @param varSetInput     the LinkedHashSet which contains all of of the variables declared in all scopes
     * @param methodSetInput  the ordered dictionary which contains all of the methods declared in this scope
     * @param counter         a stack that counts the open and closed parenthesis, so we can keep track of the scope we are in
     * @param isParsingMethod a boolean value that holds whether we are looking at a a method declaration line, or a
     *                        simple variable declaration line - this matters because in a function declaration line
     * @param scopeInput      the scope number
     * @param fullValSet      ArrayList with variables of all the scopes
     *
     */

    public FirstRunnerValidator(String input, LinkedHashMap<String, JavaType> varSetInput, TreeMap<String, FunctionType>
            methodSetInput, Stack<Integer> counter, boolean isParsingMethod, int scopeInput, ArrayList<LinkedHashMap<String, JavaType>>
                                        fullValSet) {
        this.inputString = input;
        this.generalStructureMatcher = this.generalStructureRegex.matcher(inputString);
        this.methodGeneralNameMatcher = this.methodGeneralName.matcher(inputString);
        this.privateStructureMatcher = this.subnameRegexWithValue.matcher(inputString);
        this.variableSet = varSetInput;
        this.methodSet = methodSetInput;
        this.scope = scopeInput;
        this.parenthasisCounterStack = counter;
        this.isMethod = isParsingMethod;
        this.fullVariableSet = fullValSet;
    }

    /**
     * This method updates the instance input String, and the Matcher object using the input String.
     * @param newString new String to update
     */
    public void setString(String newString) {
        this.inputString = newString;
        this.generalStructureMatcher = this.generalStructureRegex.matcher(newString);
        this.methodGeneralNameMatcher = this.methodGeneralName.matcher(newString);
        this.privateStructureMatcher = this.subnameRegexWithValue.matcher(newString);
    }

    /**
     * This method updates the filed containing the Stack for counting parenthesis, used to calculate the
     * current scope.
     * @param newParenthesisCounter updated 'Stack' data structure
     */
    public void setParenthasisCounterStack(Stack<Integer> newParenthesisCounter) {
        this.parenthasisCounterStack = newParenthesisCounter;
    }

    /**
     * -This method checks if the current String input matches the Pattern of a method sginature.
     * @return 'true' if String input matches general structure of method signature.
     */
    public boolean checkGeneralMethodName() {
        return methodGeneralNameMatcher.matches(); // true if name matches
    }

    /**
     * This method checks if the Input String matches the pattern of a variable, containing it's primitive
     * type.
     * @return 'true' if String input matches variable pattern containing primitive type
     */
    boolean checkGeneralValidity() { // for variables only!
        return generalStructureMatcher.matches();
    }

    /**
     * This method checks if the Input String matches the pattern of a variable, without an explicit type.
     * @return 'true' if String input matches variable pattern without primitive type
     */
    boolean checkPrivateValidity() { // for variables only!
        return privateStructureMatcher.matches();
    }

    /**
     * This method returns the String representation of a method name, contained in the input String.
     * @return String indicating the method name.
     */
    public String getMehodName() {
        if (!checkGeneralMethodName()) {
            return null;
        }
        String methodName = methodGeneralNameMatcher.group(2);
        return methodName;
    }


    /**
     * This method checks the Syntax of the String input representing a method signature
     * @return boolean 'true' if syntax is valid, otherwise - 'false'
     * @throws EmptyFinalDeclarationException exception thrown when there is an attempt for an empty final declaration
     * @throws VariableAlreadyExistsException exception thrown when variable already exists
     * @throws MoreThanOneEqualsException exception thrown when there is more than one equals
     * @throws AssignmentInFunctionDeclarationException exception thrown when there is an attempt for an assignment
     *                                          inside function parameters
     * @throws EmptyAssignmentException Exception thrown when there is an empty assignment
     * @throws SyntaxException Exception thrown when syntax is invalid
     * @throws ClassCastException Exception thrown when tried to  assign improper value to a class
     */
    public boolean checkMethodSyntax() throws EmptyFinalDeclarationException, VariableAlreadyExistsException,
            MoreThanOneEqualsException, AssignmentInFunctionDeclarationException, EmptyAssignmentException,
            SyntaxException,ClassCastException {
        if (!checkGeneralMethodName()) { // if method name not good
            return false;
        } // if made it here - general signature structure good
        String methodName = methodGeneralNameMatcher.group(2);
        String parameterList = methodGeneralNameMatcher.group(4);
        //check if the method doesn't have any parameters. in this case, we will just add the function's
        //name to the set of functions
        boolean parameterlessMethod = parameterList.matches("[ \\t\\r]*");
        if (parameterlessMethod) {
            this.methodSet.put(methodName, new FunctionType(new LinkedHashMap<>()));
            increaseScope();
            return true;
        }
        //check if the method only has one parameter
        LinkedHashMap<String, JavaType> localVarSet = new LinkedHashMap<>();
        if (!parameterList.contains(",")) { // no comma
            String parameters = parameterList + ";";
            FirstRunnerValidator methodParameterChecker = new FirstRunnerValidator(parameters, localVarSet,
                    null, null, true, 1, fullVariableSet);
            if (methodParameterChecker.checkSyntaxValidity()) {
                this.methodSet.put(methodName, new FunctionType(localVarSet));
                increaseScope();
                return true;
            } else {
                return false;
            }
        } // made it here- contains commas
        LinkedHashMap<String, JavaType> localVars = new LinkedHashMap<>();
        increaseScope();
        FirstRunnerValidator methodParameterChecker = new FirstRunnerValidator(parameterList, localVars, null,
                this.parenthasisCounterStack, true, scope, fullVariableSet);
        String[] comalessParams = parameterList.split(",");
        for (String parSubString : comalessParams) {
            String param = parSubString + ";";
            methodParameterChecker.setString(param);
            if (!methodParameterChecker.checkSyntaxValidity())
                return false;
        }
        this.methodSet.put(methodName, new FunctionType(localVars));
        return true;
    }

    /**
     * Increase the number of the current scope.
     */
    private void increaseScope() {
        parenthasisCounterStack.push(0);
        scope++;
    }

    /**
     * This method returns the flag indicating if we are currently evaluating a function declaration line
     * @return 'true' - if isMethod flag is 'true'; otherwise - 'false'
     */
    public boolean isMethod() {
        return isMethod;
    }

    /**
     * This method updates the isMethod flag
     * @param method the new input for the isMethod flag
     */
    public void setMethod(boolean method) {
        isMethod = method;
    }

    /**
     * This method returns the current scope number
     * @return int variable indicating the current scope
     */
    public int getScope() {
        return scope;
    }

    /**
     * This method updates the field indicating the new scope.
     * @param scope new scope
     */
    public void setScope(int scope) {
        this.scope = scope;
    }


    /**
     * This method receives a single String, with no commas, and with a variable and checks if it is valid
     * ot not. if it is valid - the variable is initialized and updated into the relevant scope dictionary.
     * @param lineName String input
     * @return 'true' if syntax valid; otherwise - 'false'
     * @throws EmptyFinalDeclarationException exception thrown when there is an attempt for an empty final declaration
     * @throws VariableAlreadyExistsException exception thrown when variable already exists
     * @throws MoreThanOneEqualsException exception thrown when there is more than one equals
     * @throws AssignmentInFunctionDeclarationException exception thrown when there is an attempt for an assignment
     *                                          inside function parameters
     * @throws EmptyAssignmentException Exception thrown when there is an empty assignment
     * @throws SyntaxException Exception thrown when syntax is invalid
     * @throws ClassCastException Exception thrown when tried to  assign improper value to a class
     */
    boolean checkSingleBlock(String lineName) throws EmptyFinalDeclarationException, VariableAlreadyExistsException,
            MoreThanOneEqualsException, AssignmentInFunctionDeclarationException, SyntaxException,
            EmptyAssignmentException, ClassCastException {
        Matcher nameMatcher = nameRegex.matcher(lineName);
        Matcher nameMatcherWithValue = nameRegexWithValue.matcher(lineName);
        //case 1: checks if we are assigning a value to a parameter (includes a '=' in a function declaration line)
        if (lineName.contains("=") && isMethod) {
            return false;
        }
        /*
        next, we'll check all of the cases where we don't have a '='. this is a bit complex, as it can be any one of the
        following cases:
        1. This is a simple assignment from the global scale. in this case, we need to check that it doesn't have a
        final prefix, as not assigning a value to a variable with a "final" declaration is illegal. Otherwise it's fine.
        2. This is a a parameter in the function declaration line, where anything goes.
         */
        if (!lineName.contains("=")) {
            if (nameMatcher.matches()) {
                if (whichCondition()) {
                    if (!isMethod) {
                        throw new EmptyFinalDeclarationException();
                    } else {  // the word final exists, but we are in a function declaration line
                        String trimmedName = lineName.trim(); // the actual variable name
                        if ((variableSet.containsKey(trimmedName)) && (variableSet.get(trimmedName).getScope() == scope)) {
                            throw new VariableAlreadyExistsException(); //the variable already exists in this scope.
                        } else {
                            variableSet.put(trimmedName, new JavaType(generalStructureMatcher.group(2),
                                    whichCondition(), scope));
                            return true;
                        }
                    }
                }
                if ((variableSet.containsKey(lineName.trim())) && (variableSet.get(lineName.trim()).getScope() == scope))
                    throw new VariableAlreadyExistsException();
                variableSet.put(lineName.trim(), new JavaType(generalStructureMatcher.group(2), scope, isMethod));
                return true;
            }
            return false;
        }
        else if (lineName.split("=").length > 2) {
            throw new MoreThanOneEqualsException();
        } else {
            String[] varAndVal = lineName.split("=");
            if (isMethod)
                throw new AssignmentInFunctionDeclarationException();
            boolean isFinal = whichCondition();
            String var = varAndVal[0].trim();
            String val;
            try {
                val = varAndVal[1].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new EmptyAssignmentException();
            }
            String typeName = generalStructureMatcher.group(2);
            if (nameMatcherWithValue.matches()) {
                if ((variableSet.containsKey(var)) && (variableSet.get(var).getScope() == scope)) {
                    throw new VariableAlreadyExistsException();
                }
                int valsScope = isValDeclared(val);
                if (valsScope != -1) {
                    if (fullVariableSet.get(valsScope).get(val).getType().equals("double") && typeName.equals("int"))
                        return false;
                    variableSet.put(var, new JavaType(typeName, fullVariableSet.get(valsScope).get(val), isFinal, scope));
                } else {
                    variableSet.put(var, new JavaType(typeName, val, isFinal, scope));
                }
                return true;
            }
            throw new SyntaxException("line syntax is incorrect");
        }
    }

    /**
     *  This method receives a name of a variable and checks if it was already declared
     * @param name String representing variable to check
     * @return 'true' if variable previously declared
     */
    public int isValDeclared(String name) {
        for (int i = 0; i < fullVariableSet.size(); i++) {
            for (String var : fullVariableSet.get(i).keySet()) {
                if (var.equals(name))
                    return i;
            }
        }
        return -1;
    }

    /**
     * This method checks if the String line contains "final"
     * @return "true" - if String input contains keyword "final"; otherwise - "false"
     */
    private boolean whichCondition() {
        return generalStructureMatcher.group(1) != null;
    }

    /**
     * This method checks if the String input is not for the global scope
     * @param previousLine String representing the previous line
     * @return "true" if not in global scope; otherwise - "false"
     */
    public boolean innerScopeLine(String previousLine) {

        Matcher matcherClosed = endsWithClosedCurlyBrackets.matcher(inputString);
        Matcher matcherOpen = endsWithOpenCurlyBrackets.matcher(inputString);
        Matcher matchesReturnLine = returnLine.matcher(previousLine);
        if (matcherOpen.matches()) {
            increaseScope();
            return true;
        }
        if (matcherClosed.matches()) {
            switch (parenthasisCounterStack.size()) {
                case 0:
                    return false;
                case 1: {
                    if (matchesReturnLine.matches()) {
                        decreaseScope();
                        return true;
                    }
                    return false;
                }
                default:
                    decreaseScope();
                    return true;
            }
        }
        return true;
    }

    /**
     * Update the current scope by decreasing -1.
     */
    private void decreaseScope() {
        parenthasisCounterStack.pop();
        scope--;
    }

    /**
     * Update the Scope number, String input, dictionary of current scope and stack of parenthesis counter
     * @param scope new scope number
     * @param line new String input
     * @param currentVarDict new dictionary of relevant scope
     * @param pCounter stack counting current scope
     */
    public void update(int scope, String line, ArrayList<LinkedHashMap<String, JavaType>> currentVarDict,
                       Stack<Integer> pCounter) {
        setScope(scope);
        setString(line);
        variableSet = currentVarDict.get(scope);
        fullVariableSet = currentVarDict;
        parenthasisCounterStack = pCounter;

    }

    /**
     * This method checks the syntax of multiple variables
     * @return boolean 'true' if all unit variables are syntactically valid
     * @throws EmptyFinalDeclarationException exception thrown when there is an attempt for an empty final declaration
     * @throws VariableAlreadyExistsException exception thrown when variable already exists
     * @throws MoreThanOneEqualsException exception thrown when there is more than one equals
     * @throws AssignmentInFunctionDeclarationException exception thrown when there is an attempt for an assignment
     *                                          inside function parameters
     * @throws EmptyAssignmentException Exception thrown when there is an empty assignment
     * @throws SyntaxException Exception thrown when syntax is invalid
     */
    boolean checkMultiBlocks() throws EmptyFinalDeclarationException, VariableAlreadyExistsException,
            MoreThanOneEqualsException, AssignmentInFunctionDeclarationException, EmptyAssignmentException,SyntaxException {
        String lineWithComma = generalStructureMatcher.group(3);
        if (lineWithComma.startsWith(",") || lineWithComma.endsWith(","))
            return false;
        String[] dividedStringArray = lineWithComma.split(",");
        for (String subString : dividedStringArray) {
            if (!subStringNameChecker(subString)) {
                return false;
            } // made it here - all names valid
        }
        for (String subString : dividedStringArray) {
            checkSingleBlock(subString);
        }
        return true;
    }

    /**
     * This method checks if assignment syntax input is valid.
     * @param subString assignment input substring
     * @return 'true' if assignment is valid syntactically ; otherwise - "false"
     */
    boolean subStringNameChecker(String subString) {
        Matcher subStringnameMatcher = nameRegex.matcher(subString);
        Matcher subStringnameMatcherWithValue = nameRegexWithValue.matcher(subString);
        if (!subString.contains("=")) { // with no '='
            return subStringnameMatcher.matches();
        }
        return subStringnameMatcherWithValue.matches();
    }

    /**
     * This method checks if a variable line is syntactically valid.
     * @return if valid - "true"; otherwise - "false"
     * @throws EmptyFinalDeclarationException exception thrown when there is an attempt for an empty final declaration
     * @throws VariableAlreadyExistsException exception thrown when variable already exists
     * @throws MoreThanOneEqualsException exception thrown when there is more than one equals
     * @throws AssignmentInFunctionDeclarationException exception thrown when there is an attempt for an assignment
     *                                          inside function parameters
     * @throws EmptyAssignmentException Exception thrown when there is an empty assignment
     * @throws SyntaxException Exception thrown when syntax is invalid
     * @throws ClassCastException Exception thrown when tried to  assign improper value to a class
     */
    public boolean checkSyntaxValidity() throws EmptyFinalDeclarationException, VariableAlreadyExistsException,
            MoreThanOneEqualsException, AssignmentInFunctionDeclarationException, EmptyAssignmentException, ClassCastException,
    SyntaxException{

        if (!checkGeneralValidity() && !checkPrivateValidity())
            return false;
        if (!inputString.contains(",")) { // if contains
            String fullComalessLine = null;
            if (checkGeneralValidity()) {
                fullComalessLine = generalStructureMatcher.group(3);
                return checkSingleBlock(fullComalessLine);
            } else {
                fullComalessLine = privateStructureMatcher.group(2);
                return checkAssignment(fullComalessLine);
            }
        } // has more than one block
        return checkMultiBlocks();
    }

    /**
     * This method checks is syntax for assignment is valid
     * @param lineName input String to check
     * @return if assignment syntax valid - "true"; otherwise - "false"
     */
    public boolean checkAssignment(String lineName) {
        Matcher nameMatcherWithValue = nameRegexWithValue.matcher(lineName);
        if (!nameMatcherWithValue.matches())
            return false;
        String varName = nameMatcherWithValue.group(1);
        String value = nameMatcherWithValue.group(6);
        int scopeOfVar=isValDeclared(varName);
        if (scopeOfVar==-1)
            return false;
        try {
            fullVariableSet.get(scopeOfVar).get(varName).update(value);
        } catch (FinalAssignmentException | ClassCastException e) {
            return false;
        }
        return true;
    }
}


