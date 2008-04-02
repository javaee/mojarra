/*
 * $Id: TestComponent.java,v 1.5 2003/12/17 15:11:32 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
