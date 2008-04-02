/*
 * $Id: NamingTag.java,v 1.1 2003/03/13 22:02:37 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


// Test UINamingContainer Tag
public class NamingTag extends FacesTag {

    public UIComponent createComponent() {
        return (new TestNamingContainer());
    }

    public String getRendererType() {
        return (null);
    }

}
