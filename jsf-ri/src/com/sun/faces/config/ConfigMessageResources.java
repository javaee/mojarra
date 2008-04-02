/*
 * $Id: ConfigMessageResources.java,v 1.1 2003/05/01 18:04:02 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


/**
 * <p>Config Bean for a MessageResources instance.</p>
 */
public class ConfigMessageResources extends ConfigFeature {

    private String messageResourcesId;
    public String getMessageResourcesId() {
        return (this.messageResourcesId);
    }
    public void setMessageResourcesId(String messageResourcesId) {
        this.messageResourcesId = messageResourcesId;
    }

    private String messageResourcesClass;
    public String getMessageResourcesClass() {
        return (this.messageResourcesClass);
    }
    public void setMessageResourcesClass(String messageResourcesClass) {
        this.messageResourcesClass = messageResourcesClass;
    }

    private Map messages = null;
    public void addMessage(ConfigMessage message) {
        if (messages == null) {
            messages = new HashMap();
        }
        messages.put(message.getMessageId(), message);
    }
    public Map getMessages() {
        if (messages == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.messages);
        }
    }

}
