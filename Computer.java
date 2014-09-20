package code;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author Jupo
 * 
 * @version 1.0.2
 * 
 *      Here is a computer modulation class. He can do simple functions such as add, divide, multiply,
 * subtract, read from keyboard and show something in console. Also it has some logical functions
 * that controls your program. To write the program create a new text file and write path to it in
 * driver class. This computer can understand 6-number computer words. First two digits are 
 * OPERAION CODE digits. You can see constants to understand what they do. Other four digits are
 * OPERAND digits. They are an argument of processor functions. To the most of the commands it is
 * a memory cell on which current function will make an action. This computer has 10^4 memory cells.
 * All commands will set program control onto the next word except logical functions.
 *
 */
public class Computer {
    
    public static final String computerName = "Prostotron";
    public static final String languageName = "Prostotron Programming Language";
    
    public static final int MEMORY_SIZE = 10000;
    
    public static final int ABORT_COMMAND = 01;
    
    public static final int READ_COMMAND = 11;
    public static final int WRITE_COMMAND = 12;
    public static final int SYMBOL_COMMAND = 13;
    
    public static final int LOAD_COMMAND = 21;
    public static final int STORE_COMMAND = 22;
    
    public static final int ADD_COMMAND = 31;
    public static final int SUBTRACT_COMMAND = 32;
    public static final int MULTIPLY_COMMAND = 33;
    public static final int DIVIDE_COMMAND = 34;
    
    public static final int BRANCH_COMMAND = 41;
    public static final int BRANCHZERO_COMMAND = 42;
    public static final int BRANCHNEGATIVE_COMMAND = 43;
    
    private static Map<Integer, Executable> functions = new HashMap<Integer, Executable>();
    
    private int[] memory = new int[MEMORY_SIZE];
    
    // an array of symbols that can be shown by symbol method
    private char[] symbols = new char[MEMORY_SIZE];
    
    // Accumulator is an operative memory of this computer. It can save integer numbers. 
    // All math functions will save their results here.
    private int accumulator = +000000;
    
    // current executing word
    private int instructionRegister = +000000;
    
    // current command memory cell
    private int instructionCounter = 0000;
    
    private int operationCode = +00;
    
    // a memory cell on which function will make an action
    private int operand = 0000;
    
    private boolean aborted = false; // program state
    private boolean printLoop = false; // show program loop info
    
    /*
     * Print any info or not.
     * Computer can generate messages. If printInfo is true it will show them in console,
     * otherwise it will send it to other object to process it(in integer value).
     */
    private boolean printInfo = true;
    
    private static Scanner scan = new Scanner(System.in); // keyboard scanner
    
    private Messenger<Integer> outputMessenger; // messenger to send computer messages for handling
    
    /**
     *  constructor that initializes computer functionality
     */
    public Computer() {
        this(null);
    }
    
    /**
     *  constructor that initializes output messenger and computer functionality
     */
    public Computer(Messenger<Integer> ms) {
        
        outputMessenger = ms;
        
        functions.put(Computer.ABORT_COMMAND, abort);
        functions.put(Computer.READ_COMMAND, read);
        functions.put(Computer.WRITE_COMMAND, write);
        functions.put(Computer.SYMBOL_COMMAND, symbol);
        functions.put(Computer.LOAD_COMMAND, load);
        functions.put(Computer.STORE_COMMAND, store);
        functions.put(Computer.ADD_COMMAND, add);
        functions.put(Computer.SUBTRACT_COMMAND, subtract);
        functions.put(Computer.MULTIPLY_COMMAND, multiply);
        functions.put(Computer.DIVIDE_COMMAND, divide);
        functions.put(Computer.BRANCH_COMMAND, branch);
        functions.put(Computer.BRANCHZERO_COMMAND, branchZero);
        functions.put(Computer.BRANCHNEGATIVE_COMMAND, branchNegative);
    }
    
    public boolean isAborted() {
        return aborted;
    }
    
    public void printLoop(boolean print) {
        printLoop = print;
    }
    
    public boolean isPrintLoop() {
        return printLoop;
    }
    
    public void printInfo(boolean print) {
        printInfo = print;
    }
    
    public boolean isPrintInfo() {
        return printInfo;
    }
    
    /**
     *  initializes messenger
     */
    public void initOutputMessenger(Messenger<Integer> inputMessenger) {
        outputMessenger = new Messenger<Integer>(inputMessenger);
    }
    
    /**
     *  Returns a string that represent integer number with chosen pattern
     *  and with sign if sign parameter is true.
     */
    private static String writeNum(String pattern, int value, boolean sign) {
        DecimalFormat f = new DecimalFormat(pattern);
        String output = f.format(value);
        return (sign ? (value < 0 ? "-" : "+") : "") + output;
    }
    
