package jsf2.demo.scrum.web.controller;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public class ManagerException extends Exception {

    private static final long serialVersionUID = 1L;

    public ManagerException(Throwable cause) {
        super(cause);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
