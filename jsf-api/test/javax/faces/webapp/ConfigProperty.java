/*
 * $Id: ConfigProperty.java,v 1.3 2004/02/04 23:39:34 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for an Property.</p>
 */
public class ConfigProperty extends ConfigFeature {

    private String propertyClass;
    public String getPropertyClass() {
        return (this.propertyClass);
    }
    public void setPropertyClass(String propertyClass) {
        this.propertyClass = propertyClass;
    }

    private String propertyName;
    public String getPropertyName() {
        return (this.propertyName);
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
