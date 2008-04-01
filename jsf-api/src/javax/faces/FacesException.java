package javax.faces;

/**
 * The class which encapsulates general JavaServer Faces exceptions.
 */
public class FacesException extends Exception {

    protected String xcptMessage;

    /**
    * Constructs a FacesException with a detailed message.
    *
    * @param message
    *   Detailed message for this exception.
    */
    public FacesException(String message) {
        super(message);
        xcptMessage = message;
    }

    /**
    * Returns the message of this exception
    * @return the message string.
    */
    public String getMessage() {
        return xcptMessage;
    }
}
