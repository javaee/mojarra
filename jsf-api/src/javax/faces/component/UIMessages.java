/*
 * $Id: UIMessages.java,v 1.14 2004/02/26 20:30:33 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Messages</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
 *
 * 
 */

public class UIMessages extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Messages";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Messages";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIMessages} instance with default property
     * values.</p>
     */
    public UIMessages() {

        super();
        setRendererType("javax.faces.Messages");

    }


    // ------------------------------------------------------ Instance Variables


    private boolean globalOnly = false;
    private boolean globalOnlySet = false;
    private boolean showDetail = false;
    private boolean showDetailSet = false;
    private boolean showSummary = true;
    private boolean showSummarySet = false;

    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the flag indicating whether only global messages (that
     * is, messages with no associated client identifier) should be
     * rendered.  Defaults to false.</p>
     */
    public boolean isGlobalOnly() {

	if (this.globalOnlySet) {
	    return (this.globalOnly);
	}
	ValueBinding vb = getValueBinding("globalOnly");
	if (vb != null) {
	    return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
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

    /**
     * <p>Return the flag indicating whether the <code>detail</code>
     * property of the associated message(s) should be displayed.
     * Defaults to false.</p>
     */
    public boolean isShowDetail() {

	if (this.showDetailSet){
	    return (this.showDetail);
	}
	ValueBinding vb = getValueBinding("showDetail");
	if (vb != null) {
	    return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
	} else {
	    return (this.showDetail);
	}

    }


    /**
     * <p>Set the flag indicating whether the <code>detail</code> property
     * of the associated message(s) should be displayed.</p>
     *
     * @param showDetail The new flag
     */
    public void setShowDetail(boolean showDetail) {

	this.showDetail = showDetail;
	this.showDetailSet = true;

    }


    /**
     * <p>Return the flag indicating whether the <code>summary</code>
     * property of the associated message(s) should be displayed.
     * Defaults to true.</p>
     */
    public boolean isShowSummary() {

	if (this.showSummarySet) {
	    return (this.showSummary);
	}
	ValueBinding vb = getValueBinding("showSummary");
	if (vb != null) {
	    return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
	} else {
	    return (this.showSummary);
	}

    }


    /**
     * <p>Set the flag indicating whether the <code>summary</code> property
     * of the associated message(s) should be displayed.</p>
     *
     * @param showSummary The new flag value
     */
    public void setShowSummary(boolean showSummary) {

	this.showSummary = showSummary;
	this.showSummarySet = true;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = this.globalOnly ? Boolean.TRUE : Boolean.FALSE;
        values[2] = this.globalOnlySet ? Boolean.TRUE : Boolean.FALSE;
        values[3] = this.showDetail ? Boolean.TRUE : Boolean.FALSE;
        values[4] = this.showDetailSet ? Boolean.TRUE : Boolean.FALSE;
        values[5] = this.showSummary ? Boolean.TRUE : Boolean.FALSE;
        values[6] = this.showSummarySet ? Boolean.TRUE : Boolean.FALSE;

        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        globalOnly = ((Boolean) values[1]).booleanValue();
        globalOnlySet = ((Boolean) values[2]).booleanValue();
        showDetail = ((Boolean) values[3]).booleanValue();
        showDetailSet = ((Boolean) values[4]).booleanValue();
        showSummary = ((Boolean) values[5]).booleanValue();
        showSummarySet = ((Boolean) values[6]).booleanValue();

    }


}
