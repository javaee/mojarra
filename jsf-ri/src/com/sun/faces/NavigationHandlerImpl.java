/*
 * $Id: NavigationHandlerImpl.java,v 1.1 2002/01/25 18:45:16 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NavigationHandlerImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.NavigationHandler;
import javax.faces.FacesException;
import javax.faces.NavigationMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 *  <B>NavigationHandlerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NavigationHandlerImpl.java,v 1.1 2002/01/25 18:45:16 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class NavigationHandlerImpl implements NavigationHandler
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String outcome = null;
    private int targetAction;
    private String targetPath = null;
    private NavigationMap navigationMap = null;
   
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public NavigationHandlerImpl(NavigationMap navMap)
    {
        ParameterCheck.nonNull(navMap);
        this.navigationMap = navMap;
    }

    //
    // Methods from NavigationHandler
    //

    //
    // Class methods
    //

    //
    // General Methods
    //
    public void handleCommandOutcome(String commandName, String outcomeName) {

        ParameterCheck.nonNull(commandName);
        Assert.assert_it(navigationMap != null);

        targetAction = navigationMap.getTargetAction(commandName, 
                outcomeName);
        targetPath = navigationMap.getTargetPath(commandName, 
                outcomeName);
    }    

    public void handleCommandSuccess(String commandName) {

        ParameterCheck.nonNull(commandName);
        Assert.assert_it(navigationMap != null);

        targetAction = navigationMap.getTargetAction(commandName, 
                Constants.OUTCOME_SUCCESS);
        targetPath = navigationMap.getTargetPath(commandName, 
                Constants.OUTCOME_SUCCESS);
    }

    public void handleCommandException(String commandName, Exception e) {

        ParameterCheck.nonNull(commandName);
        Assert.assert_it(navigationMap != null);

        targetAction = navigationMap.getTargetAction(commandName, 
                Constants.OUTCOME_FAILURE);
        targetPath = navigationMap.getTargetPath(commandName, 
                Constants.OUTCOME_FAILURE);
        // PENDING ( visvan ) shouldn't we throw this exception ??
        // otherwise why do we need it ?
    }

    public void setTarget(int actionCode, String path) {
        // PENDING ( visvan ) what does setTarget do ??
    }    

    public int getTargetAction() {
        return targetAction;
    }    

    public String getTargetPath() {
        return targetPath;
    }    
   
    // PENDING ( visvan )  if this method returns a status to indicate
    // if navigation succeeded or not, then the code to do navigation
    // can be moved here from FacesFilter.
    public void service(ServletRequest request, ServletResponse response) {
    }    
    
} // end of class NavigationHandlerImpl
