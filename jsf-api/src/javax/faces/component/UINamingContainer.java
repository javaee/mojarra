/*
 * $Id: UINamingContainer.java,v 1.4 2003/02/20 22:46:12 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UINamingContainer</strong> is a convenience base class for
 * components that wish to implement {@link NamingContainer} functionality.</p>
 */

public class UINamingContainer extends UIComponentBase
    implements NamingContainer {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE =
        "javax.faces.component.UINamingContainer";


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    private NamingContainer namespace = new NamingContainerSupport();


    // ------------------------------------------------------------- Properties


    public String getComponentType() {
	
        return (TYPE);
	
    }
    

    // ------------------------------------------------ NamingContainer Methods


    public void addComponentToNamespace(UIComponent namedComponent) {
	namespace.addComponentToNamespace(namedComponent);
    }


    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namespace.removeComponentFromNamespace(namedComponent);
    }


    public UIComponent findComponentInNamespace(String name) {
	return namespace.findComponentInNamespace(name);
    }


    public synchronized String generateClientId() {
	return namespace.generateClientId();
    }


}
