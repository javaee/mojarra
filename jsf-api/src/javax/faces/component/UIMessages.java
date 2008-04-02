/*
 * $Id: UIMessages.java,v 1.4 2003/11/09 16:52:21 eburns Exp $
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


    private String forVal = null;
    private boolean globalOnly = true;
    private boolean globalOnlySet = false;
    private boolean showDetail = false;
    private boolean showDetailSet = false;
    private boolean showSummary = true;
    private boolean showSummarySet = false;

    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the client identifier of the component for which
     * this component represents associated message(s) (if any).</p>
     */
    public String getFor() {

	if (this.forVal != null) {
	    return (this.forVal);
	}
	ValueBinding vb = getValueBinding("for");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}

    }

    /**
     * <p>Set the client identifier of the component for which
     * this component represents associated message(s) (if any).</p>
     *
     * @param newFor The new client id
     */
    public void setFor(String newFor) {

	forVal = newFor;

    }

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

    /**
     * <p>Return the flag indicating whether the <code>detail</code> property
     * of the associated message(s) should be displayed.</p>
     */
    public boolean isShowDetail() {

	if (this.showDetailSet){
	    return (this.showDetail);
	}
	ValueBinding vb = getValueBinding("showDetail");
	if (vb != null) {
	    Boolean value = (Boolean) vb.getValue(getFacesContext());
	    return (value.booleanValue());
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
     * <p>Return the flag indicating whether the <code>summary</code> property
     * of the associated message(s) should be displayed.</p>
     */
    public boolean isShowSummary() {

	if (this.showSummarySet) {
	    return (this.showSummary);
	}
	ValueBinding vb = getValueBinding("showSummary");
	if (vb != null) {
	    Boolean value = (Boolean) vb.getValue(getFacesContext());
	    return (value.booleanValue());
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

        Object values[] = new Object[8];
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
