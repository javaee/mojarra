/*
 * $Id: TestComponentNamingContainer.java,v 1.5 2003/01/17 00:26:51 craigmcc Exp $
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


    public boolean updateModel(FacesContext context) {
        TestComponent.trace("u-" + getComponentId());
        return (super.updateModel(context));
    }


    public boolean validate(FacesContext context) {
        TestComponent.trace("v-" + getComponentId());
        return (super.validate(context));
    }


    public void processDecodes(FacesContext context) throws IOException {
        TestComponent.trace("pD-" + getComponentId());
        super.processDecodes(context);
    }


    public boolean processValidators(FacesContext context) {
        TestComponent.trace("pV-" + getComponentId());
        return (super.processValidators(context));
    }


    public boolean processUpdates(FacesContext context) {
        TestComponent.trace("pU-" + getComponentId());
        return (super.processUpdates(context));
    }

}
