/*
 * $Id: TestComponent.java,v 1.7 2003/01/17 01:47:03 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;


/**
 * <p>Test <code>UIComponent</code> for unit tests.</p>
 */

public class TestComponent extends UIComponentBase {


    public TestComponent() {
        this("test");
    }

    public TestComponent(String componentId) {
        super();
        setComponentId(componentId);
    }

    public String getComponentType() {
        return ("TestComponent");
    }


    // -------------------------------------------------- Trace-Enabled Methods


    public void decode(FacesContext context) throws IOException {
        trace("d-" + getComponentId());
        super.decode(context);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        trace("eB-" + getComponentId());
        super.encodeBegin(context);
    }


    public void encodeChildren(FacesContext context) throws IOException {
        trace("eC-" + getComponentId());
        super.encodeChildren(context);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        trace("eE-" + getComponentId());
        super.encodeEnd(context);
    }


    public boolean updateModel(FacesContext context) {
        trace("u-" + getComponentId());
        return (super.updateModel(context));
    }


    public void validate(FacesContext context) {
        trace("v-" + getComponentId());
        super.validate(context);
    }


    public void processDecodes(FacesContext context) throws IOException {
        trace("pD-" + getComponentId());
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context) {
        trace("pV-" + getComponentId());
        super.processValidators(context);
    }


    public boolean processUpdates(FacesContext context) {
        trace("pU-" + getComponentId());
        return (super.processUpdates(context));
    }


    // --------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }

    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
