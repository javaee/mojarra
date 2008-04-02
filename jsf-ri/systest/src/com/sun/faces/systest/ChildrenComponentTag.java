/*
 * $Id: ChildrenComponentTag.java,v 1.3 2003/12/17 15:14:30 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


/**
 * <p><code>UIComponentTag</code> for <code>ChildrenComponent</code>.</p>
 */

public class ChildrenComponentTag extends UIComponentTag {


    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("ChildrenComponent");
    }


    public String getRendererType() {
        return (null);
    }


    public void release() {
        super.release();
    }


    // ------------------------------------------------------- Protected Methods


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
    }


}
