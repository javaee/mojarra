/*
 * $Id: UIParameter.java,v 1.22 2004/01/21 19:23:16 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UIParameter</strong> is a {@link UIComponent} that represents
 * an optionally named configuration parameter for a parent component.</p>
 *
 * <p>Parent components should retrieve the value of a parameter by calling
 * <code>getValue()</code>.  In this way, the parameter value can be set
 * directly on the component (via <code>setValue()</code>), or retrieved
 * indirectly via the value reference expression.</p>
 *
 * <p>In some scenarios, it is necessary to provide a parameter name, in
 * addition to the parameter value that is accessible via the
 * <code>getValue()</code> method.
 * {@link javax.faces.render.Renderer}s that support parameter names on their
 * nested {@link UIParameter} child components should document
 * their use of this property.</p>
 */

public class UIParameter extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Parameter";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Parameter";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIParameter} instance with default property
     * values.</p>
     */
    public UIParameter() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private String name = null;
    private Object value = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the optional parameter name for this parameter.</p>
     */
    public String getName() {

	if (this.name != null) {
	    return (this.name);
	}
	ValueBinding vb = getValueBinding("name");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the optional parameter name for this parameter.</p>
     *
     * @param name The new parameter name,
     *  or <code>null</code> for no name
     */
    public void setName(String name) {

        this.name = name;

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UIParameter</code>.</p>
     */
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


    /**
     * <p>Sets the <code>value</code> property of the\
     * <code>UIParameter</code>.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = name;
        values[2] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        name = (String) values[1];
        value = values[2];

    }


}
