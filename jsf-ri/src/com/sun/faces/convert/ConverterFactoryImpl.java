/*
 * $Id: ConverterFactoryImpl.java,v 1.7 2003/03/13 01:06:26 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
    protected HashMap byClass = null;
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
        byClass = new HashMap();
        converters = new HashMap();
    }
    
    /**
     * Adds the {@link Converter} instance to the internal set.
     *
     * @param clazz Class supported by the specified {@link Converter}
     * @param converter The Converter instance that will be added.
     * @exception IllegalArgumentException if a {@link Converter} with the
     *  specified <code>converterId</code> has already been registered
     * @exception NullPointerException if <code>converterId</code>
     *  or <code>converter</code> arguments are <code>null</code>
     */
    public void addConverter(Class clazz, Converter converter) {

        if (clazz == null || converter == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        synchronized(byClass) {
            byClass.put(clazz, converter);
        }
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
     * @param clazz Class for which a Converter is requested
     * @returns Converter A Converter instance.
     * @exception FacesException if a {@link Converter} cannot be
     *  constructed for the specified converter identifier
     * @exception NullPointerException if <code>converterId</code>
     *  argument is <code>null</code>
     */ 
    public Converter getConverter(Class clazz)
        throws FacesException {

        if (clazz == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // PENDING(craigmcc): support registration of by-class converters
        // in the configuration file.  We're fine for now, because the
        // spec does not mandate any standard by-class converters

        Class target = clazz;
        Converter converter = null;
        synchronized (byClass) {

            while (target != null) {

                // Try the exact-match case first
                converter = (Converter) byClass.get(target);
                if (converter != null) {
                    if (target != clazz) {
                        byClass.put(clazz, converter);
                    }
                    return (converter);
                }

                // Try the interfaces implemented by this class
                Class interfaces[] = clazz.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    converter = (Converter) byClass.get(interfaces[0]);
                    if (converter != null) {
                        byClass.put(clazz, converter);
                        return (converter);
                    }
                }

                // Recursively scan through our superclasses
                target = target.getSuperclass();

            }

        }

        return (null);

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

	Object [] params;
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
	    params = new Object [] { fileName };
            throw new RuntimeException(Util.getExceptionMessage(Util.FILE_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        
        try {
            digester.push(this);
            digester.parse(in);
            in.close();
        } catch (Throwable t) {
	    params = new Object [] { t.getMessage() };
            throw new IllegalStateException(Util.getExceptionMessage(Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, params));
        }

        Assert.assert_it(className != null);

        // Create a converter instance.
        //
        try {
            Class converterClass = Util.loadClass(className, this);
            converter = (Converter)converterClass.newInstance();
        } catch (ClassNotFoundException cnf) {
	    params = new Object [] { cnf.getMessage() };
            throw new RuntimeException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params));
        } catch (InstantiationException ie) {
	    params = new Object [] { ie.getMessage() };
            throw new RuntimeException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
        } catch (IllegalAccessException ia) {
            throw new RuntimeException(ia.getMessage());
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

        // PENDING(craigmcc): This method should really not
        // be public.  That can be avoided by creating a custom
        // Digester rule (in the same package) that can call this
        // method with package-level protection instead

        if (converterId.equalsIgnoreCase(this.converterId)) {
            this.className = className;
        }

    }

    /**
     * Return an iteration of classes for which we have registered
     * converters
     *
     * @returns Iterator The iteration of classes
     */
    public Iterator getConverterClasses() {

        return (byClass.keySet().iterator());

    }

    /**
     * Return an iteration of converter identifiers maintained
     * by this factory instance.
     *
     * @returns Iterator The iteration of identfiers.
     */
    public Iterator getConverterIds() {

        // PENDING(craigmcc): This iterator does not return
        // the ids of the standard Converters until they have
        // been retrieved by a call to getConverter() at least
        // once.  This will be fixed by preloading the config
        // file at startup instead of lazy instantiation

        return (converters.keySet().iterator());

    }

    private Digester initConfig() {
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);
        digester.addCallMethod("converter-config/converter",
            "setConverterClass", 2);
        digester.addCallParam("converter-config/converter/converter-id", 0);
        digester.addCallParam("converter-config/converter/class", 1);    

        return digester;
    }
}
