/*
 * $Id: UISelectItems.java,v 1.25 2004/01/27 20:29:20 craigmcc Exp $
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
import javax.faces.model.SelectItem;


/**
 * <p><strong>UISelectItems</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of one or more {@link SelectItem} instances to the
 * list of available options in the parent component.  The
 * <code>value</code> of this component (set either directly, or acquired
 * indirectly a {@link ValueBinding}, can be of any
 * of the following types:</p>
 * <ul>
 * <li><em>Single instance of {@link SelectItem}</em> - This instance is
 *     added to the set of available options for the parent tag.</li>
 * <li><em>Array of {@link SelectItem}</em> - This set of instances is
 *     added to the set of available options for the parent component,
 *     in ascending subscript order.</li>
 * <li><em>Collection of {@link SelectItem}</em> - This set of instances is
 *     added to the set of available options for the parent component,
 *     in the order provided by an iterator over them.</li>
 * <li><em>Map</em> - The keys of this object (once converted to
 *     Strings) are assumed to be labels, and the values of this object
 *     (once converted to Strings)
 *     are assumed to be values, of {@link SelectItem} instances that will
 *     be constructed dynamically and added to the set of available options
 *     for the parent component, in the order provided by an iterator over
 *     the keys.</li>
 * </ul>
 */

public class UISelectItems extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectItems";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItems";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItems} instance with default property
     * values.</p>
     */
    public UISelectItems() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private Object value = null;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // -------------------------------------------------- ValueHolder Properties


    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UISelectItems</code>.</p>
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
     * <p>Sets the <code>value</code> property of the
     * <code>UISelectItems</code>.</p>
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
