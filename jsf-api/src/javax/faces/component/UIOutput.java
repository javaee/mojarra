/*
 * $Id: UIOutput.java,v 1.43 2004/01/21 07:39:56 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


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

public class UIOutput extends UIComponentBase
    implements ValueHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "Output";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "Output";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutput} instance with default property
     * values.</p>
     */
    public UIOutput() {

        super();
        setRendererType("Text");

    }


    // ------------------------------------------------------ Instance Variables


    private Converter converter = null;
    private Object value = null;



    // --------------------------------------- ConvertableValueHolder Properties


    public Converter getConverter() {

	if (this.converter != null) {
	    return (this.converter);
	}
	ValueBinding vb = getValueBinding("converter");
	if (vb != null) {
	    return ((Converter) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }



    public Object getLocalValue() {

	return (this.value);

    }


    public Object getValue() {

	if (this.value != null) {
	    return (this.value);
	}
	ValueBinding vb = getValueBinding("value");
	if (vb != null) {
	    return (vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, converter);
        values[2] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        converter = (Converter) restoreAttachedState(context, values[1]);
        value = values[2];

    }


}
