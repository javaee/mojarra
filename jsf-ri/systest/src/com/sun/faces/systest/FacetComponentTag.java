/*
 * $Id: FacetComponentTag.java,v 1.7 2005/08/22 22:10:37 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest;


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
