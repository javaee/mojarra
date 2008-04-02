/*
 * $Id: ConfigFeature.java,v 1.3 2004/02/04 23:39:34 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Base bean for configuration beans that have common feature elements.</p>
 */
public abstract class ConfigFeature {


    private Map attributes = null;
    public void addAttribute(ConfigAttribute attribute) {
        if (attributes == null) {
            attributes = new HashMap();
        }
        attributes.put(attribute.getAttributeName(), attribute);
    }
    public Map getAttributes() {
        if (attributes == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (attributes);
        }
    }


    private String description;
    public String getDescription() {
        return (this.description);
    }
    public void setDescription(String description) {
        this.description = description;
    }


    private String displayName;
    public String getDisplayName() {
        return (this.displayName);
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    private String largeIcon;
    public String getLargeIcon() {
        return (this.largeIcon);
    }
    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }


    private Map properties = null;
    public void addProperty(ConfigProperty property) {
        if (properties == null) {
            properties = new HashMap();
        }
        properties.put(property.getPropertyName(), property);
    }
    public Map getProperties() {
        if (properties == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (properties);
        }
    }


    private String smallIcon;
    public String getSmallIcon() {
        return (this.smallIcon);
    }
    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }


}
