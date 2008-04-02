/*
 * $Id: UIMessage.java,v 1.2 2003/11/07 01:46:55 craigmcc Exp $
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
    private boolean showSummary = true;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the client identifier of the component for which
     * this component represents associated message(s) (if any).</p>
     */
    public String getFor() {

	ValueBinding vb = getValueBinding("for");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (this.forVal);
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
	setValueBinding("for", null);

    }


    /**
     * <p>Return the flag indicating whether the <code>detail</code> property
     * of the associated message(s) should be displayed.</p>
     */
    public boolean isShowDetail() {

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
	setValueBinding("showDetail", null);

    }


    /**
     * <p>Return the flag indicating whether the <code>summary</code> property
     * of the associated message(s) should be displayed.</p>
     */
    public boolean isShowSummary() {

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
	setValueBinding("showSummary", null);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = this.showSummary ? Boolean.TRUE : Boolean.FALSE;
        values[2] = this.showDetail ? Boolean.TRUE : Boolean.FALSE;
        values[3] = this.forVal;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        showSummary = ((Boolean) values[1]).booleanValue();
        showDetail = ((Boolean) values[2]).booleanValue();
	forVal = (String) values[3];
    }


}
