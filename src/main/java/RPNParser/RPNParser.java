package RPNParser;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * This class divides inputting string in lexemes and orders for RPN
 */
public class RPNParser {
    /**
     * final field delimiters defines that calculator supports
     */
    private final String operators = ("+-*/");
    /**
     * final field delimiters defines delimiters for build-in class Tokenizer
     */
    protected final String delimiters = operators + "()";
    /**
     * field errorNum shows code of error in inputting string for StrToRPN
     */
    protected int errorNum;

    /**
     * Constructor for class Parser - creation of the new object
     * Initialize param errorNum with 0 value - it means that no errors are found yet
     */
    public RPNParser() {
        errorNum = 0;
    }

    /**
     * Method isNumeric checks is inputting string is numeric
     *
     * @param str - Inputting string to check
     * @return true - If str is numeric in other way false
     */
    protected boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Method OperationPriority executes priority for inputting string
     *
     * @param check - inputting string
     * @return -2 - If it gets unexpected expression
     * -1 - If it gets WHITESPACE
     * 0 - If it gets number
     * 1 - If it gets predefined math function
     * 2 - If it gets bracket
     * 3 - If it gets back bracket
     * 4 - If it gets operators such as addition and subtraction
     * 5 - If it gets operators such as multiplication and division
     */
    protected int OperationPriority(String check) {
        if (check.equals("*") || check.equals("/")) return 5;
        if (check.equals("+") || check.equals("-")) return 4;
        if (check.equals(")")) return 3;
        if (check.equals("(")) return 2;
        if (isFunc(check)) return 1;
        if (isNumeric(check)) return 0;
        if (check.equals(" ")) return -1;
        return -2;
    }

    /**
     * Method isOperator checks is inputting string operator and defines type of operator
     *
     * @param check - String for check
     * @param prev  - Previous string in expression
     * @return 0 - If checking string isn`t operator
     * 1 - If checking string is unary operator
     * 2 - If checking string is binary operator
     */
    protected int isOperator(String check, String prev) {
        if (prev.equals("+") || prev.equals("-") || prev.equals("/") || prev.equals("*") || prev.equals("") || prev.equals("(")) {
            if (check.equals("+") || check.equals("-")) {
                return 1;
            }
        } else {
            for (int i = 0; i < operators.length(); i++) {
                if (operators.charAt(i) == check.charAt(0)) {
                    return 2;
                }
            }
        }
        return 0;
    }

    /**
     * Method isOperator checks is inputting string operator
     *
     * @param check- String for check
     * @return true - if inputting string is operator
     * false - in other way
     */
    protected boolean isOperator(String check) {
        if (check.length() != 1) return false;
        for (int i = 0; i < operators.length(); i++) {
            if (operators.charAt(i) == check.charAt(0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method isFunc checks if current string is mathematical function
     *
     * @param check - String to check
     * @return true - If string is function ; false - in other way
     */
    protected static boolean isFunc(String check) {
        return check.equals("pow2") || check.equals("sqrt") || check.equals("mod10");
    }

    /**
     * Method isInputCorrect checks if current string has not any unexpected operands in the beginning and ending of expression
     *
     * @param input - String to check
     * @return true - If String has not any unexpected operands in the beginning and ending of expression
     * false - In other way
     */
    protected boolean isInputCorrect(String input) {
        if (input.length() < 3) return false;
        if (input.charAt(0) == '*' || input.charAt(0) == '/') return false;
        String s = "" + input.charAt(input.length() - 1);
        return !isOperator(s);
    }

    /**
     * Method StrToRPN converts standard notation of expression into reversed polish notation
     *
     * @param input - String to convert
     * @return output - Result of converting to RPN
     */
    protected String StrToRPN(String input) {
        StringBuilder output = new StringBuilder();
        if (!isInputCorrect(input)) {
            errorNum = 3;
            return output.toString();
        }
        StringTokenizer tokenizer = new StringTokenizer(input, delimiters + " ", true);
        Stack<String> stack = new Stack<>();
        String currentToken;
        String previousToken = "";
        boolean isUnaryPrev = false;
        int priorityCurrent;

        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
            if (currentToken.equals(" ")) continue;
            priorityCurrent = OperationPriority(currentToken);
            switch (priorityCurrent) {
                case -2:
                    errorNum = 1;
                    return output.toString();
                case -1:
                    continue;
                case 0:
                    output.append(" ").append(currentToken);
                    if (isUnaryPrev) {
                        output.append(" ").append(stack.pop());
                        isUnaryPrev = false;
                    }
                    break;
                case 1:
                case 2:
                    stack.push(currentToken);
                    break;
                case 3:
                    while (!stack.peek().equals("(")) {
                        if (!stack.isEmpty()) {
                            output.append(" ").append(stack.pop());
                        } else {
                            errorNum = 2;
                            return output.toString();
                        }
                    }
                    stack.pop();
                    if (isFunc(stack.peek())) output.append(" ").append(stack.pop());
                    break;
                case 4:
                    if (!tokenizer.hasMoreTokens()) {
                        errorNum = 3;
                        return output.toString();
                    }
                    if (isOperator(currentToken, previousToken) == 1) {
                        isUnaryPrev = true;
                        stack.push("u" + currentToken);
                    } else {
                        while (!stack.isEmpty() && OperationPriority(stack.peek()) >= priorityCurrent) {
                            output.append(" ").append(stack.pop());
                        }
                        stack.push(currentToken);
                    }
                    break;
                case 5:
                    if (!tokenizer.hasMoreTokens()) {
                        errorNum = 3;
                        return output.toString();
                    }
                    while (!stack.isEmpty() && OperationPriority(stack.peek()) >= priorityCurrent) {
                        output.append(" ").append(stack.pop());
                    }
                    stack.push(currentToken);
            }
            previousToken = currentToken;
        }
        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) output.append(" ").append(stack.pop());
            else {
                errorNum = 2;
                return output.toString();
            }
        }
        return output.toString();
    }

}
