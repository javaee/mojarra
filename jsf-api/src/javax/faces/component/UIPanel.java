/*
 * $Id: UIPanel.java,v 1.23 2004/01/08 21:21:11 eburns Exp $
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


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIPanel} instance with default property
     * values.</p>
     */
    public UIPanel() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------- UIComponent Properties


    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }
}
