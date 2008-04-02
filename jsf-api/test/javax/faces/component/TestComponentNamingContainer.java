/*
 * $Id: TestComponentNamingContainer.java,v 1.9 2003/06/20 23:28:50 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;


/**
 * <p>Test <code>UIComponent</code> for unit tests.</p>
 */

public class TestComponentNamingContainer extends UINamingContainer {


    public TestComponentNamingContainer() {
        this("test");
    }

    public TestComponentNamingContainer(String componentId) {
        super();
        setComponentId(componentId);
    }

    public String getComponentType() {
        return ("TestComponent");
    }


    // -------------------------------------------------- Trace-Enabled Methods


    public void decode(FacesContext context) throws IOException {
        TestComponent.trace("d-" + getComponentId());
        super.decode(context);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        TestComponent.trace("eB-" + getComponentId());
        super.encodeBegin(context);
    }


    public void encodeChildren(FacesContext context) throws IOException {
        TestComponent.trace("eC-" + getComponentId());
        super.encodeChildren(context);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        TestComponent.trace("eE-" + getComponentId());
        super.encodeEnd(context);
    }


    public void updateModel(FacesContext context) {
        TestComponent.trace("u-" + getComponentId());
        super.updateModel(context);
    }


    public void processDecodes(FacesContext context) throws IOException {
        TestComponent.trace("pD-" + getComponentId());
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context) {
        TestComponent.trace("pV-" + getComponentId());
        super.processValidators(context);
    }


    public void processUpdates(FacesContext context) {
        TestComponent.trace("pU-" + getComponentId());
        super.processUpdates(context);
    }

}
