package Calculator;

import RPNParser.RPNParser;

import java.util.Stack;
import java.util.StringTokenizer;

import static java.lang.Math.pow;

/**
 * Class Calculator
 * Its executes main functions of Calculator for inputting string such as +, -, *, /, ^2, sqrt, %10
 *
 * @author Усов Андрей
 */

public class Calculator extends RPNParser {
    /**
     * Method Calculation computes inputting expression
     *
     * @param input - String in infix notation
     * @return 1e-9 - If input string is incorrect, else returns result of calculation
     */
    public double Calculation(String input) {
        String output = StrToRPN(input);
        StringTokenizer tokenizer = new StringTokenizer(output, " ", true);
        Stack<Double> stack = new Stack<>();
        Double result = 1e-9;
        String currentToken;
        switch (errorNum) {
            case 0:
                while (tokenizer.hasMoreTokens()) {
                    currentToken = tokenizer.nextToken();
                    switch (currentToken) {
                        case " ":
                            continue;
                        case "mod10":
                            stack.push(stack.pop() % 10);
                            break;
                        case "pow2":
                            stack.push(pow(stack.pop(), 2));
                            break;
                        case "sqrt":
                            stack.push(pow(stack.pop(), 0.5));
                            break;
                        case "u-":
                            stack.push(-stack.pop());
                            break;
                        case "u+":
                            break;
                        case "+":
                            stack.push(stack.pop() + stack.pop());
                            break;
                        case "-":
                            Double b = stack.pop(), a = stack.pop();
                            stack.push(a - b);
                            break;
                        case "*":
                            stack.push(stack.pop() * stack.pop());
                            break;
                        case "/":
                            Double c = stack.pop(), d = stack.pop();
                            stack.push(d / c);
                            break;
                        default:
                            stack.push(Double.parseDouble(currentToken));
                    }
                }
                if (!stack.isEmpty()) {
                    result = stack.pop();
                    System.out.println(result);
                }
                break;
            case 1:
                System.out.println("Error 1:Input string has unexpected expressions");
                break;
            case 2:
                System.out.println("Error 2:In input string brackets not matched");
                break;
            case 3:
                System.out.println("Error 3:Input string has unexpected symbols at the beginning or ending");
                break;
        }
        errorNum = 0;
        return result;
    }

}
