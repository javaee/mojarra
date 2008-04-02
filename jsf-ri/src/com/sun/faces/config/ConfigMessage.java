/*
 * $Id: ConfigMessage.java,v 1.2 2003/05/18 20:54:44 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import javax.faces.application.Message;

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

    private int severity = Message.SEVERITY_ERROR;
    public int getSeverity() {
        return (this.severity);
    }
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    private HashMap summaries;
    public Map getSummaries() {
	if (null == summaries) {
            return (Collections.EMPTY_MAP);
	}
	return summaries;
    }

    public void addSummary(String language, String summary) {
	if (null == summaries) {
	    summaries = new HashMap();
	}
	summaries.put(language, summary);
    }

    private HashMap details;
    public Map getDetails() {
	if (null == details) {
            return (Collections.EMPTY_MAP);
	}
	return details;
    }

    public void addDetail(String language, String detail) {
	if (null == details) {
	    details = new HashMap();
	}
	details.put(language, detail);
    }



}
