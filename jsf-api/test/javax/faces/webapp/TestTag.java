/*
 * $Id: TestTag.java,v 1.9 2005/08/22 22:08:32 ofung Exp $
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

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;


// Test UIComponent Tag
public class TestTag extends UIComponentTag {

    public TestTag() {
        super();
    }


    public TestTag(String componentId) {
        this(componentId, componentId);
    }

    public TestTag(String componentId, String label) {
        super();
        setId(componentId);
        setLabel(label);
    }

    private String label = null;

    public void setLabel(String label) {
        this.label = label;
    }

    private boolean rendersChildren = false;
    private boolean rendersChildrenSet = false;

    public void setRendersChildren(boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
        this.rendersChildrenSet = true;
    }

    public void release() {
        super.release();
        this.label = null;
        this.rendersChildrenSet = false;
    }

    public String getComponentType() {
        return ("TestComponent");
    }

    public String getRendererType() {
        return (null);
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (rendersChildrenSet) {
            ((TestComponent) component).setRendersChildren(rendersChildren);
        }
        if (label != null) {
            if (isValueReference(label)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
		    createValueBinding(label);
                component.setValueBinding("label", vb);
            } else {
                ((TestComponent) component).setLabel(label);
            }
        }
    }


}
