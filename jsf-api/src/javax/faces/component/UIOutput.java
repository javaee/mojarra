/*
 * $Id: UIOutput.java,v 1.24 2003/03/13 01:11:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that displays
 * output to the user.  The user cannot manipulate this component; it is
 * for display purposes only.  There are no restrictions on the data type
 * of the local value, or the object referenced by the value reference
 * expression (if any); however, individual
 * {@link javax.faces.render.Renderer}s will
 * generally impose restrictions on the type of data they know how to
 * display.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIOutput extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIOutput";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIOutput} instance with default property
     * values.</p>
     */
    public UIOutput() {

        super();
        setRendererType("Text");

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>The local value of this {@link UIInput} component.</p>
     */
    private Object value = null;


    /**
     * <p>Return the local value of this {@link UIInput} component.</p>
     */
    public Object getValue() {

        return (this.value);

    }


    /**
     * <p>Set the local value of this {@link UIInput} component.</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    /**
     * <p>The value reference expression for this {@link UIInput} component.
     * </p>
     */
    private String valueRef = null;


    /**
     * <p>Return the value reference expression for this {@link UIInput}
     * component, pointing at the model tier property that will be updated
     * or rendered.</p>
     */
    public String getValueRef() {

        return (this.valueRef);

    }


    /**
     * <p>Set the value reference expression for this {@link UIInput}
     * component, pointing at the model tier property that will be updated
     * or rendered.</p>
     *
     * @param valueRef The new value reference expression
     */
    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Evaluate and return the current value of this component, according
     * to the following algorithm.</p>
     * <ul>
     * <li>If the <code>value</code> property has been set (containing
     *     the local value for this component), return that; else</li>
     * <li>If the <code>valueRef</code> property has been set,
     *     <ul>
     *     <li>Retrieve the {@link Application} instance for this web
     *         application from {@link ApplicationFactory}.</li>
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
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            Application application = factory.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


}
