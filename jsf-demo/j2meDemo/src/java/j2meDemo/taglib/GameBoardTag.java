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

package j2meDemo.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GameBoardTag extends UIComponentTag { 
    private String value;
   
    public void setValue(String newValue) { value = newValue; }

    public void setProperties(UIComponent component) { 
        super.setProperties(component); 
      
        if (value != null) {
            if (isValueReference(value)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application application = context.getApplication();
                ValueBinding vb = application.createValueBinding(value);
                component.setValueBinding("value", vb);
            } else {
                component.getAttributes().put("value", value);
            }
        }
    } 

    public void release() {
        value = null;
    }

    public String getRendererType() { 
        return "j2meDemo.renderkit.GameBoard"; 
    } 
    public String getComponentType() { 
        return "j2meDemo.renderkit.GameBoard"; 
    }  
}
