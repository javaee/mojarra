/*
 * $Id: UINamingContainer.java,v 1.7 2003/09/25 07:50:04 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


/**
 * <p><strong>UINamingContainer</strong> is a convenience base class for
 * components that wish to implement {@link NamingContainer} functionality.</p>
 */

public class UINamingContainer extends UIComponentBase
    implements NamingContainer {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UINamingContainer} instance with default property
     * values.</p>
     */
    public UINamingContainer() {

        super();
        setRendererType(null);

    }

 
    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    private NamingContainerSupport namespace = new NamingContainerSupport();


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
