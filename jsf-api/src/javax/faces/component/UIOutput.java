/*
 * $Id: UIOutput.java,v 1.52 2005/08/22 22:07:57 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;


import java.io.IOException;

import javax.el.ValueExpression;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;



/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that has a
 * value, optionally retrieved from a model tier bean via a value
 * expression, that is displayed to the user.  The user cannot directly
 * modify the rendered value; it is for display purposes only.</p>
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
 * "<code>javax.faces.Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIOutput extends UIComponentBase
    implements ValueHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Output";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Output";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutput} instance with default property
     * values.</p>
     */
    public UIOutput() {

        super();
        setRendererType("javax.faces.Text");

    }


    // ------------------------------------------------------ Instance Variables


    private Converter converter = null;
    private Object value = null;



    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // --------------------------------------- ConvertibleValueHolder Properties


    public Converter getConverter() {

	if (this.converter != null) {
	    return (this.converter);
	}
	ValueExpression ve = getValueExpression("converter");
	if (ve != null) {
	    try {
		return ((Converter) ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
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
	ValueExpression ve = getValueExpression("value");
	if (ve != null) {
	    try {
		return (ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
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
