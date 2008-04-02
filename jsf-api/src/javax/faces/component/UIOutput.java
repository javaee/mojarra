/*
 * $Id: UIOutput.java,v 1.28 2003/08/15 17:23:43 craigmcc Exp $
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
import javax.faces.render.Renderer;


/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that has a
 * value, optionally retrieved from a model tier bean via a value reference
 * expression, that is displayed to the user.  The user cannot directly modify
 * the rendered value; it is for display purposes only.</p>
 *
 * <p>During the <em>Render Response</em> phase of the request processing
 * lifecycle, the current value of this component must be
 * converted to a String (if it is not already), according to the following
 * rules:</p>
 * <ul>
 * <li>If the current value is not <code>null</code>, and is not already
 *     a <code>String</code>, locate a {@link Converter} (if any) to use
 *     for the conversion, as follows:
 *     <ul>
 *     <li>If <code>getConverter()</code> returns a non-null {@link Converter},
 *         use that one, otherwise</li>
 *     <li>If <code>Application.createConverter(Class)</code>, passing the
 *         current value's class, returns a non-null {@link Converter},
 *         use that one.</li>
 *     </ul></li>
 * <li>If the current value is not <code>null</code> and a {@link Converter}
 *     was located, call its <code>getAsString()</code> method to perform
 *     the conversion.</li>
 * <li>If the current value is not <code>null</code> but no {@link Converter}
 *     was located, call <code>toString()</code> on the current value to perform
 *     the conversion.</li>
 * </ul>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UIOutput extends UIComponent {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link Converter} (if any)
     * that is registered for this component.</p>
     */
    public Converter getConverter();


    /**
     * <p>Set the {@link Converter} (if any)
     * that is registered for this component.</p>
     *
     * @param converter New {@link Converter} (or <code>null</code>)
     */
    public void setConverter(Converter converter);


    /**
     * <p>Return the local value of this {@link UIOutput} component
     * (if any).</p>
     */
    public Object getValue();


    /**
     * <p>Set the local value of this {@link UIOutput} component (if any).</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);


    /**
     * <p>Return the value reference expression for this {@link UIOutput}
     * component (if any), pointing at the model tier property that will
     * be rendered.</p>
     */
    public String getValueRef();


    /**
     * <p>Set the value reference expression for this {@link UIOutput}
     * component (if any), pointing at the model tier property that will
     * be rendered.</p>
     *
     * @param valueRef The new value reference expression
     */
    public void setValueRef(String valueRef);


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Evaluate and return the current value of this component, according
     * to the following algorithm.</p>
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
