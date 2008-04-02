/*
 * $Id: UIPanel.java,v 1.14 2003/07/26 17:54:37 craigmcc Exp $
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

public interface UIPanel extends UIOutput {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren();


}
