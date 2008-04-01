/*
 * $Id: NavigationMapImpl.java,v 1.1 2002/01/24 18:38:17 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import java.util.Hashtable;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.NavigationHandler;
import javax.faces.NavigationMap;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

public class NavigationMapImpl implements NavigationMap {

    // Relationship Instance Variables

    private Hashtable ht;

    /**
     * Constructor creates new data structure.
     */
    public NavigationMapImpl() {
        ht = new Hashtable();
    }

    /**
     * Adds a new entry to the NavigationMap. 
     * Required parameters are <code>commandName</code>, 
     * <code>outcomeName</code>, and <code>actionCode</code>.
     * <code>actionCode</code> should be one of the following:
     * <ul>
     * <li><code>NavigationHandler.REDIRECT</code>
     * <li><code>NavigationHandler.FORWARD</code>
     * <li><code>NavigationHandler.PASS</code>
     * </ul> 
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @param actionCode integer describing the type of action to be taken
     * @param path String containing the path to be associated with the outcome
     * @throws FacesException if entry already exists
     */
    public void put(String commandName, String outcomeName,
        int actionCode, String path) throws FacesException {

        ParameterCheck.nonNull(commandName);
        ParameterCheck.nonNull(outcomeName);

        if (outcomeName != Constants.OUTCOME_SUCCESS &&
            outcomeName != Constants.OUTCOME_FAILURE) {
            throw new FacesException("Invalid outcomeName parameter.");
        }
        if (actionCode < NavigationHandler.FORWARD ||
            actionCode > NavigationHandler.PASS) {
            throw new FacesException("Invalid actionCode parameter.");
        }
        
        // Construct key for table storage..
        //
        String key = constructKey(commandName, outcomeName);
        if (ht.containsKey(key)) {
            throw new FacesException("NavigationMap insertion failed: Entry already exists.");
        }

        // Set up values for table storage..
        //
        ActionTarget actionTarget = new ActionTarget(actionCode, path);

        ht.put(key, actionTarget);
    }

    /**
     * Removes an entry from the NavigationMap.
     * Required parameters are <code>commandName</code>,
     * <code>outcomeName</code>.
     *
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @throws FacesException if commandId, outcomeName
     *         combination does not exist in map
     */
    public void remove(String commandName, String outcomeName)
        throws FacesException {

        ParameterCheck.nonNull(commandName);
        ParameterCheck.nonNull(outcomeName);

        // Construct key for table storage..
        //
        String key = constructKey(commandName, outcomeName);

        if (!ht.containsKey(key)) {
            throw new FacesException("Key:" + key + 
                " does no exist in the NavigationMap.");
        }
        ht.remove(key);
    }

    /**
     * Returns the current target action.  The target action will be one
     * of:
     * <ul>
     * <li><code>NavigationHandler.REDIRECT</code>
     * <li><code>NavigationHandler.FORWARD</code>
     * <li><code>NavigationHandler.PASS</code>
     * </ul>
     * The value return will be '-1' if the entry is not found.
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @return integer corresponding to current target action
     */
    public int getTargetAction(String commandName, String outcomeName) {
    
        ParameterCheck.nonNull(commandName);
        ParameterCheck.nonNull(outcomeName);

        // Construct key for table lookup..
        //
        String key = constructKey(commandName, outcomeName);

        if (!ht.containsKey(key)) {
            return -1;
        }

        ActionTarget actionTarget = (ActionTarget) ht.get(key);

        return actionTarget.getActionCode(); 
    }

    /**
     * Returns a string containing the target path.  If the entry is not
     * found in the NavigationMap, null is returned.
     * @param commandName String containing the name of the command.
     * @param outcomeName String containing the name of the command's outcome
     * @return String containing the current target path.
     */
    public String getTargetPath(String commandName, String outcomeName) {

        ParameterCheck.nonNull(commandName);
        ParameterCheck.nonNull(outcomeName);

        // Construct key for table lookup..
        //
        String key = constructKey(commandName, outcomeName);

        if (!ht.containsKey(key)) {
            return null;
        }

        ActionTarget actionTarget = (ActionTarget) ht.get(key);

        return actionTarget.getTargetPath();
    }

    private String constructKey(String commandName, String outcomeName) {
        StringBuffer sb = new StringBuffer();
        sb.append(commandName);
        sb.append(outcomeName);
        return sb.toString();
    }

    // Inner Class

    public class ActionTarget {

        private int actionCode;
        private String targetPath;

        public ActionTarget(int actionCode, String targetPath) {
            this.actionCode = actionCode;
            this.targetPath = targetPath;
        }

        public int getActionCode() {
            return actionCode;
        }

        public String getTargetPath() {
            return targetPath;
        }
    }
}
