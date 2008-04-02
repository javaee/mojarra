/*
 * $Id: UIMessages.java,v 1.3 2003/11/07 18:55:31 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * <p>The renderer for this component is responsible for obtaining the
 * messages from the {@link FacesContext} and displaying them to the
 * user.</p>
 *
 * <p>This component supports the <code>Messages</code> renderer-type.</p>
 * 
 */

public class UIMessages extends UIComponentBase {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIMessages} instance with default property
     * values.</p>
     */
    public UIMessages() {

        super();
        setRendererType("Messages");

    }


    // ------------------------------------------------------ Instance Variables


    private boolean globalOnly = true;
    private boolean globalOnlySet = false;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the flag indicating whether only global messages (that is,
     * messages with no associated client identifier) should be rendered.</p>
     */
    public boolean isGlobalOnly() {

	if (this.globalOnlySet) {
	    return (this.globalOnly);
	}
	ValueBinding vb = getValueBinding("globalOnly");
	if (vb != null) {
	    Boolean value = (Boolean) vb.getValue(getFacesContext());
	    return (value.booleanValue());
	} else {
	    return (this.globalOnly);
	}

    }


    /**
     * <p>Set the flag indicating whether only global messages (that is,
     * messages with no associated client identifier) should be rendered.</p>
     *
     * @param globalOnly The new flag value
     */
    public void setGlobalOnly(boolean globalOnly) {

	this.globalOnly = globalOnly;
	this.globalOnlySet = true;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = this.globalOnly ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        globalOnly = ((Boolean) values[1]).booleanValue();

    }


}
