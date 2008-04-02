/*
 * $Id: ConfigMessage.java,v 1.1 2003/05/01 18:04:02 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>Config Bean for a Message.</p>
 */
public class ConfigMessage extends ConfigFeature {

    private String messageId;
    public String getMessageId() {
        return (this.messageId);
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private String messageClass;
    public String getMessageClass() {
        return (this.messageClass);
    }
    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }

}
