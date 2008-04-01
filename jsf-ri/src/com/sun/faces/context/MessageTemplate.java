/*
 * $Id: MessageTemplate.java,v 1.1 2002/06/07 22:55:32 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.Locale;
import javax.faces.context.Message;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class MessageTemplate extends Object 
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    
    private String  messageId = null;
    private int  severity = Message.SEVERITY_INFO;
    private String summary = null;
    private String detail = null;
    private Locale locale = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageTemplate() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    
    /**
     * Gets the severity
     * @return Returns a int
     */
    public int getSeverity() {
        return severity;
    }
    
    /**
     * Sets the severity
     * @param severity The severity to set
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * Gets summary
     * @return Returns a String
     */
    public String getSummary() {
        return summary;
    }
    /**
     * Sets summary
     * @param summary The summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets the messageId
     * @return Returns a String
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the id
     * @param id The id to set
     */
    public void setMessageId(String id) {
        messageId = id;
    }

    /**
     * Gets the locale
     * @return Returns a Locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Sets the locale
     * @param locale The locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the detail
     * @return Returns a String
     */
    public String getDetail() {
        return detail;
    }
    /**
     * Sets the detail
     * @param detail The detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
   
} // end of class MessageTemplate
