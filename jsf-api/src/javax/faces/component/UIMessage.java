/*
 * $Id: UIMessage.java,v 1.1 2003/11/06 23:01:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;

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

    protected boolean showSummary;
    public boolean isShowSummary() {
	return showSummary;
    }

    public void setShowSummary(boolean newShowSummary) {
	showSummary = newShowSummary;
    }

    protected boolean showDetail;
    public boolean isShowDetail() {
	return showDetail;
    }

    public void setShowDetail(boolean newShowDetail) {
	showDetail = newShowDetail;
    }

    protected String forVal;
    public String getFor() {
	return forVal;
    }

    public void setFor(String newFor) {
	forVal = newFor;
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
