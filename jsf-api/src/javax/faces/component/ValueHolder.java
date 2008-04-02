/*
 * $Id: ValueHolder.java,v 1.5 2003/10/09 19:18:12 craigmcc Exp $
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
 * implement {@link ConvertableValueHolder} instead.</p>
 */

public interface ValueHolder {


    // -------------------------------------------------------------- Properties



    /**
     * <p>Return the local value of this {@link UIComponent} (if any).</p>
     */
    public Object getValue();


    /**
     * <p>Set the local value of this {@link UIComponent} (if any).</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);


    /**
     * <p>Return the value reference expression for this {@link UIComponent}
     * (if any), pointing at the model tier property that will be rendered.</p>
     */
    public String getValueRef();


    /**
     * <p>Set the value reference expression for this {@link UIComponent}
     * (if any), pointing at the model tier property that will be rendered.</p>
     *
     * @param valueRef The new value reference expression
     */
    public void setValueRef(String valueRef);


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Evaluate and return the current value of this {@link UIComponent},
     * according to the following algorithm.</p>
     * <ul>
     * <li>If the <code>value</code> property has a non-null value,
     *     return that; else</li>
     * <li>If the <code>valueRef</code> property has a non-null value,
     *     <ul>
     *     <li>Retrieve the {@link Application} instance for this web
     *         application.</li>
     *     <li>Ask it for a {@link ValueBinding} for the <code>valueRef</code>
     *         expression.</li>
     *     <li>Use the <code>getValue()</code> method of the
     *         {@link ValueBinding} to retrieve the value that the
     *         value reference expression points at.</li>
     *     </ul>
     * <li>Otherwise, return <code>null</code>.</li>
     * </ul>
     *
     * @param context {@link FacesContext} within which to evaluate the value
     *  reference expression, if necessary
     *
     * @exception EvaluationException if a problem occurs evaluating
     *  the value reference expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public Object currentValue(FacesContext context);


}
