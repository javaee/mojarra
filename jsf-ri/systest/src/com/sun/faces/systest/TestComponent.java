/*
 * $Id: TestComponent.java,v 1.6 2004/02/04 23:42:33 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;


/**
 * <p>Test <code>UIComponent</code> for sys tests.</p>
 */

public class TestComponent extends UIInput {


    public TestComponent() {
        this("test");
    }

    public TestComponent(String componentId) {
        super();
        setId(componentId);
    }

    public String getComponentType() {
        return ("TestComponent");
    }


    // -------------------------------------------------- Trace-Enabled Methods


    public void decode(FacesContext context) {
        trace("d-" + getId());
        super.decode(context);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        trace("eB-" + getId());
        super.encodeBegin(context);
    }


    public void encodeChildren(FacesContext context) throws IOException {
        trace("eC-" + getId());
        super.encodeChildren(context);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        trace("eE-" + getId());
        super.encodeEnd(context);
    }


    public void updateModel(FacesContext context) {
        trace("u-" + getId());
        super.updateModel(context);
    }


    public void validate(FacesContext context) {
        trace("v-" + getId());
        super.validate(context);
    }


    public void processDecodes(FacesContext context) {
        trace("pD-" + getId());
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context) {
        trace("pV-" + getId());
        super.processValidators(context);
    }


    public void processUpdates(FacesContext context) {
        trace("pU-" + getId());
        super.processUpdates(context);
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
