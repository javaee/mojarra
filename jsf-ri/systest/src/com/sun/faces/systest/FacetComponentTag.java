/*
 * $Id: FacetComponentTag.java,v 1.3 2003/12/17 15:14:31 rkitain Exp $
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
 * <p><code>UIComponentTag</code> for <code>FacetComponent</code>.</p>
 */

public class FacetComponentTag extends UIComponentTag {


    // -------------------------------------------------------------- Attributes


    private String value = null;
    public void setValue(String value) {
        this.value = value;
    }


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("FacetComponent");
    }


    public String getRendererType() {
        return (null);
    }


    public void release() {
        super.release();
        value = null;
    }


    // ------------------------------------------------------- Protected Methods


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (value != null) {
            ((FacetComponent) component).setValue(value);
        }
    }


}
