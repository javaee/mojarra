/*
 * $Id: ConverterManagerFactoryImpl.java,v 1.3 2002/04/15 20:11:01 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ConverterManagerFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.ConverterManager;
import javax.faces.FacesException;
import javax.faces.NavigationMap;
import javax.faces.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

import javax.faces.FacesFactory;

/**
 *
 *  <B>ConverterManagerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ConverterManagerFactoryImpl.java,v 1.3 2002/04/15 20:11:01 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ConverterManagerFactoryImpl implements FacesFactory
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

    public ConverterManagerFactoryImpl()
    {
        super();
    }

    //
    // Methods from ConverterManagerFactory
    //

    //
    // Class methods
    //

    //
    // Methods from FacesFactory
    //

    public Object newInstance(String facesName, ServletRequest req,
                          ServletResponse res) throws FacesException
    {
        throw new FacesException("Can't create ConverterManager from Request and Response");
    }

    public Object newInstance(String facesName, ServletContext ctx) throws FacesException
    {
        return (new ConverterManagerImpl(ctx)); 
    }

    public Object newInstance(String facesName) throws FacesException
    {
        throw new FacesException("Can't create ConverterManager");    
    }

    public Object newInstance(String facesName, Map args) throws FacesException
    {
        throw new FacesException("Can't create ConverterManager from map");
    }
   
} // end of class ConverterManagerFactoryImpl
