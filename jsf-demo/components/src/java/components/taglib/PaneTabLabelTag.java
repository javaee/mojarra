/*
 * $Id: PaneTabLabelTag.java,v 1.2 2005/08/22 22:08:57 ofung Exp $
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

package components.taglib;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;


/**
 * This class creates a <code>PaneComponent</code> instance
 * that represents a tab button control on the tab pane.
 */
public class PaneTabLabelTag extends UIComponentTag {

    private static Log log = LogFactory.getLog(PaneTabLabelTag.class);


    private String commandName = null;


    public void setCommandName(String newCommandName) {
        commandName = newCommandName;
    }


    private String image = null;


    public void setImage(String newImage) {
        image = newImage;
    }


    private String label = null;


    public void setLabel(String newLabel) {
        label = newLabel;
    }


    public String getComponentType() {
        return ("Pane");
    }


    public String getRendererType() {
        return ("TabLabel");
    }

    protected String paneTabLabelClass;
    public String getPaneTabLabelClass() {
	return paneTabLabelClass;
    }

    public void setPaneTabLabelClass(String newPaneTabLabelClass) {
	paneTabLabelClass = newPaneTabLabelClass;
    }

    public void release() {
        super.release();
        this.commandName = null;
        this.image = null;
        this.label = null;
    }


    protected void setProperties(UIComponent component) {

        super.setProperties(component);

        if (commandName != null) {
            if (isValueReference(commandName)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(commandName);
                component.setValueBinding("commandName", vb);
            } else {
                component.getAttributes().put("commandName", commandName);
            }
        }

        if (image != null) {
            if (isValueReference(image)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(image);
                component.setValueBinding("image", vb);
            } else {
                component.getAttributes().put("image", image);
            }
        }

        if (label != null) {
            if (isValueReference(label)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(label);
                component.setValueBinding("label", vb);
            } else {
                component.getAttributes().put("label", label);
            }
        }

        if (paneTabLabelClass != null) {
            if (isValueReference(paneTabLabelClass)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(paneTabLabelClass);
                component.setValueBinding("paneTabLabelClass", vb);
            } else {
                component.getAttributes().put("paneTabLabelClass", 
					      paneTabLabelClass);
            }
        }

    }


}
