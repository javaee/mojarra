/*
 * $Id: UINamingContainerBase.java,v 1.2 2003/07/26 17:54:50 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;


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
    private NamingContainer namespace = new NamingContainerSupport();


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


}
