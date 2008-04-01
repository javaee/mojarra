/*
 * $Id: MessageListImpl.java,v 1.1 2002/05/28 18:20:39 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.context.MessageList;
import javax.faces.context.Message;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class MessageListImpl extends MessageList
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
    private ArrayList messageList = new ArrayList();
    private FacesContext facesContext = null;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageListImpl(FacesContext fc) {
        this.facesContext = fc;
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from MessageList
    //
    public void add(String messageId) {
        add(messageId, null, null);
    }


    public void add(String messageId, Object params[]) {
        add(messageId, null, params);
    }


    public void add(String messageId, String reference) {
        add(messageId, reference, null);
    }


    public void add(String messageId, String reference, Object params[]) {
        if (messageList != null ) {
            messageList = new ArrayList();
        }
        // PENDING (visvan) clean up after the Message API gets final. 
        Message msg = new MessageImpl(this,messageId, reference, params);
        messageList.add(msg);
    }


    public Iterator iterator() {
        return (messageList.iterator()); 
    }


    public Iterator iterator(String reference) {
        ArrayList list = new ArrayList(messageList.size());
    	Iterator it = iterator();
    	while(it.hasNext()) {
    	    Message m = (Message)it.next();
    	    if (reference == null) {
                if (m.getReference() == null) {
                        list.add(m);
                }    
            }
    	    else {
                if (reference.equals(m.getReference())) {
                    list.add(m);
                }    
            }
    	}
    	return list.iterator();    
    }


    public int size() {
        return messageList.size();
    }
   
    // The testcase for this class is TestclassName.java 


} // end of class MessageListImpl
