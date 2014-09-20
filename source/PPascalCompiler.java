package code;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * @author Jupo
 * 
 *      This is a compiler for PPascal language to Prostotron Programming Language
 * It provides basic functionality such as loops, arrays, functions, switches, if statements,
 * goto commands, complex expressions.
 *
 */
public class PPascalCompiler extends Compiler {
    
    // Computer.MEMORY_SIZE - 1 is an auxiliary computing memory
    private int lastMemory = Computer.MEMORY_SIZE - 2;
    private TableEntry temp = new TableEntry(TableEntry.VARIABLE, "+000000", +000000, 9999);
    private int commandMemory = 0000;
    
    private Messenger<Integer> computerMessages = new Messenger<Integer>();
    
    private Computer computer = new Computer(computerMessages);
    
    private int[] memory = new int[Computer.MEMORY_SIZE];
    
    private ArrayList<TableEntry> memoryTable = new ArrayList<TableEntry>();
    
    int flags[] = new int[100];
    
    public static void main(String[] args) {
        
        operationsPriority.put("^", 1);
        operationsPriority.put("%", 2);
        operationsPriority.put("*", 2);
        operationsPriority.put("/", 2);
        operationsPriority.put("+", 3);
        operationsPriority.put("-", 3);
        
        System.out.println(createPostfix("50-4*(6-10^11)+12*12*12+3-2/7/7*(2*2)"));
    }
    
    public void startCompiler() {
        
    }
    
    public void writeFile() {
        
    }
    
    public void createArray() {
        
    }
    
    public void createLoop() {
        
    }
    
    public void getVaribleMemory(String variable) {
        
    }
    
    /**
     *  Function that checks if memoryTable contains such element and returns his memory location
     *  and otherwise returns -1.
     */
    public TableEntry getFromMemory(int type, String name) {
        
        int size = memoryTable.size();
        
        for (int i = 0; i < size; i++) {
            if (memoryTable.get(i).type() == type) {
                if (memoryTable.get(i).name().equals(name)) {
                    return memoryTable.get(i);
                }
            }
        }
        
        return null;
    }
    
    public int calculatePostfix(String postfix) {
        
        Stack<TableEntry> stack = new Stack<TableEntry>();
        
        postfix +="\0";
        
        int i = 0;
        char sym = postfix.charAt(i);
        while (sym != '\0') {
            
            sym = postfix.charAt(i);
            
            if (!isOperation(sym) && sym != ' ') {
                
                String name = "";
                boolean variable = false;
                
                while (sym != ' ') {
                    if (!isDigit(sym)) variable = true;
                    i++;
                    sym = postfix.charAt(i);
                }
                
                TableEntry answer;
                
                if (variable) {
                    answer = getFromMemory(TableEntry.VARIABLE, name);
                    if (answer == null) return 1;
                } else {
                    answer = getFromMemory(TableEntry.CONST, name);
                    if (answer != null) {
                        stack.push(answer);
                    } else {
                        TableEntry ent = new TableEntry(TableEntry.CONST, name, Integer.parseInt(name), lastMemory);
                        memoryTable.add(ent);
                        answer = ent;
                        lastMemory--;
                    }
                }
                
                stack.push(answer);
            } else if (sym != ' ') {
                TableEntry e1 = stack.pop();
                TableEntry e2 = stack.pop();
                switch (sym) {
                    case '+' :
                        memory[commandMemory] = 210000 + e1.getMemoryLocation();
                        memory[commandMemory + 1] = 310000 + e2.getMemoryLocation();
                        memory[commandMemory + 2] = 229999;
                    case '-' :
                        memory[commandMemory] = 210000 + e1.getMemoryLocation();
                        memory[commandMemory + 1] = 320000 + e2.getMemoryLocation();
                        memory[commandMemory + 2] = 229999;
                    case '*' :
                        memory[commandMemory] = 210000 + e1.getMemoryLocation();
                        memory[commandMemory + 1] = 330000 + e2.getMemoryLocation();
                        memory[commandMemory + 2] = 229999;
                    case '/' :
                        memory[commandMemory] = 210000 + e1.getMemoryLocation();
                        memory[commandMemory + 1] = 340000 + e2.getMemoryLocation();
                        memory[commandMemory + 2] = 229999;
                }
                stack.push(paramE);
            }
        }
        
        return 0;
    }
}
