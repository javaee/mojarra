/*
 * $Id: FacesContextImpl.java,v 1.3 2002/06/03 20:08:18 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.Iterator;
import java.util.Locale;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.faces.FacesException;     
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.faces.context.MessageList;
import javax.faces.FactoryFinder;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.LifecycleFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

public class FacesContextImpl extends FacesContext
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
    private ServletContext servletContext = null;
    private Lifecycle lifecycle = null;
    private Locale locale = null;
    private MessageList messageList = null;
    private int phaseId = 0;
    private ServletRequest request = null;
    private Tree requestTree = null;
    private ServletResponse response = null;
    private Tree responseTree = null;
    private HttpSession session = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    // 
    public FacesContextImpl(ServletContext sc, ServletRequest request,
                     ServletResponse response, String lifecycleId)
        throws FacesException {
        
        try {
            ParameterCheck.nonNull(sc);
            ParameterCheck.nonNull(request);
            ParameterCheck.nonNull(response);
            ParameterCheck.nonNull(lifecycleId);
        } catch (Exception e ) {
            throw new FacesException("Cannot create FacesContext." + 
                "One or more input paramters might be null");
        }
        
        this.servletContext = sc;
        this.request = request;
        this.response = response;
        this.locale = request.getLocale();
        if (this.request instanceof HttpServletRequest) {
            this.session =
                ((HttpServletRequest) request).getSession(false);
        }
        // set lifecyle.
        /* PENDING (visvan) set lifecyle once it is implemented
         LifecycleFactory lFactory = (LifecycleFactory)
             FactoryFinder.createFactory
             ("javax.faces.lifecycle.LifecycleFactory");
         this.lifecycle = lFactory.createLifecycle(lifecycleId); */
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from FacesContext
    //
    public Iterator getApplicationEvents() {
        throw new FacesException("UnImplemented");
    }


    public HttpSession getHttpSession() {
        return (this.session);
    }

    public Lifecycle getLifecycle() {
        return (this.lifecycle);
    }


    public Locale getLocale() {
        return (this.locale);
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public MessageList getMessageList() {
        if ( messageList == null ) {
            messageList = new MessageListImpl(this);
        }
        return messageList;
    }
    
    public int getPhaseId() {
        return (this.phaseId);
    }


    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }


    public Tree getRequestTree() {
        return (this.requestTree);
    }


    public void setRequestTree(Tree requestTree) {
        ParameterCheck.nonNull(requestTree);
        this.requestTree = requestTree;
        if (this.responseTree == null) {
            this.responseTree = this.requestTree;
        }
    }


    public Tree getResponseTree() {
        return (this.responseTree);
    }


    public void setResponseTree(Tree responseTree) {
        ParameterCheck.nonNull(responseTree);
        this.responseTree = responseTree;
    }


    public ServletContext getServletContext() {
        return (this.servletContext);
    }


    public ServletRequest getServletRequest() {
        return (this.request);
    }


    public ServletResponse getServletResponse() {
        return (this.response);
    }


    public void addApplicationEvent(FacesEvent event) {
        throw new FacesException("UnImplemented");
    }

    /**
     * PRECONDITION: ObjectManager exists in Application Scope.  The
     * 'model reference' string references a valid model bean instance
     * existing in the ObjectManager.  Note that for nested beans,
     * the nested bean is also instantiated inside the container bean.
     *
     * POSTCONDITION: Class type of the property is returned as identified by  
     * the model reference string.
     *
     * @param request ServletRequest object representing the client request
     * @param objectReference A string referencing a bean's property.
     *
     * @exception FacesException If the model bean identified by the
     *     model reference string cannot be found in the ObjectManager,
     *     or the property value could not be retrieved.
     */
    public Class getModelType(String model) throws FacesException {
        ParameterCheck.nonNull(model);
	
        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        Class returnClass = null;
        
        if (model.startsWith("${") && model.endsWith("}")) {
	    expression = model.substring(2, model.length() - 1);
            // if it is not a nested property, then it directly references
            // a model object. So there should be a model bean existing
            // in the ObjectManager with this name.
            if ( expression.indexOf(".") == -1 ) {
                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                object = getHttpSession().getAttribute(baseName);
                if (object == null) {
                    throw new FacesException("Named Object: '"+expression+
                        "' not found in ObjectManager.");
                }
                returnClass = object.getClass();
            } else {    
                property = expression.substring((expression.indexOf(".")+1));
                baseName = expression.substring(0, expression.indexOf("."));
                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                object = getHttpSession().getAttribute(baseName);
                if (object == null) {
                    throw new FacesException("Named Object: '"+baseName+
                        "' not found in ObjectManager.");
                }
                try {
                    returnClass = PropertyUtils.getPropertyType(object, 
                                                                   property);
                } catch (IllegalAccessException iae) {
                    throw new FacesException(iae.getMessage());
                } catch (InvocationTargetException ite) {
                    throw new FacesException(ite.getMessage());
                } catch (NoSuchMethodException nme) {
                    throw new FacesException(nme.getMessage());
                }
            }    
        } else {
            throw new FacesException("Expression should start with ${");    
        }
        return returnClass;
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
    public Object getModelValue(String model) throws FacesException {
        
        ParameterCheck.nonNull(model);
	
        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        Object returnObject = null;
     
        if (model.startsWith("${") && model.endsWith("}")) {
	    expression = model.substring(2, model.length() - 1);
            // if it is not a nested property, then it directly references
            // a model object. So there should be a model bean existing
            // in the ObjectManager with this name.
            if ( expression.indexOf(".") == -1 ) {
                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                returnObject = getHttpSession().getAttribute(baseName);
                if (returnObject == null) {
                    throw new FacesException("Named Object: '"+expression+
                        "' not found in ObjectManager.");
                }
            } else {    
                property = expression.substring((expression.indexOf(".")+1));
                baseName = expression.substring(0, expression.indexOf("."));

                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                object = getHttpSession().getAttribute(baseName);
                if (object == null) {
                    throw new FacesException("Named Object: '"+baseName+
                        "' not found in ObjectManager.");
                }
                try {
                    returnObject = PropertyUtils.getNestedProperty(object, 
                                                                   property);
                } catch (IllegalAccessException iae) {
                    throw new FacesException(iae.getMessage());
                } catch (InvocationTargetException ite) {
                    throw new FacesException(ite.getMessage());
                } catch (NoSuchMethodException nme) {
                    throw new FacesException(nme.getMessage());
                }
            }    
        } else {
            throw new FacesException("Expression should start with ${");    
        }
        return returnObject;
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
    public void setModelValue(String model, Object value) 
            throws FacesException {
        
        ParameterCheck.nonNull(model);
        ParameterCheck.nonNull(value);
        
        String expression = null;
        String property = null;
        String baseName = null;
        Object object = null;
        
       // model reference string complies with JSTL syntax
        if (model.startsWith("${") && model.endsWith("}")) {
	    expression = model.substring(2, model.length() - 1);
            // if it is not a nested property, then it directly references
            // a model object. So there should be a model bean existing
            // in the ObjectManager with this name.
            if ( expression.indexOf(".") == -1 ) {
                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                object = getHttpSession().getAttribute(baseName);
                if (object == null) {
                    throw new FacesException("Named Object: '"+expression+
                        "' not found in ObjectManager.");
                }
                object = value;
                getHttpSession().setAttribute(baseName, object);   
            } else {
                property = expression.substring((expression.indexOf(".")+1));
                baseName = expression.substring(0, expression.indexOf("."));
                // PENDING (visvan) temporary: assume the model bean is stored
                // in session
                object = getHttpSession().getAttribute(baseName);
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
            }    
        } else {
            throw new FacesException("Expression should start with ${");
        }    
    }


    public void release() {
        throw new FacesException("UnImplemented");
    }
    
    // The testcase for this class is TestFacesContextImpl.java 
    // The testcase for this class is TestFacesContextImpl_Model.java

} // end of class FacesContextImpl
