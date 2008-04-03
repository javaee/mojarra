/*
 * $Id: UIMessage.java,v 1.19 2007/04/27 22:00:05 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.el.ELException;
import javax.el.ValueExpression;


/**
 * <p>This component is responsible for displaying messages for a specific
 * {@link UIComponent}, identified by a <code>clientId</code>.  The component
 * obtains the messages from the {@link FacesContext}.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Message</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>

 * 
 */

public class UIMessage extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Message";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Message";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIMessage} instance with default property
     * values.</p>
     */
    public UIMessage() {

        super();
        setRendererType("javax.faces.Message");

    }


    // ------------------------------------------------------ Instance Variables


    private String forVal = null;
    private boolean showDetail = true;
    private boolean showDetailSet = false;
    private boolean showSummary = false;
    private boolean showSummarySet = false;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the client identifier of the component for which
     * this component represents associated message(s) (if any).</p>
     */
    public String getFor() {

	if (this.forVal != null) {
	    return (this.forVal);
	}
	ValueExpression ve = getValueExpression("for");
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
     * <p>Set the client identifier of the component for which this
     * component represents associated message(s) (if any).  This
     * property must be set before the message is displayed.</p>
     *
     * @param newFor The new client id
     */
    public void setFor(String newFor) {

	forVal = newFor;

    }


    /**
     * <p>Return the flag indicating whether the <code>detail</code>
     * property of the associated message(s) should be displayed.
     * Defaults to <code>true</code>.</p>
     */
    public boolean isShowDetail() {

	if (this.showDetailSet){
	    return (this.showDetail);
	}
	ValueExpression ve = getValueExpression("showDetail");
	if (ve != null) {
	    try {
		return (Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext())));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
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
     * Defaults to <code>false</code>.</p>
     */
    public boolean isShowSummary() {

	if (this.showSummarySet) {
	    return (this.showSummary);
	}
	ValueExpression ve = getValueExpression("showSummary");
	if (ve != null) {
	    try {
		return (!Boolean.FALSE.equals(ve.getValue(getFacesContext().getELContext())));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
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


    private Object[] values;

    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[6];
        }
       
        values[0] = super.saveState(context);
        values[1] = this.forVal;
        values[2] = this.showDetail ? Boolean.TRUE : Boolean.FALSE;
        values[3] = this.showDetailSet ? Boolean.TRUE : Boolean.FALSE;
        values[4] = this.showSummary ? Boolean.TRUE : Boolean.FALSE;
        values[5] = this.showSummarySet ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
	forVal = (String) values[1];
        showDetail = ((Boolean) values[2]).booleanValue();
        showDetailSet = ((Boolean) values[3]).booleanValue();
        showSummary = ((Boolean) values[4]).booleanValue();
        showSummarySet = ((Boolean) values[5]).booleanValue();

    }


}
