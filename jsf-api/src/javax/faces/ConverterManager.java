/*
 * $Id: ConverterManager.java,v 1.2 2002/03/15 20:49:21 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletContext;

/**
 * Class which implements management of type converters.
 * A type converter performs type conversion (in both directions) 
 * between java.lang.String and a particular Java type.  
 * <p>
 * There is one ConverterManager instance per application.
 * JSF may use this instance to obtain a type converter when 
 * migrating a java.lang.String value of a UI Component 
 * to and from the application data object (model) associated 
 * with that component.  
 * <p>
 * JSF implementations must provide a default type
 * converter for each of the following common Java types:
 * <ul>
 * <li>java.lang.Integer
 * <li>java.lang.Float
 * <li>java.lang.Double
 * <li>java.lang.Long
 * <li>java.lang.Short
 * <li>java.util.Date
 * <li>java.util.Currency
 * </ul>
 * These default type converters should use the support provided
 * in the java.text package where appropriate to handle Locale and 
 * formatting when performing conversion.
 * @see Converter
 * 
 */
public abstract class ConverterManager {

    private static ConverterManager instance = null;

    /**
     * @param ctx
     *    the servlet context associated with the application
     * @throws NullPointerException if ctx is null
     * @return ConverterManager instance to be used with specified 
     *         servlet context
     */
    public static ConverterManager getInstance(ServletContext ctx) {
        //PENDING(aim): this should be handled similar to ObjectManager
        //and should not rely on a static field for this instance
        return instance;
    }

    /**
     * Sets the converter manager to be used for the application
     * associated with the specified servlet context.
     * @param ctx
     *    the servlet context associated with the application
     * @param cm
     *    the ConverterManager instance to be used with specified 
     *    servlet context
     * @throws NullPointerException if ctx is null
     */
    public static void setInstance(ServletContext ctx, ConverterManager cm) {
        //PENDING(aim): this should be changed to cache the cm per-app
        instance = cm;
    } 

    /**
     * Returns a Converter object capable to converting java.lang.String 
     * objects to instances of the specified class, and converting objects
     * of the specified class to java.lang.String.  If there is no
     * available converter for the specified class, returns null.
     * @param cls the class which identifies the converter
     * @throws NullPointerException if cls is null
     * @return Converter object capable of converting String objects
     *         to instances of the specified class
     */
    public abstract Converter getConverter(java.lang.Class cls);


}
