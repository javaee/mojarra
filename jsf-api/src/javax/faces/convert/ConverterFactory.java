/*
 * $Id: ConverterFactory.java,v 1.7 2003/02/24 18:16:04 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import java.util.Iterator;
import javax.faces.FacesException;


/**
 * <p><strong>ConverterFactory</strong> is a factory object that creates
 * (if neeeded) and returns new {@link Converter} instances.</p>
 *
 * <p>There must be one <code>ConverterFactory</code> instance per web
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
     * <code>converterId</code>, to be supported by this
     * <code>ConverterFactory</code>.  This method may be called at
     * any time, and makes the corresponding {@link Converter} instance
     * available throughout the remaining lifetime of this web application.
     * </p>
     *
     * @param converterId Identifier of the new {@link Converter}
     * @param converter {@link Converter} instance that we are registering
     *
     * @exception IllegalArgumentException if a {@link Converter} with the
     *  specified <code>converterId</code> has already been registered
     * @exception NullPointerException if <code>converterId</code> or
     *  <code>converter</code> is null
     */
    public abstract void addConverter(String converterId, Converter converter);


    /**
     * <p>Register a new {@link Converter} instance that knows how to perform
     * conversions to and from the specified object class.  This method may be
     * called at any time, and makes the corresponding {@link Converter}
     * instance available throughout the remaining lifetime of this web
     * application.</p>
     *
     * @param clazz Class for which Object-to-String and String-to-Object
     *  conversions are supported by this {@link Converter}
     * @param converter {@link Converter} instance that we are registering
     *
     * @exception NullPointerException if <code>clazz</code> or
     *  <code>converter</code> is <code>null</code>
     */
    public abstract void addConverter(Class clazz, Converter converter);


    /**
     * <p>Create (if needed) and return a {@link Converter} instance that
     * may be used to perform Object-to-String and String-to-Object conversions
     * for objects of the specified class.  A search of the registered
     * {@link Converter}s is performed in the following order:</p>
     * <ul>
     * <li>An exact match on the original class.</li>
     * <li>An exact match on one of the interfaces directly implemented
     *     by the original class, checked in the order they are returned
     *     by <code>Class.getInterfaces()</code>.</li>
     * <li>Recursively for each superclass of the original class:
     *     <ul>
     *     <li>An exact match on this superclass.</li>
     *     <li>An exact match on one of the interfaces directly implemented
     *         by this superclass.</li>
     *     </ul></li>
     * </ul>
     * <p>If a {@link Converter} is discovered during the search described
     * above, it is returned; otherwise <code>null</code> is returned.</p>
     *
     * <p>Each call to <code>getConverter()</code> for the same
     * <code>clazz</code>, from within the same web application,
     * must return the same {@link Converter} instance.</p>
     *
     * @param clazz Class for which a {@link Converter} is requested
     *
     * @exception NullPointerException if <code>clazz</code>
     *  is <code>null</code>
     */
    public abstract Converter getConverter(Class clazz);


    /**
     * <p>Create (if needed) and return a {@link Converter} instance that
     * may be used to perform Object-to-String and String-to-Object
     * conversions for web applications based on JavaServer Faces.  The
     * set of available converter identifiers is available via the
     * <code>getConverterIds()</code> method.  If no {@link Converter}
     * has been registered for the specified converter identifier,
     * return <code>null</code>.</p>
     *
     * <p>Each call to <code>getConverter()</code> for the same
     * <code>converterId</code>, from within the same web application,
     * must return the same {@link Converter} instance.</p>
     *
     * @param converterId Converter identifier of the requested
     *  {@link Converter} instance
     *
     * @exception NullPointerException if <code>converterId</code>
     *  is <code>null</code>
     */
    public abstract Converter getConverter(String converterId);


    /**
     * <p>Return an <code>Iterator</code> over the set of classes for
     * which we have registered {@link Converter}s in this factory.</p>
     */
    public abstract Iterator getConverterClasses();


    /**
     * <p>Return an <code>Iterator</code> over the set of converter
     * identifiers supported by this factory.</p>
     */
    public abstract Iterator getConverterIds();


}
