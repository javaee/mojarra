/*
 * $Id: TestComponent.java,v 1.2 2003/04/29 18:52:03 eburns Exp $
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


// Test UIComponent Class
public class TestComponent extends UIComponentBase {


    public TestComponent() {
        super();
    }


    public TestComponent(String componentId) {
        super();
        setComponentId(componentId);
    }


    private String label = null;

    public String getLabel() {
        return (this.label);
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
        String componentId = getComponentId();
        if (componentId != null) {
            writer.write(componentId);
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
        String componentId = getComponentId();
        if (componentId != null) {
            writer.write(componentId);
        }
    }


}
