/*
 * $Id: ConfigAttribute.java,v 1.1 2003/10/09 16:37:14 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;


/**
 * <p>Config Bean for an Attribute.</p>
 */
public class ConfigAttribute extends Object {

    private String attributeClass;
    public String getAttributeClass() {
        return (this.attributeClass);
    }
    public void setAttributeClass(String attributeClass) {
        this.attributeClass = attributeClass;
    }

    private String attributeName;
    public String getAttributeName() {
        return (this.attributeName);
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

}
