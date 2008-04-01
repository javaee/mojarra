/*
 * $Id: MessageImpl.java,v 1.1 2002/05/28 18:20:39 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageList;

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
    private String messageId = null;
    private MessageList messageList = null;
    private Object params[] = null;
    private String reference = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageImpl(MessageList messageList, String messageId,
                String reference, Object params[]) {

        this.messageList = messageList;
        this.messageId = messageId;
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
        // PENDING (visvan) 
        return null;
    }    

    public String getMessageId() {
        return this.messageId;
    }
    
    public String getReference() {
        return this.reference;
    }    

    public int getSeverity() {
        // PENDING (visvan) 
        return Message.SEVERITY_INFO;
    }    

    public String getSummary() {
        // PENDING (visvan) 
        return null;
    }    
    
    // The testcase for this class is TestclassName.java 


} // end of class MessageImpl
