/*
 * $Id: TestComponent.java,v 1.9 2005/08/22 22:08:32 ofung Exp $
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


import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;


// Test UIComponent Class
public class TestComponent extends UIComponentBase {


    public TestComponent() {
        super();
    }


    public TestComponent(String id) {
        super();
        setId(id);
    }


    public String getFamily() {
        return ("Test");
    }


    private String label = null;

    public String getLabel() {
        if (this.label != null) {
            return (this.label);
        }
        ValueBinding vb = getValueBinding("label");
        if (vb != null) {
            return ((String) vb.getValue(getFacesContext()));
        } else {
            return (null);
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }


    private boolean rendersChildren = false;

    public boolean getRendersChildren() {
        return (this.rendersChildren);
    }

    public void setRendersChildren(boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
    }

    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/b");
        String id = getId();
        if (id != null) {
            writer.write(id);
        }
    }


    public void encodeChildren(FacesContext context) throws IOException {
        if (isRendered()) {
            super.encodeChildren(context);
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("/e");
        String id = getId();
        if (id != null) {
            writer.write(id);
        }
    }


}
