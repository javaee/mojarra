/*
 * $Id: UIOutput.java,v 1.27 2003/07/26 17:54:37 craigmcc Exp $
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
 * <p>During encoding, the value associated with this component may be
 * converted to a String (if it is not already), according to the following
 * rules:</p>
 * <ul>
 * <li>A <code>null</code> value will be converted to a String in a manner
 *     that is dependent upon the {@link Renderer} in
 *     use; typically, however, it will be rendered as a zero-length
 *     String.</li>
 * <li>If the <code>converter</code> property is set to a non-null value,
 *     it will be passed to <code>Application.getConverter()</code> in order
 *     to retrieve a {@link Converter} instance to be
 *     utilized.  The <code>getAsString()</code> method will be called.</li>
 * <li>If the <code>converter</code> property is null, but the type of the
 *     value is one for which the JavaServer Faces implementation provides
 *     default conversion, that default conversion will be performed.</li>
 * <li>Otherwise, the <code>toString()</code> method will be called
 *     on the value.</li>
 * </ul>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UIOutput extends UIComponent {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the converter id of the {@link Converter} (if any)
     * that is registered for this component.</p>
     */
    public String getConverter();


    /**
     * <p>Set the converter id of the {@link Converter} (if any)
     * that is registered for this component.</p>
     *
     * @param converter New converter identifier (or <code>null</code>)
     */
    public void setConverter(String converter);


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
