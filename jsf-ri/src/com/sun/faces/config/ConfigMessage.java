/*
 * $Id: ConfigMessage.java,v 1.4 2003/09/03 18:53:38 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.util.Util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Collections;

import javax.faces.application.Message;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

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
    public void setSeverity(String severity) {
        // if not info, warn, or fatal, leave the default of error
        if ("info".equals(severity)) 
            this.severity = Message.SEVERITY_INFO;
        else if ("warn".equals(severity)) 
            this.severity = Message.SEVERITY_WARN;            
        else if ("fatal".equals(severity))
            this.severity = Message.SEVERITY_FATAL;
        else if ("error".equals(severity)) 
            this.severity = Message.SEVERITY_ERROR;
        else
            throw new FacesException(
                Util.getExceptionMessage(Util.INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID,
                    new Object[] { severity }));                            
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
