/*
 * $Id: UIDataHeaderBean.java,v 1.5 2005/08/22 22:08:18 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
