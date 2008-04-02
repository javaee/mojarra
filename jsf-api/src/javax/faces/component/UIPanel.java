/*
 * $Id: UIPanel.java,v 1.13 2003/04/29 18:51:31 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIPanel</strong> is a {@link UIComponent} that manages the
 * layout of its child components.</p>
 */

public class UIPanel extends UIOutput {


    // ------------------------------------------------------- Static Variables


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIPanel} instance with default property
     * values.</p>
     */
    public UIPanel() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------------- Properties

    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


}
