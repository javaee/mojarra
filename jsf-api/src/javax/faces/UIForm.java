/*
 * $Id: UIForm.java,v 1.9 2002/04/02 01:24:39 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.ServletRequest;
import java.util.EventObject;

/**
 * Class for representing a form user-interface component. 
 * A form encapsulates the process of taking a collection of
 * input from the user and submitting it to the application
 * in a single &quot;submit&quot; operation.  A &quot;form control&quot;
 * is a user-interface component added as a descendent to the form
 * for the purpose of:
 * <ol>
 * <li>collecting data from the user
 * <li>displaying form data to the user
 * <li>executing a form command (submit, navigate, etc):
 * </ol>
 * @see UISelectBoolean
 * @see UISelectOne
 * @see UITextEntry
 * @see UIOutput
 * @see UICommand  
 */
public class UIForm extends UIComponent { 

    private static String TYPE = "Form";
    
    // PENDING ( visvan ) added per discussion with Amy on NavigationHandler
    private String navigationMapId = null;
    
    /** 
     * Returns a String representing the form's type.  
     *
     * @return a String object containing &quot;Form&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }
    
    /**
     * Registers the specified id as a navigationMap object
     * for this form.  The specified navigationMap id must be registered
     * in the scoped namespace and it must implement the <code>
     * NavigationMap</code> interface, else an exception will  be thrown.
     *
     * @see NavigationMap
     * @param navMapId the id of the NavigationMap
     * @throws FacesException if navMapId is not registered in the
     *         scoped namespace or if the object referred to by navMapId
     *         does not implement the <code>NavigationMap</code> interface.
     */
    public void setNavigationMapId(String navMapId) {
        // PENDING ( visvan ) add FacesException and assertions as per javadoc.        
        navigationMapId = navMapId;            
    }
    
    /** 
     * @return String containing the navigationMapId
     */
    public String getNavigationMapId() {
        return navigationMapId;
    }  
    
    /** 
     * @param rc the render context used to render this component
     * @return NavigationMap instance represented by navigationMapId.
     */
    public NavigationMap getNavigationMap(RenderContext rc) {
        
        // ParameterCheck.nonNull(rc);
        
        ObjectManager objectManager = rc.getObjectManager();
        // Assert.assert_it( objectManager != null );
        
        NavigationMap navMap = (NavigationMap) 
                        objectManager.get(rc.getRequest(), navigationMapId);
        return navMap;
    }

    /**
     * Registers the specified validator id as a validator
     * for this validatible component.  The specified validator id must be registered
     * in the scoped namespace, else an exception will be thrown.
     * @see Validator
     * @see #removeValidator
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered in the
     *         scoped namespace or if the object referred to by validatorId
     *         does not implement the <code>Validator</code> interface.
     */
    public void addValidator(String validatorId) {
    }

    /**
     * Removes the specified validator id as a validator
     * for this validatible component.
     * @see #addValidator  
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered as a
     *         validator for this component.
     */
    public void removeValidator(String validatorId) {
    }

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    public Iterator getValidators() {
	return null; //compile
    }

    /**
     * The &quot;validState&quot; attribute which describes the current
     * valid state of this component.  Valid state may be one of the
     * following:
     * <ul>
     * <li><code>Validatible.UNVALIDATED</code>
     * <li><code>Validatible.VALID</code>
     * <li><code>Validatible.INVALID</code>
     * </ul>
     * @see #setValidState
     * @return integer containing the current valid state of this
     *         component
     */
    public int getValidState() {
	Integer valid = (Integer)getAttribute(null, "validState");
	return valid != null? valid.intValue() : Validatible.UNVALIDATED;
    }

    /**
     * Sets the &quot;validState&quot; attribute for this component.
     * @see #getValidState
     * @param validState integer containing the valid state of this
     *        component
     * @throws IllegalParameterException if validState is not
     *         UNVALIDATED, VALID, or INVALID
     */
    public void setValidState(int validState) {
        setAttribute("validState", new Integer(validState));
    }

    /**

    * We override this because a form's value should not be set as a
    * local value.

    */

    public Object pullValueFromModel(RenderContext rc) {
	Object result = null;
        if (null == getModelReference()) {
	    return result;
	}

	try {
            result = rc.getObjectAccessor().getObject(rc.getRequest(),
						      getModelReference());
	} catch ( FacesException e ) {
            // PENDING (visvan) skip this exception ??
	}
	return result;
    }

}
