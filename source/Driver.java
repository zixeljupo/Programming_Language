package code;

import java.util.Scanner;

/**
 * 
 * @author Jupo
 * 
 * Here is a driver for Prostotron Computer.
 * 
 */
public class Driver {
    
    public static Computer computer;
    private static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        computer = new Computer();
        
        {
            System.out.println("Do you want to choose symbols file for your programm?");
            System.out.print("(Answer \"y\" or \"n\") : ");
            
            String answer = scan.next();
            
            if (answer.equals("y")) {
                
                System.out.println("Write file path here:");
                System.out.print("Path : ");
                
                try {
                    computer.loadSymbols(scan.next());
                } catch (Exception e) {
                    System.out.println("I cannot load this file. I won't load symbols.");
                }
                
            } else if (answer.equals("n")) {
                System.out.println("I won't load symbols.");
            } else {
                System.out.println("I cannot inderstand your answer. I won't load symbols.");
            } 
        }
        
        {
            System.out.println("Do you want to load your program file or write it now?");
            System.out.print("(Answer \"l\" or \"w\") : ");
            
            String answer = scan.next();
            
            if (answer.equals("l")) {
                
                System.out.println("Write file path here:");
                System.out.print("Path : ");
                
                try {
                    computer.loadProgram(scan.next());
                } catch (Exception e) {
                    System.out.println("I cannot load this file. I won't load your program.");
                    return;
                }
                
            } else if (answer.equals("w")) {
                computer.writeProgram();
            } else {
                System.out.println("I cannot inderstand your answer. I won't load program.");
                return;
            } 
        }
        
        computer.startProgram();
    }
    
}
