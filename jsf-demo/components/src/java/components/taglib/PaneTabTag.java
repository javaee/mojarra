/*
 * $Id: PaneTabTag.java,v 1.2 2005/08/22 22:08:57 ofung Exp $
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
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;


/**
 * This class creates a <code>PaneComponent</code> instance
 * that represents an individual tab on the overall control.
 */
public class PaneTabTag extends UIComponentTag {


    private static Log log = LogFactory.getLog(PaneTabTag.class);


    public String getComponentType() {
        return ("Pane");
    }


    public String getRendererType() {
        return ("Tab");
    }


    public void release() {
        super.release();
    }

    protected String paneClass;
    public String getPaneClass() {
	return paneClass;
    }

    public void setPaneClass(String newPaneClass) {
	paneClass = newPaneClass;
    }



    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        if (paneClass != null) {
            if (isValueReference(paneClass)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
                    createValueBinding(paneClass);
                component.setValueBinding("paneClass", vb);
            } else {
                component.getAttributes().put("paneClass", paneClass);
            }
        }

    }
}
