/*
 * $Id: UIOutputBase.java,v 1.2 2003/07/26 17:54:50 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UIOutputBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIOutput}.</p>
 */

public class UIOutputBase extends UIComponentBase implements UIOutput {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutputBase} instance with default property
     * values.</p>
     */
    public UIOutputBase() {

        super();
        setRendererType("Text");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The converter id of the {@link Converter} (if any)
     * that is registered for this component.</p>
     */
    private String converter = null;


    public String getConverter() {

        return (this.converter);

    }


    public void setConverter(String converter) {

        this.converter = converter;

    }


    /**
     * <p>The local value of this {@link UIOutput} component (if any).</p>
     */
    private Object value = null;


    public Object getValue() {

        return (this.value);

    }


    public void setValue(Object value) {

        this.value = value;

    }


    /**
     * <p>The value reference expression for this {@link UIOutput} component
     * (if any).</p>
     */
    private String valueRef = null;


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ---------------------------------------------------------- Public Methods


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
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


}
