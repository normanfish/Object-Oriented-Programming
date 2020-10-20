package oop.ex6.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


import oop.ex6.main.Regex.NoReturnValueException;
import oop.ex6.main.Regex.ValidatorException;
import oop.ex6.main.Regex.FirstRunnerValidator;
import oop.ex6.main.Types.JavaType;
import oop.ex6.main.Regex.SecondRunnerValidator;
import oop.ex6.main.Types.FunctionType;
import oop.ex6.main.Regex.SyntaxException;

import java.io.*;
import java.util.regex.*;
import java.util.*;

/**
 * This class handles the code inspection. It's main class checks a code, and prints the appropriate message.
 * The class goes over the code twice (each time is a called a "run"). As described in the README, the first run is
 * meant to declare the global variables, functions and make sure the structure of the scopes (brackets and all) is
 * correct. The second run handles all of the other commands.
 * The main purpose of this separation is so that all functions and global variables will be accesible from anywhere in
 * the code, regardless of their place of declaration.
 */
public class Sjavac {


    /*a set holding information about the lines that we have already visited in the first run. will be convenient
    going into the second run, as we can skip those lines*/
    public Set<Integer> visitedLineSet;
    /*a list of ordered sets with keys being strings and holding JavaType objects.
     *First, the pairs of String and Javatypes represent the type declared in the program by it's name.
     * Second, the LinkedHashMap is an ordered set of all of the variables in a certain scope. The purpose of keeping
     * them ordered is so that it is easy to compare to functions variables
     * Lastly, Each LinkedHashMap in the Arraylist corresponds to the scope it is in the ArrayList.
     * For instance, ArrayList[0] will hold the global scope variables
     */
    public ArrayList<LinkedHashMap<String, JavaType>> variableDict;
    /*A set containing all of the functions declared in the program*/
    public TreeMap<String, FunctionType> methodDict;
    /*A stack that keeps track of the open and closed brackets. Useful for keeping track that all parenthesis are opened
    and closed correctly. Each time a curly bracket appears, 0 is inserted into the stack. When a closed curly bracket
    appears, then the top 0 in the stack is removed.*/
    public Stack<Integer> parenthesisCounter;
    public static int FIRST_SCOPE = 0;

    /**
     * The default class Constructor. Initializes all of the data structure fields to new and empty.
     */
    Sjavac() {
        this.visitedLineSet = new HashSet<>();
        this.variableDict = new ArrayList<>();
        variableDict.add(new LinkedHashMap<>());
        this.methodDict = new TreeMap<>();
        parenthesisCounter = new Stack<>();
    }

