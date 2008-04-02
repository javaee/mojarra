/*
 * $Id: ValueBindingImpl.java,v 1.1 2003/03/24 19:45:29 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;

import org.mozilla.util.ParameterCheck;

public class ValueBindingImpl extends ValueBinding
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

// Relationship Instance Variables

    protected String ref = null;

    protected VariableResolver variableResolver = null;
    protected PropertyResolver propertyResolver = null;

//
// Constructors and Initializers    
//

    public ValueBindingImpl(VariableResolver newVar, 
			    PropertyResolver newProp) {
	ParameterCheck.nonNull(newVar);
	ParameterCheck.nonNull(newProp);
	variableResolver = newVar;
	propertyResolver = newProp;
    }

//
// Class methods
//

//
// General Methods
//

    public void setRef(String newRef) {
	reset();
	ParameterCheck.nonNull(newRef);
	ref = newRef;
    }

    public void reset() {
	ref = null;
    }

//
// Methods from ValueBinding
//

    public Object getValue(FacesContext context)
        throws PropertyNotFoundException {
	return null;
    }
	       
    public void setValue(FacesContext context, Object value)
        throws PropertyNotFoundException {
    }

    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
	return false;
    }

    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
	return null;
    }

} // end of class ValueBindingImpl
