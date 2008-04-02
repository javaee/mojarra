/*
 * $Id: ConfigFeature.java,v 1.1 2003/04/07 21:45:34 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.util.HashMap;
import java.util.Map;


/**
 * <p>Base bean for configuration beans that have common feature elements.</p>
 */
public abstract class ConfigFeature {




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

    private String smallIcon;
    public String getSmallIcon() {
        return (this.smallIcon);
    }
    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }


}
