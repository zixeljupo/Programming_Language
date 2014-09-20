package code;

/**
 * 
 * @author Jupo
 * 
 * This class send messages to another messenger, whose getMessage method should be overridden.
 * 
 */
public class Messenger<MessageType> {
    
    private Messenger<MessageType> ms;
    
    public Messenger() {
        this(null);
    }
    
    public Messenger(Messenger<MessageType> ms) {
        this.ms = ms;
    }
    
    public void initMessenger(Messenger<MessageType> ms) {
        this.ms = ms;
    }
    
    public void sendMessage(MessageType message) {
        if (ms != null) {
            ms.getMessage(message);
        } else throw new NullPointerException("null messenger");
    }
    
    public void getMessage(MessageType message) {
        
    }
    
}
