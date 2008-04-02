/*
 * $Id: ConvertableValueHolder.java,v 1.1 2003/10/09 19:18:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ConvertableValueHolder</strong> is an extension of
 * {@link ValueHolder} that adds support for conversion between Strings and
 * the native model tier data type.</p>
 */

public interface ConvertableValueHolder extends ValueHolder {


    // -------------------------------------------------------------- Properties



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


    /**
     * <p>Return a flag indicating whether the local value of this component
     * is valid (no conversion error has occurred).</p>
     */
    public boolean isValid();


    /**
     * <p>Set a flag indicating whether the local value of this component
     * is valid (no conversion error has occurred).</p>
     *
     * @param valid The new valid flag
     */
    public void setValid(boolean valid);


}
