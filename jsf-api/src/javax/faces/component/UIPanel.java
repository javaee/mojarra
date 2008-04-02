/*
 * $Id: UIPanel.java,v 1.26 2004/01/21 19:23:15 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UIPanel</strong> is a {@link UIComponent} that manages the
 * layout of its child components.</p>
 */

public class UIPanel extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Panel";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIPanel} instance with default property
     * values.</p>
     */
    public UIPanel() {

        super();
        setRendererType(null);

    }

}
