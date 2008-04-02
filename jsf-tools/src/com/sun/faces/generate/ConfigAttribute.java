/*
 * $Id: ConfigAttribute.java,v 1.2 2003/11/04 18:38:32 rkitain Exp $
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

    private String description;
    public String getDescription() {
        return (this.description);
    }
    public void setDescription(String description) {
        this.description = description;
    }

    private String tagAttribute;
    public String getTagAttribute() {
        return (this.tagAttribute);
    }
    public void setTagAttribute(String tagAttribute) {
        this.tagAttribute = tagAttribute;
    }
}
