/*
 * $Id: ConverterManagerFactoryImpl.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
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

import javax.servlet.ServletContext;

/**
 *
 *  <B>ConverterManagerFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ConverterManagerFactoryImpl.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ConverterManagerFactoryImpl extends ConverterManagerFactory
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

    public ConverterManager newConverterManager(javax.servlet.ServletContext sc) 
            throws FacesException {
        ConverterManager result = new ConverterManagerImpl(sc);
        return result;
    }

    //
    // General Methods
    //

} // end of class ConverterManagerFactoryImpl
