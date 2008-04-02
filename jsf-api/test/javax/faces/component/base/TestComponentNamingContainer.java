/*
 * $Id: TestComponentNamingContainer.java,v 1.3 2003/09/23 20:35:54 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;


/**
 * <p>Test <code>UIComponent</code> for unit tests.</p>
 */

public class TestComponentNamingContainer extends UINamingContainerBase {


    public TestComponentNamingContainer() {
        super();
    }

    public TestComponentNamingContainer(String id) {
        super();
        setId(id);
    }


    // -------------------------------------------------- Trace-Enabled Methods


    public void decode(FacesContext context) {
        TestComponent.trace("d-" + getId());
        super.decode(context);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        TestComponent.trace("eB-" + getId());
        super.encodeBegin(context);
    }


    public void encodeChildren(FacesContext context) throws IOException {
        TestComponent.trace("eC-" + getId());
        super.encodeChildren(context);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        TestComponent.trace("eE-" + getId());
        super.encodeEnd(context);
    }


    public void processDecodes(FacesContext context) {
        TestComponent.trace("pD-" + getId());
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context) {
        TestComponent.trace("pV-" + getId());
        super.processValidators(context);
    }


    public void processUpdates(FacesContext context) {
        TestComponent.trace("pU-" + getId());
        super.processUpdates(context);
    }

}
