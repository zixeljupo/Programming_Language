package code;

/**
 * 
 * @author Jupo
 * 
 *      This is an entry that represents auxiliary compiler array of variables to generate code.
 */
public class TableEntry {
    
    public static final int VARIABLE = 0;
    public static final int CONST = 1;
    public static final int STRING_NUM = 2;
    
    private int type; // data type
    
    private String name; // data name
    
    private int value; // data value
    private int memoryLocation; // memory location of this data
    
    public TableEntry(int type, String name, int value, int memory) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.memoryLocation = memory;
    }
    
    public int type() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String name() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getMemoryLocation() {
        return memoryLocation;
    }
    
    public void setMemoryLocation(int memoryLocation) {
        this.memoryLocation = memoryLocation;
    }
    
    public boolean isConstant() {
        return type == CONST;
    }
    
    public boolean isVariable() {
        return type == VARIABLE;
    }
    
    public boolean isStringNumber() {
        return type == STRING_NUM;
    }
}