    /**
     * The first run of the program
     *
     * @param filePath the path of the code we wish to go over
     * @param scope    the scope we are currently in
     * @return true if all of the declarations of global functions, parameters, and block structure is OK, and false
     * if not.
     * @throws IOException if there was a problem with handling the file
     */
    private boolean firstRunner(String filePath, int scope) throws IOException {
        File file = new File(filePath);
        FileReader fileReader = null;
        String prevLine = "";/*the previous line which we will update every iteration of the first runner.*/
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;//the line we are currently on
            int lineCounter = 0;//counts the lines
            /*Creates an instance of FirstVariableValidator, that will handle most of the checks and tasks of the first
             runner*/
            FirstRunnerValidator variableHandler = new FirstRunnerValidator("", this.variableDict.get(FIRST_SCOPE),
                    methodDict, parenthesisCounter, true, scope, variableDict);
            while ((line = bufferedReader.readLine()) != null) {
                /*we have reached a not empty line*/
                /*let's check if it's a comment line.*/
                lineCounter++;
                /*the regex matching a comment line*/
                Pattern commentLinePattern = Pattern.compile("//");
                Matcher commentLine = commentLinePattern.matcher((line));
                if (line.matches("[ \\t\\r]*") || commentLine.lookingAt()) {
                    /*we have reached a comment line! skip to the next line*/
                    this.visitedLineSet.add(lineCounter);
                    continue;
                }

                /*it is not a comment line.*/
                /*first, we'll check if we are in the global scope*/
                if (parenthesisCounter.isEmpty()) {
                    /*we are in the global scope! we'll now check and handle both legal cases in the global scope,
                    variable handling and declaration, and function declaration*/
                    if (!golbalScopeHandler(line, variableHandler))
                        return false;
                    visitedLineSet.add(lineCounter);
                }
                /*if we have reached here, that means that we are NOT in the global scope. so we will just keep track of
                the number of brackets opened and closed*/
                else {
                    if (!innerScopeHandler(line, prevLine, variableHandler))
                        throw new NoReturnValueException();
                }
                prevLine = line;//update the previos line
            }
            /*catches any exception that was thrown from the Validator Types. If any Exception was thrown, we return
             false, as the method had errors in the commands.*/
        } catch (ValidatorException | ClassCastException e) {
            return false;
        }
        /*catches any IO errors*/ catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        /*if we have reached here, it means all of the lines relevant to global variables and functions
        syntacticly correct, so we just have to check that we indeed closed all open brackets*/
        return parenthesisCounter.isEmpty();
    }

    /**
     * The second runner handles the commands that aren't in the global scope
     *
     * @param filePath the path to the file that contains the code
     * @return true if all of the commands make sense, and false otherwise
     * @throws IOException in case there is some problem with the file
     */
    private boolean secondRunner(String filePath) throws IOException {
        File file = new File(filePath);
        FileReader fileReader = null;
        int lineCounter = 0; // counts the lines
        int localScope = 0; // a variable that holds which scope we are currently in
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();

            String line; // the line we are currently on
            /*the following creates an instance of FirstRunnerValidator, that will be used to initialize LOCAL parameters,
            it will be updates every iteration to hold the relevant arraylist of scopes, scope number and so on.*/
            FirstRunnerValidator funcInitializer = new FirstRunnerValidator("", variableDict.get(localScope),
                    methodDict, parenthesisCounter, false, 0, variableDict);
            /*Creates an instance of SecondRunnerValidator, which handles most of the actions needed to take once we
            know what line we are reading*/
            SecondRunnerValidator conditionChecker = new SecondRunnerValidator("", variableDict, methodDict, localScope);
            while ((line = bufferedReader.readLine()) != null) {
                /*first, we'll update that necessary parameters for the Runer instances*/
                funcInitializer.update(localScope, line, variableDict, parenthesisCounter);
                conditionChecker.update(line, localScope, variableDict);
                lineCounter++;//increase the line counter
                /*now, we parse the lines. there are a number of possible scenarios for legal lines. for each one,
                //we will act accordingly, primarily using the RunnerValidators*/

                /*first, handle redeclaring the function parameters into the set containing the scope's parameters.*/
                try {
                    localScope = funcParamInitializer(localScope, funcInitializer);
                }
                 catch (NullPointerException e) {
                    return false;
                }

                /*next, if we are at a line we already visited, or at a "return" line, we can skip them.*/
                if (visitedLineSet.contains(lineCounter) || line.matches("[ \\t\\r]*return[ \\t\\r]*;[ \\t\\r]*"))
                    continue;

                /*next, init and declaration for of local parameters.*/
                if (funcInitializer.checkSyntaxValidity()) {
                    continue;
                }

                /* now if/while conditions*/
                if (conditionChecker.checkBooleanSyntax()) {
                    /*increase the scope, add 0 to the stack keeping track of parenthesis, and open a new "scope" in the
                    //Arraylist containing the variables of the function.*/
                    localScope++;
                    parenthesisCounter.push(0);
                    variableDict.add(new LinkedHashMap<>());
                    continue;
                }

                /*checks "}" lines*/
                if (line.matches("[ \\t\\r]*}[ \\t\\r]*")) {
                    /*reduce the scope, and remove the scope from the list of scope, as all of the variables that were
                    declared in this scope go alone with it.*/
                    variableDict.remove(localScope);
                    localScope--;
                    parenthesisCounter.pop();
                    continue;
                }
                /*checks function calls*/
                if (conditionChecker.checkMethodCallSyntax()) {
                    /*gets the name of the functions that was called, along with it's parameter types.*/
                    ArrayList<String> funcNameAndParams = conditionChecker.getCalledMeth();
                    /*now, we'll check if it matches and of the functions that we declared in the global scope*/
                    if (!conditionChecker.getParamsandCheck(funcNameAndParams.get(0), funcNameAndParams.subList(1,
                            funcNameAndParams.size()))) {
                        throw new SyntaxException("bad call to function in line" + lineCounter);
                    }
                    continue;
                }

                /*if we have reached here, it means that the line doesn't fit in to any one of the other "molds",
                therfore, it is an incorrect line syntactically, and we'll throw an exception.*/
                throw new SyntaxException("bad call to function in line" + lineCounter);
            }

            /*catch any exceptions that might arise in the program. we will return false as these indicate the program
            has illegal commands*/
        } catch (ValidatorException | ClassCastException e) {
            return false;
        }
        /*if we reached here, that means that all of the commands in the program are OK! we can return true to the main.*/
        return true;
    }

    /**
     * this function is called in the second run, when we encounter a function declaration line,
     * it adds all of the parameters that the function accepts as local parameters in the relevant scope.
     *
     * @param localScope      the scope we are currently in. we will increase this by one as the function itself
     *                        opens a new scope by definition
     * @param funcInitializer a FirstRunner Instance that we will use to initialize the variables
     * @return the new scope that the code is in after the variables were declared
     */
    private int funcParamInitializer(int localScope, FirstRunnerValidator funcInitializer) {
        /*check that indeed the method exists in the set of the functions declared in the global scope.*/
        if (funcInitializer.checkGeneralMethodName()) {
            String methodName = funcInitializer.getMehodName(); // method's name
            LinkedHashMap<String, JavaType> methodParams = methodDict.get(methodName).getParameters();//it's parameters
            localScope++;//increase the scope and add the parenthesis to the stack
            parenthesisCounter.add(0);
            variableDict.add(new LinkedHashMap<String, JavaType>()); //create a new scope of variables
            /*add the parameters from the function declaration line to the newly opened scope*/
            for (String param : methodParams.keySet()) {
                variableDict.get(localScope).put(param, methodDict.get(methodName).getParameters().get(param));
            }
        }
        return localScope; // the new scope we are on
    }

    /**
     * this function is called by the first runner function, and it declares a function or a variable.
     *
     * @param line            the line we are currently on
     * @param variableHandler an instance of FirstRunnerValidator we will use to create the variables and functions
     * @return if the parameters and variables were initiated successfully, and false otherwise
     * @throws ValidatorException any exception that results from invalid commands
     */
    private boolean golbalScopeHandler(String line, FirstRunnerValidator variableHandler) throws ValidatorException {
        /* first, we'll check if we are parsing a function declaration line, and declare it.*/
        variableHandler.setMethod(true);
        variableHandler.setString(line);
        if (variableHandler.checkMethodSyntax()) {
            return true;
        }
        /*next, we'll check if we are parsing a global declaration/ initialization of a global variable, and use the
        FirstValidatorRunner to add it to the program.*/
        variableHandler.setMethod(false);
        if (!variableHandler.checkSyntaxValidity())
            /*we have reached here if the line is not a declaration of function or a variable. throw syntax error, as no
            other lines are legal in the global scope.*/
            throw new SyntaxException("bad syntax in line" + line);
        return true;
    }

    /**
     * handles the case where we are in an inner scope line while in the first runner. Simply delegates the job of
     * keeping count of the brackets to the RunnerValidator
     *
     * @param currentLine     the current line we are in
     * @param prevLine        the previous line
     * @param variableHandler the instance of the FirstRunnerValidator, through which we will how to handle the line
     * @return true if the inner line complies with the rules of the program, and false otherwise.
     */
    private boolean innerScopeHandler(String currentLine, String prevLine, FirstRunnerValidator variableHandler) {
        variableHandler.setString(currentLine);
        return variableHandler.innerScopeLine(prevLine);
    }

    /**
     * runs the program
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        Sjavac runner = new Sjavac();
        try {
            if (runner.firstRunner(args[0], 0) && runner.secondRunner(args[0])) {
                System.out.println(0);
            } else {
                System.out.println(1);
            }
        } catch (IOException e) {
            System.out.println(2);
            System.err.println("Error: Problem with the file");
        }
    }
}
