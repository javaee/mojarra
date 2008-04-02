/*
 * $Id: UIOutputBase.java,v 1.5 2003/08/21 15:26:06 eburns Exp $
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

    // ---------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	Object [] myState = (Object []) state[THIS_INDEX];
        valueRef = (String) myState[ATTRS_INDEX];
	value = myState[VALUE_INDEX];
	// restore the converter
	List [] converterList = context.getApplication().getViewHandler().
	    getStateManager().restoreAttachedObjectState(context, 
							 myState[CONVERTER_INDEX]);
	if (null != converterList) {
	    converter = (Converter) ((List)converterList[0]).get(0);
	}

	super.restoreState(context, state[SUPER_INDEX]);
    }


    private static final int ATTRS_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int CONVERTER_INDEX = 2;


    public Object getState(FacesContext context) {
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	Object [] myState = new Object[3];
	myState[ATTRS_INDEX] = valueRef;
	// PENDING(edburns): is it correct to "just skip it" if value is
	// not Serializable?
	if (value instanceof Serializable) {
	    myState[VALUE_INDEX] = value;
	}
	if (null != converter) {
	    List [] converterList = new List[1];
	    List theConverter = new ArrayList(1);
	    theConverter.add(converter);
	    converterList[0] = theConverter;

	    myState[CONVERTER_INDEX] = context.getApplication().getViewHandler().getStateManager().getAttachedObjectState(context, this, "converter", converterList);
	}
	result[THIS_INDEX] = myState;
	result[SUPER_INDEX] = superState;
	return result;
    }

}
