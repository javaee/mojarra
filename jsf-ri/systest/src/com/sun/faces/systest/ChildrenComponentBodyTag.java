/*
 * $Id: ChildrenComponentBodyTag.java,v 1.3 2004/02/04 23:42:31 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentBodyTag;


/**
 * <p><code>UIComponentBodyTag</code> for <code>ChildrenComponent</code>.</p>
 */

public class ChildrenComponentBodyTag extends UIComponentBodyTag {


    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("ChildrenComponent");
    }


    public String getRendererType() {
        return (null);
    }


    // ------------------------------------------------------- Protected Methods


}
