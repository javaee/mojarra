/*
 * $Id: DynamicComponentTag.java,v 1.3 2004/02/04 23:42:32 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


/**
 * <p><code>UIComponentTag</code> for <code>ChildrenComponent</code>.</p>
 */

public class DynamicComponentTag extends UIComponentTag {


    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("DynamicComponent");
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
