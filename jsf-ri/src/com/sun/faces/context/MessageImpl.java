/*
 * $Id: MessageImpl.java,v 1.3 2002/06/20 20:20:10 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.Message;

import java.text.MessageFormat;
import java.util.HashMap;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class MessageImpl extends Message
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
    private Object params[];
    private String reference = null;
    private String summary = null;
    private String detail = null;
    private MessageTemplate template = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageImpl(MessageTemplate template, String reference, 
            Object params[]) {
                
        ParameterCheck.nonNull(template);
        this.template = template;
        this.reference = reference;
        this.params = params;
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from Message
    //
    public String getDetail() {
        if (detail == null) {
            if (template.getDetail() == null) {
                return null;
            }
            else {
                StringBuffer b = new StringBuffer(100);
                MessageFormat mf = new MessageFormat(template.getDetail());
                if (template.getLocale() != null) {
                    mf.setLocale(template.getLocale());
                    b.append(mf.format(params));
                    detail = b.toString();
                }
            }
        }    
        return detail;
    }    

    public String getMessageId() {
        return template.getMessageId();
    }
    
    public String getReference() {
        return this.reference;
    }    

    public int getSeverity() {
        return template.getSeverity();
    }    

    public String getSummary() {
        if (summary == null) {
            if ( template.getSummary() == null ) {
                return null;
            }    
            StringBuffer b = new StringBuffer(100);
            MessageFormat mf = new MessageFormat(template.getSummary());
            if (template.getLocale() != null) {
                mf.setLocale(template.getLocale());
                b.append(mf.format(params));
                summary = b.toString();
            }    
        }
        return summary;
    }    
    
    // The testcase for this class is TestMessageListImpl.java 


} // end of class MessageImpl
