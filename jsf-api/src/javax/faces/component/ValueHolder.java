/*
 * $Id: ValueHolder.java,v 1.14 2004/02/04 23:37:48 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.Application;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ValueHolder</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to support a local
 * value, as well as access data in the model tier via a <em>value
 * binding expression</em>, and support conversion 
 * between String and the model tier data's native data type.
 */

public interface ValueHolder {


    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the local value of this {@link UIComponent} (if any),
     * without evaluating any associated {@link ValueBinding}.</p>
     */
    public Object getLocalValue();


    /**
     * <p>Gets the value of this {@link UIComponent}.  First, consult
     * the local value property of this component.  If
     * non-<code>null</code> return it.  If non-null, see if we have a
     * {@link javax.faces.el.ValueBinding} for the <code>value</code>
     * property.  If so, return the result of evaluating the
     * property, otherwise return null.</p>
     */
    public Object getValue();


    /**
     * <p>Set the value of this {@link UIComponent} (if any).</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);



    /**
     * <p>Return the {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     */
    public Converter getConverter();


    /**
     * <p>Set the {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     *
     * @param converter New {@link Converter} (or <code>null</code>)
     */
    public void setConverter(Converter converter);
}
