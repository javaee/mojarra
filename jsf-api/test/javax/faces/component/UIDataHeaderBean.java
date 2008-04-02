/*
 * $Id: UIDataHeaderBean.java,v 1.4 2004/02/26 20:31:30 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


// Bean used to verify that command and input components in the header facet
// of a column only get processed once, instead of once per row.
public class UIDataHeaderBean {


    private int actionCount = 0;
    public int getActionCount() { return actionCount; }

    private int updateCount = 0;
    public int getUpdateCount() { return updateCount; }

    private int validateCount = 0;
    public int getValidateCount() { return validateCount; }


    // Action method for a command in the header facet
    public void action(ActionEvent event) {
	actionCount++;
    }


    // Read/write property so that we can count updates
    private String value = "Initial Value";
    public String getValue() { return value; }
    public void setValue(String value) {
	this.value = value;
	updateCount++;
    }


    // Validator method for an input in the header facet
    public void validate(FacesContext context, UIComponent component,
			 Object value) {
	validateCount++;
    }


}
