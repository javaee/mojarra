/*
 * $Id: ConfigFeature.java,v 1.5 2005/08/22 22:08:31 ofung Exp $
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
