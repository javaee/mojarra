package javax.faces;

public class CommandFailedException extends FacesException {
   /**
    * Constructs a CommandFailedException with a detailed message.
    *
    * @param message
    *   Detailed message for this exception.
    */
    public CommandFailedException(String message) {
        super(message);
    }
}
