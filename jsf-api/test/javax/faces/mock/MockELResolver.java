/*
 * $Id: MockELResolver.java,v 1.2 2005/08/22 22:08:23 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
