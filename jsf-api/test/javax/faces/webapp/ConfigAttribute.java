/*
 * $Id: ConfigAttribute.java,v 1.1 2003/04/07 21:45:33 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for an Attribute.</p>
 */
public class ConfigAttribute extends ConfigFeature {

    private String attributeName;
    public String getAttributeName() {
        return (this.attributeName);
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    private String attributeType;
    public String getAttributeType() {
        return (this.attributeType);
    }
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

}
