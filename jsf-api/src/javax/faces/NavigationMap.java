/*
 * $Id: NavigationMap.java,v 1.2 2002/01/24 18:33:33 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * An interface used to define an object which can be used to 
 * house navigation mappings for an application.  An implementation
 * of this interface could be used in conjunction with a 
 * NavigationHandler implementation.  NavigationMap implementations 
 * may also load data from a configuration file from within the 
 * implementation's constructor.
 */
public interface NavigationMap {

    /**
     * Associates a commandName and outcomeName with a particular 
     * navigational action and path.  When a command listener passes 
     * this outcomeName to the <code>handleCommandOutcome, 
     * handleCommandSuccess,</code> or <code>handleCommandException</code>
     * methods, the target action and path will be set to those specified
     * in this method.
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @param actionCode integer describing the type of action to be taken
     * @param path String containing the path to be associated with the outcome
     *        if the actionCode is either FORWARD or REDIRECT
     * @throws NullPointerException if commandName, outcomeName is null
     * @throws FacesException if entry already exists
     */
    public void put(String commandName, String outcomeName, 
        int actionCode, String path) throws FacesException;

    /**
     * Removes the navigational outcome mapping associated with the specified
     * commandName and outcomeName.
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @throws NullPointerException if commandName or outcomeName is null
     * @throws FacesException if commandName, outcomeName 
     *         combination does not exist in map
     */
    public void remove(String commandName, String outcomeName) 
        throws FacesException;

    /**
     * Returns the current target action, one of:
     * <ul>
     * <li><code>NavigationHandler.REDIRECT</code>
     * <li><code>NavigationHandler.FORWARD</code>
     * <li><code>NavigationHandler.PASS</code>
     * </ul>
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @return integer corresponding to current target action
     */
    public int getTargetAction(String commandName, String outcomeName);

    /**
     * Returns a string containing the current target path.
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @return String containing the current target path.
     */
    public String getTargetPath(String commandName, String outcomeName);

}
