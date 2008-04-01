/*
 * $Id: BeanAccessor.java,v 1.3 2002/04/05 19:40:15 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.lang.ExceptionInInitializerError;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.SecurityException;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.FacesContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

/**
 * This class incorporates the methods for setting values in
 * model objects, as well as getting model object values.
 */ 
public class BeanAccessor implements ObjectAccessor {

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private FacesContext facesContext;

//
// Constructors and Initializers    
//

public BeanAccessor(FacesContext yourFacesContext)
{
    super();
    facesContext = yourFacesContext;
}


    /**
     * PRECONDITION: ObjectManager exists in Application Scope.  The
     * 'model reference' string references a valid model bean instance
     * existing in the ObjectManager.  Note that for nested beans,
     * the nested bean is also instantiated inside the container bean.
     *
     * POSTCONDITION: The property value is set, where the property
     * is identified by the model reference string.  The model bean
     * with the new value is put back into the ObjectManager.
     *
     * @param request ServletRequest object representing the client request
     * @param modelReference A string referencing a bean's property.
     * @param value The value of the property to be set.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the ObjectManager,
     *     or the property value could not be set.
     */
    public void setObject(ServletRequest request, String objectReference,  
			  Object value) throws FacesException {

        // ParameterCheck.nonNull(request);
        // ParameterCheck.nonNull(objectReference);
	// Assert.assert_it(null != facesContext);

        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        ObjectManager objectManager = facesContext.getObjectManager();
        // Assert.assert_it(null != objectManager);

        // If the reference string begins with a "$" (ex:
        // $user.address.street), then the string is to be 
        // interpreted as 'a user bean which contains an address
        // bean which has a "street" property'.
        //
        if (objectReference.startsWith("$")) {
            expression = objectReference.substring(1);
            property = expression.substring((expression.indexOf(".")+1));
            baseName = expression.substring(0, expression.indexOf("."));
            object = objectManager.get(request, baseName);
            if (object == null) {
                throw new FacesException("Named Object: '"+baseName+
                    "' not found in ObjectManager.");
            }

            try {
                PropertyUtils.setNestedProperty(object, property, value);
            } catch (IllegalAccessException iae) {
                throw new FacesException(iae.getMessage());
            } catch (InvocationTargetException ite) {
                throw new FacesException(ite.getMessage());
            } catch (NoSuchMethodException nme) {
                throw new FacesException(nme.getMessage());
            }
//PENDING (ROGERK) - Not Sure About This Part (below)....???? 
        // Otherwise, treat the reference string as a 'literal'
        // name for the model bean itself.  There should be 
        // a model bean existing in the ObjectManager with this
        // name.
        //
        } else {
            object = objectManager.get(request, objectReference);
            if (object == null) {
                throw new FacesException("Named Object: '"+objectReference+
                    "' not found in ObjectManager.");
            }
            object = value;
            javax.servlet.http.HttpServletRequest httpRequest = 
                    (javax.servlet.http.HttpServletRequest) request;
            objectManager.put(httpRequest.getSession(),objectReference,object); 
        }
    }

    /**
     * PRECONDITION: ObjectManager exists in Application Scope.  The
     * 'model reference' string references a valid model bean instance
     * existing in the ObjectManager.  Note that for nested beans,
     * the nested bean is also instantiated inside the container bean.
     *
     * POSTCONDITION: An object is returned as identified by  
     * the model reference string.
     *
     * @param request ServletRequest object representing the client request
     * @param objectReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the ObjectManager,
     *     or the property value could not be retrieved.
     */
    public Object getObject(ServletRequest request, 
			    String objectReference) throws FacesException {
        // ParameterCheck.nonNull(request);
	// ParameterCheck.nonNull(objectReference);
	// Assert.assert_it(null != facesContext);
	
        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        Object returnObject = null;
        ObjectManager objectManager = facesContext.getObjectManager();
	// Assert.assert_it(null != objectManager);

        if (objectReference.startsWith("$")) {
            expression = objectReference.substring(1);
            property = expression.substring((expression.indexOf(".")+1));
            baseName = expression.substring(0, expression.indexOf("."));

            object = objectManager.get(request, baseName);
            if (object == null) {
                throw new FacesException("Named Object: '"+baseName+
                    "' not found in ObjectManager.");
            }
            try {
                returnObject = BeanUtils.getNestedProperty(object, property);
            } catch (IllegalAccessException iae) {
                throw new FacesException(iae.getMessage());
            } catch (InvocationTargetException ite) {
                throw new FacesException(ite.getMessage());
            } catch (NoSuchMethodException nme) {
                throw new FacesException(nme.getMessage());
            }
// PENDING (ROGERK) Just return from object table???
        } else {
            returnObject = objectManager.get(request, objectReference);
            if (returnObject == null) {
                throw new FacesException("Named Object: '"+objectReference+
                    "' not found in ObjectManager.");
            }
        }

        return returnObject;
    }
}
