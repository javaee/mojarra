package javax.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * An interface used to define an object which can be used during
 * the event processing phase of a request to allow application
 * event handling logic (command listeners) to determine the
 * navigational result of processing events originating from the
 * request. 
 * <p>
 * A NavigationHandler instance is obtained from the event context.
 * @see javax.faces.EventContext#getNavigationHandler
 * <p>
 * It is also passed directly to the <code>doCommand</code> method
 * on <code>CommandListener</code>.
 * Command listeners should configure the NavigationHandler object
 * appropriately during command processing.
 * @see javax.faces.EventContext#getNavigationHandler
 */
public interface NavigationHandler {

    /**
     * actionCode corresponding to an undefined navigational action
     */
    public static final int UNDEFINED = 0;

    /**
     * actionCode corresponding to a forward navigational action
     */
    public static final int FORWARD = 1;

    /**
     * actionCode corresponding to a redirect navigational action
     */
    public static final int REDIRECT = 2;

    /**
     * actionCode corresponding to a pass navigational action.
     * [does this mean continue processing current request?]
     * PENDING(Aim): not sure this is the correct interpretation
     */
    public static final int PASS = 3;

    /**
     * Invoked to associate a particular outcome with the execution of
     * the command specified by commandName.  
     * @param commandName String containing the name of the command
     * @param outcomeName String containing a name which describes the outcome
     *        of the command
     */
    void handleCommandOutcome(String commandName, String outcomeName);

    /**
     * Invoked to indicate the command specified by commandName 
     * executed successfully.
     * @param commandName String containing the name of the command
     */
    void handleCommandSuccess(String commandName);

    /**
     * Invoked to indicate the command specified by commandName 
     * raised an exception during execution.
     * @param commandName String containing the name of the command
     * @param e Exception raised during command execution
     */
    void handleCommandException(String commandName, Exception e);

    /**
     * Invoked to set an explicit navigational result.
     * <code>actionCode</code> should be one of the following:
     * <ul>
     * <li><code>NavigationHandler.REDIRECT</code>
     * <li><code>NavigationHandler.FORWARD</code>
     * <li><code>NavigationHandler.PASS</code>
     * </ul>
     * @param actionCode integer describing the navigation action to be taken
     * @param path String containing the navigation path
     * @throws IllegalParameterException if actionCode isn't REDIRECT,
     *         FORWARD, or PASS
     */
    void setTarget(int actionCode, String path);

    /**
     * Returns the current target action, one of:
     * <ul>
     * <li><code>NavigationHandler.REDIRECT</code>
     * <li><code>NavigationHandler.FORWARD</code>
     * <li><code>NavigationHandler.PASS</code>
     * </ul>
     * @see #setTarget
     * @return integer corresponding to current target action
     */
    int getTargetAction(); 

    /**
     * @see #setTarget
     * @return String containing the current target path.
     */
    String getTargetPath();      // returns path for redirect or forward

    /**
     * Processes the request/response pair to effect the current
     * navigational action for this request.  This method is invoked
     * by JSF after all command events have been processed.
     * @param request ServletRequest object corresponding to this request
     * @param response ServletResponse object used to write the response
     */
    void service(ServletRequest request, ServletResponse response);
}
