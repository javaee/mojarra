/*
 * $Id: UIPanelBase.java,v 1.2 2003/07/26 17:54:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UIPanel;


/**
 * <p><strong>UIPanelBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIPanel}.</p>
 */

public class UIPanelBase extends UIOutputBase implements UIPanel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIPanelBase} instance with default property
     * values.</p>
     */
    public UIPanelBase() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    public boolean getRendersChildren() {

        return (true);

    }


}
