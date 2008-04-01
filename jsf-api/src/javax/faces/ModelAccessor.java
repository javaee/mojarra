/*
 * $Id: ModelAccessor.java,v 1.3 2001/12/20 22:25:45 ofung Exp $
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
import javax.faces.ObjectTable;
import javax.faces.RenderContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 * This class incorporates the methods for setting values in
 * model objects, as well as getting model object values.
 */ 
public class ModelAccessor {

    /**
     * PRECONDITION: ObjectTable exists in Application Scope.  The
     * 'model reference' string references a valid model bean instance
     * existing in the ObjectTable.  Note that for nested beans,
     * the nested bean is also instantiated inside the container bean.
     *
     * POSTCONDITION: The property value is set, where the property
     * is identified by the model reference string.  The model bean
     * with the new value is put back into the ObjectTable.
     *
     * @param rc The RenderContext containing the current session.
     * @param modelReference A string referencing a bean's property.
     * @param value The value of the property to be set.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the ObjectTable,
     *     or the property value could not be set.
     */
    public static void setModelObject(RenderContext rc, 
        String modelReference, Object value) throws FacesException {

        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(modelReference);

        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        ObjectTable objectTable;

        HttpSession session = rc.getSession();
        Assert.assert_it(null != session);

        objectTable = (ObjectTable)session.getServletContext()
            .getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it(null != objectTable);

        // If the reference string begins with a "$" (ex:
        // $user.address.street), then the string is to be 
        // interpreted as 'a user bean which contains an address
        // bean which has a "street" property'.
        //
        if (modelReference.startsWith("$")) {
            expression = modelReference.substring(1);
            property = expression.substring((expression.indexOf(".")+1));
            baseName = expression.substring(0, expression.indexOf("."));
            object = objectTable.get(session, baseName);
            if (object == null) {
                throw new FacesException("Named Object: '"+baseName+
                    "' not found in ObjectTable.");
            }

            try {
                PropertyUtils.setNestedProperty(object, property, value);
                objectTable.put(session, baseName, object);
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
        // a model bean existing in the ObjectTable with this
        // name.
        //
        } else {
            object = objectTable.get(session, modelReference);
            if (object == null) {
                throw new FacesException("Named Object: '"+modelReference+
                    "' not found in ObjectTable.");
            }
            object = value;
            objectTable.put(session, modelReference, object); 
        }
    }

    /**
     * PRECONDITION: ObjectTable exists in Application Scope.  The
     * 'model reference' string references a valid model bean instance
     * existing in the ObjectTable.  Note that for nested beans,
     * the nested bean is also instantiated inside the container bean.
     *
     * POSTCONDITION: An object is returned as identified by  
     * the model reference string.
     *
     * @param rc The RenderContext containing the current session.
     * @param modelReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the ObjectTable,
     *     or the property value could not be retrieved.
     */
    public static Object getModelObject(RenderContext rc, 
        String modelReference) throws FacesException {
        
        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        Object returnObject = null;
        ObjectTable objectTable;

        HttpSession session = rc.getSession();
        Assert.assert_it(null != session);

        objectTable = (ObjectTable)session.getServletContext()
            .getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it(null != objectTable);

        if (modelReference.startsWith("$")) {
            expression = modelReference.substring(1);
            property = expression.substring((expression.indexOf(".")+1));
            baseName = expression.substring(0, expression.indexOf("."));

            object = objectTable.get(session, baseName);
            if (object == null) {
                throw new FacesException("Named Object: '"+baseName+
                    "' not found in ObjectTable.");
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
            returnObject = objectTable.get(session, modelReference);
            if (returnObject == null) {
                throw new FacesException("Named Object: '"+modelReference+
                    "' not found in ObjectTable.");
            }
        }

        return returnObject;
    }
}
