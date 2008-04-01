/*
 * $Id: ObjectAccessorFactoryImpl.java,v 1.3 2002/04/15 20:11:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectAccessorFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.ObjectAccessor;
import javax.faces.BeanAccessor;
import javax.faces.FacesContext;
import javax.faces.FacesException;
import javax.faces.FacesFactory;
import javax.faces.Constants;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import java.util.Map;

/**
 *
 *  <B>ObjectAccessorFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectAccessorFactoryImpl.java,v 1.3 2002/04/15 20:11:02 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectAccessorFactoryImpl implements FacesFactory
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

    //
    // Constructors and Initializers    
    //

    public ObjectAccessorFactoryImpl()
    {
        super();
    }

    //
    // Methods from ObjectAccessorFactory
    //

    //
    // Class methods
    //


    // Methods from FacesFactory
    //

    public Object newInstance(String facesName, ServletRequest req,
                          ServletResponse res) throws FacesException
    {
        throw new FacesException("Can't create ObjectAccessor from Request and Response");
    }

    public Object newInstance(String facesName, ServletContext ctx) throws FacesException
    {
        throw new FacesException("Can't create ObjectAccessor from ServletContext");
    }

    public Object newInstance(String facesName) throws FacesException
    {
        throw new FacesException("Can't create ObjectAccessor");   
    }

    public Object newInstance(String facesName, Map args) throws FacesException
    {
        return new BeanAccessor(((FacesContext)args.get(Constants.REF_FACESCONTEXT)));
    }
  
    //
    // General Methods
   //

} // end of class ObjectAccessorFactoryImpl
