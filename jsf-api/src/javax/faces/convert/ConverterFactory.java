/*
 * $Id: ConverterFactory.java,v 1.1 2002/08/31 17:42:45 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import java.util.Iterator;
import javax.faces.FacesException;


/**
 * <p><strong>ConverterFactory</strong> is a Factory object that creates
 * (if necessary) and returns new {@link Converter} instances, each uniquely
 * named by a converter identifier.  Each request for the same converter
 * identifier, from the same web application, shall return the same
 * {@link Converter} instance.</p>
 *
 * <p>There shall be one <code>ConverterFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   ConverterFactory factory = (ConverterFactory)
 *    FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
 * </pre>
 */

public abstract class ConverterFactory {


    /**
     * <p>Register a new {@link Converter} instance with the specified
     * converter identifier, and make it available via calls to
     * <code>getConverter()</code>.</p>
     *
     * @param converterId Converter identifier of the new {@link Converter}
     * @param converter The new {@link Converter} instance
     *
     * @exception IllegalArgumentException if a {@link Converter} with the
     *  specified <code>converterId</code> has already been registered
     * @exception NullPointerException if <code>converterId</code> or
     *  <code>converter</code> is null
     */
    public abstract void addConverter(String converterId, Converter converter);


    /**
     * <p>Construct and return a {@link Converter} that
     * may be used to perform Object-to-String and String-to-Object
     * conversions for web applications based on JavaServer Faces.  Each
     * request for a particular converter identifier, from within the same
     * web application, must return the same {@link Converter} instance.</p>
     *
     * @param converterId Unique identifier of the 
     *  requested {@link Converter} instance
     *
     * @exception FacesException if a {@link Converter} cannot be
     *  constructed for the specified converter identifier
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract Converter getConverter(String converterId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> of the converter identifiers of all
     * {@link Converter} instances registered with this
     * <code>ConverterFactory</code>.</p>
     */
    public abstract Iterator getConverterIds();


}
