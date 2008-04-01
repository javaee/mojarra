/*
 * $Id: NavigationMap.java,v 1.1 2002/01/18 21:56:22 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A class which implements a NavigationHandler object which
 * maps outcome names to specific target actions and paths.
 */
public class NavigationMap implements NavigationHandler {

    /**
     * Outcome name indicating successful command execution.
     */
    public static final String OUTCOME_SUCCESS = "Success";

    /**
     * Outcome name indicating failed command execution.
     */
    public static final String OUTCOME_FAILURE = "Failure";

    /**
     * Associated an outcomeName with a particular navigational action
     * and path.  When a command listener passes this outcomeName to 
     * the <code>handleCommandOutcome, handleCommandSuccess,</code> 
     * or <code>handleCommandException</code>
     * methods, the target action and path will be set to those specified
     * in this method.
     * @param outcomeName String containing the name of the command's outcome
     * @param actionCode integer describing the type of action to be taken
     * @param path String containing the path to be associated with the outcome
     *        if the actionCode is either FORWARD or REDIRECT
     * @throws NullPointerException if outcomeName or actionCode is null
     */
    public void put(String outcomeName, int actionCode, String path) {
    }

    /**
     * Removes the navigational outcome mapping associated with the specified
     * outcomeName.
     * @param outcomeName String containing the name of the command's outcome
     * @throws NullPointerException if outcomeName is null
     * @throws FacesException if outcomeName does not exist in map
     */
    public void remove(String outcomeName) {
    } 

    /**
     * Invoked to associate a particular outcome with the execution of
     * the command specified by commandName.  
     * @param commandName String containing the name of the command
     * @param outcomeName String containing a name which describes the outcome
     *        of the command
     */
    public void handleCommandOutcome(String commandName, String outcomeName) {
    }

    /**
     * Invoked to indicate the command specified by commandName 
     * executed successfully.
     * @param commandName String containing the name of the command
     */
    public void handleCommandSuccess(String commandName) {
    }

    /**
     * Invoked to indicate the command specified by commandName 
     * raised an exception during execution.
     * @param commandName String containing the name of the command
     * @param e Exception raised during command execution
     */
    public void handleCommandException(String commandName, Exception e) {
    }

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
    public void setTarget(int actionCode, String path) {
    }

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
    public int getTargetAction() {
	return 0;
    }

    /**
     * @see #setTarget
     * @return String containing the current target path.
     */
    public String getTargetPath() {
	return null; //compile
    }

    /**
     * Processes the request/response pair to effect the current
     * navigational action for this request.  
     * @param request ServletRequest object corresponding to this request
     * @param response ServletResponse object used to write the response
     */
    public void service(ServletRequest request, ServletResponse response) {
    }
}
