/*
 * $Id: NamingTag.java,v 1.2 2003/04/29 18:52:03 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


// Test UINamingContainer Tag
public class NamingTag extends FacesTag {

    public String getComponentType() {
        return ("TestNamingContainer");
    }

    public String getRendererType() {
        return (null);
    }

}
