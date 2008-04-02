/*
 * $Id: UIParameter.java,v 1.28 2005/08/22 22:07:57 ofung Exp $
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
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.el.ValueExpression;
import javax.el.ELException;



/**
 * <p><strong>UIParameter</strong> is a {@link UIComponent} that represents
 * an optionally named configuration parameter for a parent component.</p>
 *
 * <p>Parent components should retrieve the value of a parameter by calling
 * <code>getValue()</code>.  In this way, the parameter value can be set
 * directly on the component (via <code>setValue()</code>), or retrieved
 * indirectly via the value binding expression.</p>
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


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the optional parameter name for this parameter.</p>
     */
    public String getName() {

	if (this.name != null) {
	    return (this.name);
	}
	ValueExpression ve = getValueExpression("name");
	if (ve != null) {
	    try {
		return ((String) ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
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
