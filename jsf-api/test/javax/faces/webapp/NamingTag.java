/*
 * $Id: NamingTag.java,v 1.3 2003/05/02 05:04:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


// Test UINamingContainer Tag
public class NamingTag extends UIComponentTag {

    public String getComponentType() {
        return ("TestNamingContainer");
    }

    public String getRendererType() {
        return (null);
    }

}
