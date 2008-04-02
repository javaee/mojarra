/*
 * $Id: ConfigProperty.java,v 1.1 2003/04/07 21:45:35 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for an Property.</p>
 */
public class ConfigProperty extends ConfigFeature {

    private String propertyName;
    public String getPropertyName() {
        return (this.propertyName);
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    private String propertyType;
    public String getPropertyType() {
        return (this.propertyType);
    }
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

}
