/*
 * $Id: UIOutputBase.java,v 1.6 2003/08/27 00:56:51 craigmcc Exp $
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

import java.io.Serializable;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

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
     * <p>The converter {@link Converter} (if any)
     * that is registered for this component.</p>
     */
    private Converter converter = null;


    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

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


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = super.getState(context);
        List[] converterList = new List[1];
        List theConverter = new ArrayList(1);
        theConverter.add(converter);
        converterList[0] = theConverter;
        values[1] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "converter", converterList);
        values[2] = value;
        values[3] = valueRef;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        List[] converterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1]);
        // PENDING(craigmcc) - it shouldn't be this hard to restore converters
	if (converterList != null) {
            List theConverter = converterList[0];
            if ((theConverter != null) && (theConverter.size() > 0)) {
                converter = (Converter) theConverter.get(0);
            }
	}
        value = (String) values[2];
        valueRef = (String) values[3];

    }


}
