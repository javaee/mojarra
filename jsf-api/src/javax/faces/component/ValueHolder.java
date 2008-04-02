/*
 * $Id: ValueHolder.java,v 1.7 2003/11/08 01:15:28 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ValueHolder</strong> is an interface that may be implemented
 * by any concrete {@link UIComponent} that wishes to support a local
 * value, as well as access data in the model tier via a <em>value
 * reference expression</em>.  If the component wishes to support conversion
 * between String and the model tier data's native data type, it should
 * implement {@link ConvertibleValueHolder} instead.</p>
 */

public interface ValueHolder {


    // -------------------------------------------------------------- Properties



    /**
     * <p>Return the local value of this {@link UIComponent} (if any),
     * without evaluating any associated {@link ValueBinding}.</p>
     */
    public Object getLocalValue();


    /**
     * <p>Return the value of this {@link UIComponent} (if any).</p>
     */
    public Object getValue();


    /**
     * <p>Set the value of this {@link UIComponent} (if any).</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);


}
