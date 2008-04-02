/*
 * $Id: NamingTag.java,v 1.2 2004/02/04 23:42:33 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;


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
