/*
 * $Id: ChildrenComponentTag.java,v 1.2 2003/09/05 18:57:09 eburns Exp $
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


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
    }


}
