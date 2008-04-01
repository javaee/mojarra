/*
 * $Id: ConverterManagerImpl.java,v 1.1 2002/03/08 00:24:48 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ConverterManagerImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.Converter;
import javax.faces.ConverterManager;
import javax.faces.Constants;
import javax.faces.FacesException;

import java.util.Properties;
import java.util.Iterator;
import java.io.InputStream;

public class ConverterManagerImpl extends ConverterManager
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
    private Properties converters = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ConverterManagerImpl()
    {
        super();
        ConverterManager.setInstance(this);
        loadProperties();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    private void loadProperties() {
        if (null == converters) {
            converters = new Properties();
        }

        if (null == converters) {
            return;
        }

        String propsName = ConverterManagerImpl.class.getName();
        propsName = propsName.replace('.', '/');
        propsName += ".properties";
        InputStream is = null;

        if (null == propsName) {
            return;
        }

        // stolen from PropertyMessageResources.java in Struts
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(propsName);
            if (is != null) {
                converters.load(is);
                is.close();
            }
        } catch (Throwable t) {
            System.out.println("Can't open properties: " + t.toString());
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable u) {
                    ;
                }
            }
        }	
    }


    //
    // Class methods
    //

    //
    // Methods from ConverterManagerImpl
    //
    public Converter getConverter(Class classType) {
        
        Class converterClass;
        Converter result;

        Assert.assert_it(null != classType);
        String className = classType.getName();
        
        String converterName = (String) converters.getProperty(className);
        if (converterName == null ) {
            return null;
        }    
        try {
            converterClass = Class.forName(converterName);
            result = (Converter) converterClass.newInstance();
        }
        catch (IllegalAccessException e) {
            throw new FacesException("Can't create instance for " + 
                                     converterName + ": " + e.getMessage());
        }
        catch (InstantiationException e) {
            throw new FacesException("Can't create instance for " + 
                                     converterName + ": " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            throw new FacesException("Can't find class for " + 
                                     converterName + ": " + e.getMessage());
        }
        return result;
        
    }

    /*public void setConverter(java.lang.Class c, Converter cv) {
        String key = c.getName();
        String className = cv.getClass().getName();
        converters.put(key,className);
    } */

    // The testcase for this class is TestclassName.java 


} // end of class className
