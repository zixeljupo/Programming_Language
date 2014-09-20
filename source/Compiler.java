package code;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 
 * @author Jupo
 * 
 * This is a superclass for complex compilers.
 *
 */
public class Compiler {
    
    public static boolean isDigit(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }
    
    public static boolean isOperation(char sym) {
        return sym == '+' || sym == '-' || sym == '*' || sym == '/' || sym == '^' || sym == '%';
    }
    
    public static boolean isOperation(String sym) {
        return sym.equals("+") || sym.equals("-") || sym.equals("*") || sym.equals("/") || sym.equals("^") || sym.equals("%");
    }
    
    public static Map<String, Integer> operationsPriority = new HashMap<String, Integer>();
    
    public static int firstOverride(String first, String second) {
        if (operationsPriority.get(first) > operationsPriority.get(second)) {
            return 1;
        } else if (operationsPriority.get(first) == operationsPriority.get(second)) {
            return 0;
        }
        return -1;
    }
    
    public static String createPostfix(String infix) {
        
        Stack<String> stack = new Stack<String>();
        String postfix = "";
        
        stack.push("(");
        infix += ")";
        
        int i = 0;
        while (!stack.isEmpty()) {
            
            char sym = infix.charAt(i);
            
            if (!isOperation(sym) && sym != '(' && sym != ')') {
                postfix += sym;
            } else if (isOperation(sym)) {
                postfix += " ";
                while (isOperation(stack.lastElement()) && (firstOverride(stack.lastElement(), sym + "") != -1)) {
                    postfix += stack.pop();
                    postfix += " ";
                }
                stack.push("" + sym);
            } else if (sym == '(') {
                stack.push("(");
            } else if (sym == ')') {
                postfix += " ";
                while (!stack.lastElement().equals("(")) {
                    postfix += stack.pop();
                }
                stack.pop();
            }
            
            i++;
            if (i >= infix.length()) break;
        }
        
        return postfix;
    }
}
