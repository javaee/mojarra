/*
 * $Id: MockELResolver.java,v 1.1 2005/05/05 20:51:16 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.ELException;

/**
 * <p>Mock implementation of <code>ELResolver</code> that supports a
 * limited subset of expression evaluation functionality:</p>
 *
 * <ul>
 * <li>Recognizes <code>applicationScope</code>, <code>requestScope</code>,
 *     and <code>sessionScope</code> implicit names.</li>
 * <li>Searches in ascending scopes for non-reserved names.</li>
 * </ul>
 */

public class MockELResolver extends ELResolver {

    private MockVariableResolver variableResolver = null;

    private MockPropertyResolver propertyResolved = null;
   


    // ------------------------------------------------------------ Constructors
    public MockELResolver() {
	variableResolver = new MockVariableResolver();
	propertyResolved = new MockPropertyResolver();
    }

    public Object getValue(ELContext context, Object base, Object property)
        throws ELException {
	Object result = null;
	FacesContext facesContext = (FacesContext) 
	    context.getContext(FacesContext.class);
	
	if (null == base) {
	    String name = (null != property) ? property.toString() : null;
	    try {
		result = variableResolver.resolveVariable(facesContext, name);
	    }
	    catch (Throwable e) {
		throw new ELException(e);
	    }
	}
	else {
	    try {
		result = propertyResolved.getValue(base, property);
	    }
	    catch (Throwable e) {
		throw new ELException(e);
	    }
	}

	return result;
    }

    public Class getType(ELContext context, Object base, Object property)
        throws ELException {
	Class result = null;

	return result;
    }

    public void setValue(ELContext context, Object base, Object property,
			 Object value) throws ELException {
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws ELException {
	boolean result = false;
	return false;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
	return null;
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
	Class result = null;
	
	return result;
    }
    
   



}
