/*
 * $Id: FacetComponentTag.java,v 1.2 2003/09/05 18:57:09 eburns Exp $
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


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (value != null) {
            ((FacetComponent) component).setValue(value);
        }
    }


}
