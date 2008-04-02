/*
 * $Id: PropertyResolver.java,v 1.3 2003/12/17 15:10:50 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


/**
 * <p><strong>PropertyResolver</strong> represents a pluggable mechanism
 * for accessing a "property" of an underlying Java object instance.
 * Different {@link PropertyResolver} implementations can support property
 * resolution on instances of different Java classes (such as
 * introspection-based access to properties of a JavaBean, or
 * <code>get()</code> and <code>put()</code> calls on a
 * <code>java.util.Map</code> instance).</p>
 */

public abstract class PropertyResolver {


    /**
     * <p>Return the value of the property with the specified name from
     * the specified base object.</p>
     *
     * @param base The base object whose property value is to be returned
     * @param name Name of the property to be returned
     *
     * @exception EvaluationException if an exception is thrown while getting
     *  the property value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>base</code> or
     *  <code>name</code> is <code>null</code>
     * @exception PropertyNotFoundException if the specified property name
     *  does not exist, or is not readable
     */
    public abstract Object getValue(Object base, String name)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Return the value at the specified index of the specified
     * base object.</p>
     *
     * @param base The base object whose property value is to be returned
     * @param index Index of the value to return
     *
     * @exception EvaluationException if an exception is thrown while getting
     *  the property value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception IndexOutOfBoundsException if thrown by the underlying
     *  access to the base object
     * @exception NullPointerException if <code>base</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if some other exception occurs 
     */
    public abstract Object getValue(Object base, int index)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Set the specified value of the property with the specified name on
     * the specified base object.</p>
     *
     * @param base The base object whose property value is to be set
     * @param name Name of the property to be set
     * @param value Value of the property to be set
     *
     * @exception EvaluationException if an exception is thrown while setting
     *  the property value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>base</code> or
     *  <code>name</code> is <code>null</code>
     * @exception PropertyNotFoundException if the specified property name
     *  does not exist, or is not writeable
     */
    public abstract void setValue(Object base, String name, Object value)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Set the value at the specified index of the specified
     * base object.</p>
     *
     * @param base The base object whose property value is to be set
     * @param index Index of the value to set
     * @param value Value to be set
     *
     * @exception EvaluationException if an exception is thrown while setting
     *  the property value (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception IndexOutOfBoundsException if thrown by the underlying
     *  access to the base object
     * @exception NullPointerException if <code>base</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if some other exception occurs
     */
    public abstract void setValue(Object base, int index, Object value)
        throws EvaluationException, PropertyNotFoundException;


    /**
     * <p>Return <code>true</code> if the specified property of the specified
     * base object is known to be immutable; otherwise, return
     * <code>false</code>.</p>
     *
     * @param base The base object whose property is to analyzed
     * @param name Name of the property to be analyzed
     *
     * @exception NullPointerException if <code>base</code> or
     *  <code>name</code> is <code>null</code>
     * @exception PropertyNotFoundException if the specified property name
     *  does not exist
     */
    public abstract boolean isReadOnly(Object base, String name)
        throws PropertyNotFoundException;


    /**
     * <p>Return <code>true</code> if the value at the specified index of
     * the specified base object is known to be immutable; otherwise,
     * return <code>false</code>.</p>
     *
     * @param base The base object whose property is to analyzed
     * @param index Index of the value whose type is to be returned
     *
     * @exception IndexOutOfBoundsException if thrown by the underlying
     *  accessed to the indexed property
     * @exception NullPointerException if <code>base</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if some other exception occurs
     */
    public abstract boolean isReadOnly(Object base, int index)
        throws PropertyNotFoundException;


    /**
     * <p>Return the <code>java.lang.Class</code> representing the type of
     * the specified property of the specified base object, if it can be
     * determined; otherwise return <code>null</code>.</p>
     *
     * @param base The base object whose property is to analyzed
     * @param name Name of the property to be analyzed
     *
     * @exception NullPointerException if <code>base</code> or
     *  <code>name</code> is <code>null</code>
     * @exception PropertyNotFoundException if the specified property name
     *  does not exist
     */
    public abstract Class getType(Object base, String name)
        throws PropertyNotFoundException;


    /**
     * <p>Return the <code>java.lang.Class</code> representing the type of
     * value at the specified index of the specified base object, or
     * <code>null</code> if this value is <code>null</code>.</p>
     *
     * @param base The base object whose property is to analyzed
     * @param index Index of the value whose type is to be returned
     *
     * @exception IndexOutOfBoundsException if thrown by the underlying
     *  accessed to the indexed property
     * @exception NullPointerException if <code>base</code>
     *  is <code>null</code>
     * @exception PropertyNotFoundException if some other exception occurs
     */
    public abstract Class getType(Object base, int index)
        throws PropertyNotFoundException;


}