    /**
     *  Loads program from file.
     *  The file should contains integer number lower than 999999 and bigger than -999999.
     *  All numbers should located on different lines.
     */
    public void loadProgram(String fileName) {
        
        FileInputStream fis;
        
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            if (printInfo)
                System.out.println("FATAL: unable to load file!");
            else
                outputMessenger.sendMessage(01);
            return;
        }
        
        Scanner scan = new Scanner(fis);
        
        int i = 0;
        while (scan.hasNextLine()) {
            if (i > MEMORY_SIZE - 1) {
                if (printInfo)
                    System.out.println("FATAL: not enough memory to load program!");
                else
                    outputMessenger.sendMessage(02);
                break;
            } else memory[i++] = scan.nextInt();
        }
        
        scan.close();
    }
    
    /**
     *  Allows user to create PPL programs in command line.
     *  User should write number that is bigger or lower than computer word to stop read loop.
     */
    public void writeProgram() {
        
        for (int cell = 0; cell < MEMORY_SIZE; cell++) {
            
            System.out.println("Write computer word here:");
            System.out.print(writeNum("0000", cell, false) + " ? ");
            
            if (scan.hasNextInt()) {
                
                int num = scan.nextInt();
                
                if (num > MEMORY_SIZE*100 - 1 || num < -MEMORY_SIZE*100 + 1) {
                    break;
                } else {
                    memory[cell] = num;
                }
            } else {
                System.out.println("Illegal input data!");
                scan.nextLine();
                cell--;
            }
        }
    }
    
    /**
     *  Loads symbols from file.
     *  The file should contains numbers from 00 to 99.
     *  After a number on each line you should put SPACE and after that put symbol
     *  (It should be 4-th on each line)
     */
    public void loadSymbols(String symbolsFile) {
        
        FileInputStream fis;
        
        try {
            fis = new FileInputStream(symbolsFile);
        } catch (FileNotFoundException e) {
            if (printInfo)
                System.out.println("FATAL: unable to load symbols!");
            else
                outputMessenger.sendMessage(03);
            return;
        }
        
        Scanner scan = new Scanner(fis);
        
        int i = 0;
        
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
            char symbol = s.toCharArray()[3];
            if (i > MEMORY_SIZE - 1) {
                if (printInfo)
                    System.out.println("FATAL: not enough memory to load symbols!");
                else
                    outputMessenger.sendMessage(04);
                break;
            } else symbols[i++] = symbol;
        }
        
        scan.close();
    }
    
    /**
     *  prints memory dump.
     */
    public void printMemory() {
        System.out.println("Memory dump:");
        for(int i = 0; i < MEMORY_SIZE; i++) System.out.println(memory[i]);
    }
    
    /**
     *  prints all computer dump.
     */
    public void printComputerDump() {
        System.out.println();
        printRegisters();
        //printMemory();
    }
    
    /**
     *  prints register dump.
     */
    public void printRegisters() {
        System.out.println("Registers dump:");
        System.out.println("Instruction Counter = " + writeNum("0000", instructionCounter, false));
        System.out.println("Instruction Register = " + writeNum("000000", instructionRegister, true));
        System.out.println("Accumulator = " + writeNum("000000", accumulator, true));
        System.out.println("Operation Code = " + writeNum("00", operationCode, false));
        System.out.println("Operand = " + writeNum("00",operand, false));
    }
    
    /**
     *  Starts the program.
     *  Program running is situated in a loop, that runs until program aborts.
     */
    public void startProgram() {
        
        if (printInfo) System.out.println("Program has started.");
            else outputMessenger.sendMessage(60);
        
        while (!aborted) {
            
            instructionRegister = memory[instructionCounter]; // reads register from memory
            
            operationCode = instructionRegister / MEMORY_SIZE; // initializes operation
            operand = instructionRegister % MEMORY_SIZE; // initializes function argument(operand)
            
            // prints each game loop iteration if such option has been chosen
            if (printLoop && printInfo) {
                System.out.println();
                printRegisters();
            }
            
            
            // executing current operation
            try {
                functions.get(operationCode).execute();
            } catch (Exception e) {
                
                // if illegal operation exists
                if (printInfo)
                    System.out.println("RUNTIME ERROR: illegal operation code value.\n"
                                     + "Program will be aborted.");
                else
                    outputMessenger.sendMessage(10);
                operand = 1;
                abort.execute();
            }
        }
    }
    
    private Executable abort = new Executable() {
        
        /**
         *  exits the program with operand code mod 10
         */
        @Override
        public void execute() {
            if (printInfo) {
                System.out.println();
                System.out.println("Program exited with code " + operand % 10 + ".");
                printComputerDump();
            } else {
                outputMessenger.sendMessage(90 + operand % 10);
            }
            aborted = true;
        }
    };
    
    private Executable read = new Executable() {
        
        /**
         *  reads integer data from keyboard (should be in range of computer word)
         */
        @Override
        public void execute() {
            
            boolean failed = false;
            int number = MEMORY_SIZE;
            
            if (printInfo) {
                System.out.print("? : ");
            }
            
            try {
                number = scan.nextInt();
            } catch (Exception e) {
                failed = true;
            }
            
            if (number > MEMORY_SIZE*100 - 1 || number < -MEMORY_SIZE*100 + 1) {
                failed = true;
            } else {
                memory[operand] = number;
            }
            
            if (failed) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: illegal input argument.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(11);
                }
                operand = 1;
                abort.execute();
            }
            
            instructionCounter++;
        }
    };
    
    private Executable write = new Executable() {
        
        /**
         *  writes current memory cell value
         */
        @Override
        public void execute() {
            System.out.println(memory[operand]);
            instructionCounter++;
        }
    };
    
    private Executable symbol = new Executable() {
        
        /**
         *  writes a symbol from symbols array
         */
        @Override
        public void execute() {
            System.out.print(symbols[operand]);
            instructionCounter++;
        }
    };
    
    private Executable load = new Executable() {
        
        /**
         *  loads value from operand to accumulator
         */
        @Override
        public void execute() {
            accumulator = memory[operand];
            instructionCounter++;
        }
    };
    
    private Executable store = new Executable() {
        
        /**
         *  loads value from accumulator to operand
         */
        @Override
        public void execute() {
            memory[operand] = accumulator;
            instructionCounter++;
        }
    };
    
    private Executable add = new Executable() {
        
        /**
         *  math operation(can throw exception if number is out of computer word)
         */
        @Override
        public void execute() {
            
            boolean failed = false;
            
            accumulator += memory[operand];
            
            if (accumulator > MEMORY_SIZE*100 - 1 || accumulator < -MEMORY_SIZE*100 + 1) {
                failed = true;
            }
            
            if (failed) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: out of possible range accumulator value.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(12);
                }
                operand = 1;
                abort.execute();
            }
            
            instructionCounter++;
        }
    };
    
    private Executable subtract = new Executable() {
        
        /**
         *  math operation(can throw exception if number is out of computer word)
         */
        @Override
        public void execute() {
            
            boolean failed = false;
            
            accumulator -= memory[operand];
            
            if (accumulator > MEMORY_SIZE*100 - 1 || accumulator < -MEMORY_SIZE*100 + 1) {
                failed = true;
            }
            
            if (failed) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: out of possible range accumulator value.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(12);
                }
                operand = 1;
                abort.execute();
            }
            
            instructionCounter++;
        }
    };
    
    private Executable multiply = new Executable() {
        
        /**
         *  math operation(can throw exception if number is out of computer word)
         */
        @Override
        public void execute() {
            
            boolean failed = false;
            
            accumulator *= memory[operand];
            
            if (accumulator > MEMORY_SIZE*100 - 1 || accumulator < -MEMORY_SIZE*100 + 1) {
                failed = true;
            }
            
            if (failed) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: out of possible range accumulator value.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(12);
                }
                operand = 1;
                abort.execute();
            }
            
            instructionCounter++;
        }
    };
    
    private Executable divide = new Executable() {
        
        /**
         *  math operation(can throw exception if number is out of computer word)
         *  also can throw exception if zero division detected
         */
        @Override
        public void execute() {
            
            boolean failed = false;
            
            try {
                accumulator /= memory[operand];
            } catch (Exception e) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: zero division.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(13);
                }
                operand = 1;
                abort.execute();
                return;
            }
            
            if (accumulator > MEMORY_SIZE*100 - 1 || accumulator < -MEMORY_SIZE*100 + 1) {
                failed = true;
            }
            
            if (failed) {
                if (printInfo) {
                    System.out.println("RUNTIME ERROR: out of possible range accumulator value.\n"
                                     + "Program will be aborted.");
                } else {
                    outputMessenger.sendMessage(12);
                }
                operand = 1;
                abort.execute();
            }
            
            instructionCounter++;
        }
    };
    
    private Executable branch = new Executable() {
        
        /**
         *  this is an unconditional transition operator
         */
        @Override
        public void execute() {
            instructionCounter = operand;
        }
    };
    
    private Executable branchZero = new Executable() {
        
        /**
         *  this is an transition operator that goes to current operand line if accumulator is 0
         *  and otherwise goes to the next line
         */
        @Override
        public void execute() {
            if (accumulator == 0) {
                instructionCounter = operand;
            } else {
                instructionCounter++;
            }
        }
    };
    
    private Executable branchNegative = new Executable() {
        
        /**
         *  this is an transition operator that goes to current operand line if accumulator is negative
         *  and otherwise goes to the next line
         */
        @Override
        public void execute() {
            if (accumulator < 0) {
                instructionCounter = operand;
            } else {
                instructionCounter++;
            }
        }
    };
}
