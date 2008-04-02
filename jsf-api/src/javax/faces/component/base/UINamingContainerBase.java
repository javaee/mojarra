/*
 * $Id: UINamingContainerBase.java,v 1.4 2003/08/27 00:56:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * <p><strong>UINamingContainer</strong> is a convenience base class for
 * components that wish to implement {@link NamingContainer} functionality.</p>
 */

public class UINamingContainerBase extends UIComponentBase
    implements NamingContainer {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    protected NamingContainerSupport namespace = new NamingContainerSupport();


    // ------------------------------------------------- NamingContainer Methods


    public void addComponentToNamespace(UIComponent namedComponent) {
	namespace.addComponentToNamespace(namedComponent);
    }


    public UIComponent findComponentInNamespace(String name) {
	return namespace.findComponentInNamespace(name);
    }


    public synchronized String generateClientId() {
	return namespace.generateClientId();
    }

    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namespace.removeComponentFromNamespace(namedComponent);
    }


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.getState(context);
        values[1] = namespace.getState(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        namespace.restoreState(context, values[1]);

    }


}
