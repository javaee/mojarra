/*
 * $Id: UIGraphic.java,v 1.31 2004/01/15 06:03:21 eburns Exp $
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
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that displays
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Image</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIGraphic extends UIComponentBase {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIGraphic} instance with default property
     * values.</p>
     */
    public UIGraphic() {

        super();
        setRendererType("Image");

    }


    // ------------------------------------------------------ Instance Variables


    private Object value = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the image URL for this {@link UIGraphic}.  This method is a
     * typesafe alias for <code>getValue()</code>.</p>
     */
    public String getURL() {

        return ((String) getValue());

    }


    /**
     * <p>Set the image URL for this {@link UIGraphic}.  This method is a
     * typesafe alias for <code>setValue()</code>.</p>
     *
     * @param url The new image URL
     */
    public void setURL(String url) {

        setValue(url);

    }




    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UIGraphic</code>. This will typically be rendered as an URL.</p>
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
     * <p>Sets the <code>value</code> property of the <code>UIGraphic</code>.
     * This will typically be rendered as an URL.</p>
     * 
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }




    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        value = values[1];

    }


}
