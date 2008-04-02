/*
 * $Id: ConverterFactoryImpl.java,v 1.2 2003/02/04 16:19:19 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.convert;

import com.sun.faces.util.Util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterFactory;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class ConverterFactoryImpl extends ConverterFactory {

//
// Protected Constants
//
    protected String converterId = null;
    protected String className = null;
    protected HashMap converters = null;
    protected Digester digester = null;

//
// Class Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers
//
    /**
     * Constructor registers default Render kit.
     */
    public ConverterFactoryImpl() {
        super();
        digester = initConfig();
        converters = new HashMap();
    }
    
    /**
     * Adds the {@link Converter} instance to the internal set.
     *
     * @param converterId The Converter identifier for the Converter 
     *        that will be added.
     * @param converter The Converter instance that will be added.
     * @exception IllegalArgumentException if a {@link Converter} with the
     *  specified <code>converterId</code> has already been registered
     * @exception NullPointerException if <code>converterId</code>
     *  or <code>converter</code> arguments are <code>null</code>
     */
    public void addConverter(String converterId, Converter converter) {

        if (converterId == null || converter == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (converters.containsKey(converterId)) {
            throw new IllegalArgumentException(converterId);
        }

        synchronized(converters) {
            converters.put(converterId, converter);
        }
    }

    /**
     * Return a {@link Converter} instance for the given converter 
     * identifier.  If a {@link Converter} instance does not
     * exist for the identifier, create one and add it to the
     * internal table.
     *
     * @param converterId A Converter identifier.
     * @returns Converter A Converter instance.
     * @exception FacesException if a {@link Converter} cannot be
     *  constructed for the specified converter identifier
     * @exception NullPointerException if <code>converterId</code>
     *  argument is <code>null</code>
     */ 
    public Converter getConverter(String converterId)
        throws FacesException {

        if (converterId == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Converter converter = null;
        this.converterId = converterId;

	// PENDING(): instead of doing this lazily, do it all at once at
	// startup time.  The current implementation causes the XML file
	// to be parsed for each new call to getConverter.  That seems
	// ineffecient.

        // If an instance already exists, return it.
        //
        synchronized(converters) {
            if (converters.containsKey(converterId)) {
                return ((Converter) converters.get(converterId));
            }
        }

        String fileName = "com/sun/faces/convert/ConverterConfig.xml";
        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(
                fileName);
        } catch (Throwable t) {
            throw new RuntimeException("Error Opening File:"+fileName);
        }
        
        try {
            digester.push(this);
            digester.parse(in);
            in.close();
        } catch (Throwable t) {
            throw new IllegalStateException(
                "Unable to parse file:"+t.getMessage());
        }

        Assert.assert_it(className != null);

        // Create a converter instance.
        //
        try {
            Class converterClass = Util.loadClass(className);
            converter = (Converter)converterClass.newInstance();
        } catch (ClassNotFoundException cnf) {
            throw new RuntimeException("Class Not Found:"+cnf.getMessage());
        } catch (InstantiationException ie) {
            throw new RuntimeException("Class Instantiation Exception:"+
                ie.getMessage());
        } catch (IllegalAccessException ia) {
            throw new RuntimeException("Illegal Access Exception:"+
                ia.getMessage());
        }

        // Add the newly created converter to the table.
        //
        synchronized(converters) {
            converters.put(converterId, converter);
        }

        return converter;
    }

    /** 
     * Method is invoked through Digester when an Xml pattern is
     * recognized from Xml configuration file.
     * 
     * @param converterId The Converter Identifier
     * @param className The fully qualified class name.
     */ 
    public void setConverterClass(String converterId, String className) {
        if (converterId.equalsIgnoreCase(this.converterId)) {
            this.className = className;
        }
    }

    /**
     * Return an iteration of converter identifiers maintained
     * by this factory instance.
     *
     * @returns Iterator The iteration of identfiers.
     */
    public Iterator getConverterIds() {
        return (converters.keySet().iterator());
    }

    private Digester initConfig() {
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.addCallMethod("converter-config/converter",
            "setConverterClass", 2);
        digester.addCallParam("converter-config/converter/converter-id", 0);
        digester.addCallParam("converter-config/converter/class", 1);    

        return digester;
    }
}
