/*
 * $Id: UINamingContainer.java,v 1.13 2004/01/21 19:23:15 craigmcc Exp $
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


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.NamingContainer";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.NamingContainer";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UINamingContainer} instance with default property
     * values.</p>
     */
    public UINamingContainer() {

        super();
        setRendererType(null);

    }
}
