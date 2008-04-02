/*
 * $Id: UINamingContainer.java,v 1.8 2003/09/30 14:35:01 rlubke Exp $
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

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}     
     */ 
    public void addComponentToNamespace(UIComponent namedComponent) {

	namespace.addComponentToNamespace(namedComponent);

    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public UIComponent findComponentInNamespace(String name) {

	return namespace.findComponentInNamespace(name);

    }


    public synchronized String generateClientId() {

	return namespace.generateClientId();

    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}     
     */ 
    public void removeComponentFromNamespace(UIComponent namedComponent) {

	namespace.removeComponentFromNamespace(namedComponent);

    }


}
