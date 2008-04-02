/*
 * $Id: UIMessage.java,v 1.3 2003/11/07 18:55:31 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * <p>This component is responsible for displaying messages for a given
 * <code>clientId</code>.  The component obtains the messages from the
 * {@link FacesContext}.</p>
 *
 * <p>The renderer-type for this component is <code>Message</code>.</p>
 * 
 */

public class UIMessage extends UIComponentBase {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIMessage} instance with default property
     * values.</p>
     */
    public UIMessage() {

        super();
        setRendererType("Message");

    }


    // ------------------------------------------------------ Instance Variables


    private String forVal = null;
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

        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = this.forVal;
        values[2] = this.showDetail ? Boolean.TRUE : Boolean.FALSE;
        values[3] = this.showDetailSet ? Boolean.TRUE : Boolean.FALSE;
        values[4] = this.showSummary ? Boolean.TRUE : Boolean.FALSE;
        values[5] = this.showSummarySet ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
	forVal = (String) values[1];
        showDetail = ((Boolean) values[2]).booleanValue();
        showDetailSet = ((Boolean) values[3]).booleanValue();
        showSummary = ((Boolean) values[4]).booleanValue();
        showSummarySet = ((Boolean) values[5]).booleanValue();

    }


}
