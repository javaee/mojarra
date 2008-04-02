/*
 * $Id: UIMessages.java,v 1.1 2003/11/06 23:01:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;

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

    public boolean isGlobalOnly() {
	return globalOnly;
    }

    public void setGlobalOnly(boolean newGlobalOnly) {
	globalOnly = newGlobalOnly;
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
