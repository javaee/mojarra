/*
 * $Id: OutcomeTag.java,v 1.1 2002/01/25 18:45:19 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OutcomeTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.Constants;
import javax.faces.NavigationMap;
import javax.faces.NavigationHandler;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>OutcomeTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OutcomeTag.java,v 1.1 2002/01/25 18:45:19 visvan Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class OutcomeTag extends TagSupport
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

    // Attribute Instance Variables
    private String commandName = null;
    private String targetAction = null;
    private String targetPath = null;
    private String outcome = null;
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public OutcomeTag()
    {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init()
    {
        // super.init();
    }

    
    //
    // Class methods
    //

    //
    // General Methods
    //

    public int doStartTag() throws JspException{

        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectManager from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        
        // get the Id of the navigationMap from the ancestor tag
        // navigationMap.
        NavigationMapTag ancestor = null;
        try {
            ancestor = (NavigationMapTag) findAncestorWithClass(this,
                    NavigationMapTag.class);
            String nav_id = ancestor.getId();
            if ( nav_id != null ) {
                NavigationMap navigationMap = (NavigationMap)
                    ot.get(pageContext.getRequest(), nav_id);
                // navigationMap object must exist. It gets created when
                // NavigationMap tag is hit.
                Assert.assert_it(navigationMap != null ); 
                populateNavigationMap(navigationMap);
            } 
        } catch ( Exception e ) {
           // If form tag cannot be found, then we cannot obtain 
           // navigationMap object.
           // PENDING ( visvan ) log this exception
        }
        return(EVAL_BODY_INCLUDE);
    }

    /**
     * Populates the navigationMap after converting the parameters
     * to correct format as required by the navigationMap.
     */
    private void populateNavigationMap(NavigationMap navigationMap) {
        
        int target_action = 0;
        if ( outcome.equals(Constants.OUTCOME_SUCCESS )) {
            outcome = Constants.OUTCOME_SUCCESS;
        } else if ( outcome.equals(Constants.OUTCOME_FAILURE )) {
              outcome = Constants.OUTCOME_FAILURE;
        }  
        
        if ( targetAction.equals(Constants.REF_FORWARD )) {
            target_action = NavigationHandler.FORWARD;
        } else if ( outcome.equals(Constants.REF_REDIRECT )) {
              target_action = NavigationHandler.REDIRECT;
        } else if ( outcome.equals(Constants.REF_PASS )) {
              target_action = NavigationHandler.PASS;
        }
        // PENDING ( visvan ) support UNDEFINED ?
        // map implementation should make sure that
        // the entry doesn't exist already.
        try {
            // if any of the paramters is not valid an exception will
            // thrown.
            navigationMap.put(commandName, outcome, target_action, targetPath);  
        } catch ( FacesException e ) {
            // PENDING ( visvan ) log this exception
       }
    }
    
    /**
     * Returns the value of commandName attribute
     *
     * @return String value of commandName attribute
     */
    public String getCommandName() {
        return this.commandName;
    }

    /**
     * Sets commandName attribute
     * @param cmd value of commandName attribute 
     */
    public void setCommandName(String cmd) {
        this.commandName = cmd;
    }

    /**
     * Returns the value of outcome attribute
     *
     * @return String value of outcome attribute
     */
    public String getOutcome() {
        return this.outcome;
    }

    /**
     * Sets outcome attribute
     * @param oc value of outcome attribute
     */
    public void setOutcome(String oc) {
        this.outcome = oc;
    }

    /**
     * Returns the value of targetAction attribute
     *
     * @return String value of  targetAction attribute
     */
    public String getTargetAction() {
        return this.targetAction;
    }

    /**
     * Sets targetAction attribute
     * @param action value of targetAction attribute
     */
    public void setTargetAction(String action) {
        this.targetAction = action;
    }
    
    /**
     * Returns the value of targetPath attribute
     *
     * @return String value of  targetPath attribute
     */
    public String getTargetPath() {
        return this.targetPath;
    }

    /**
     * Sets targetPath attribute
     * @param path value of targetPath attribute
     */
    public void setTargetPath(String path) {
        this.targetPath = path;
    }

   

} // end of class OutcomeTag
