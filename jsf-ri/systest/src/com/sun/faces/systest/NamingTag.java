/*
 * $Id: NamingTag.java,v 1.3 2004/02/06 18:56:04 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import javax.faces.webapp.UIComponentTag;


/**
 * <p><code>UIComponentTag</code> for a <code>UINamingContainer</code>
 * component.</p>
 */

public class NamingTag extends UIComponentTag {


    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("NamingContainer");
    }


    public String getRendererType() {
        return (null);
    }


    // ------------------------------------------------------- Protected Methods


}
