/*
 * $Id: ConfigMessage.java,v 1.3 2003/07/22 19:44:39 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Collections;

import javax.faces.application.Message;
import javax.faces.context.FacesContext;

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
	if (language == null) {
	    Locale locale = null;
	    FacesContext context = FacesContext.getCurrentInstance();
	    if (context != null) {
                locale = context.getLocale();
	    } else {
		locale = Locale.getDefault();
            }
	    language = locale.getLanguage();
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
