/*
 * $Id: MessageResourcesFactoryImpl.java,v 1.1 2002/06/25 20:47:56 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;

import javax.faces.context.MessageResources;
import javax.faces.context.MessageResourcesFactory;
import javax.faces.FacesException;

public class MessageResourcesFactoryImpl extends MessageResourcesFactory
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
    private HashMap messageResourcesList = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageResourcesFactoryImpl()
    {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from MessagesResurcesFactory
    //
    public void addMessageResources (String messageResourcesId, 
            MessageResources messageResources) {
        if ( messageResources == null || messageResourcesId == null ) {
            throw new NullPointerException("One or more parameters could be null");
        }
        
        if ( messageResourcesList == null ) {
            messageResourcesList = new HashMap();
        } else if ( messageResourcesList.containsKey(messageResourcesId)) {
            throw new IllegalArgumentException("messageResourceId " + 
                messageResourcesId + " already exists");
        }
        synchronized ( messageResourcesList ) {
            messageResourcesList.put(messageResourcesId, messageResources);
        }    
    }             

    public MessageResources getMessageResources (String messageResourcesId) 
            throws FacesException {
        if ( messageResourcesId == null ) {
            throw new NullPointerException("messageResourcesId connot be null");
        }
      
        if ( messageResourcesList == null ) {
            messageResourcesList = new HashMap();
        }    
        
        if ( messageResourcesList.containsKey(messageResourcesId)) {
            return ((MessageResources)
                    messageResourcesList.get(messageResourcesId));
        } 
        // PENDING (visvan) throw FacesException/IllegalArg Exception ??
        MessageResources messageResource = 
                new MessageResourcesImpl(messageResourcesId);
        synchronized ( messageResourcesList ) { 
            messageResourcesList.put(messageResourcesId, messageResource);
        }    
        return messageResource;
    }            

    public Iterator getMessageResourcesIds() {
        if (messageResourcesList != null ) {
	    return (messageResourcesList.keySet()).iterator();
	} 
	return Collections.EMPTY_LIST.iterator();
    }
    
    // The testcase for this class is TestMessageResourcesFactoryImpl.java 


} // end of class MessageResourcesFactoryImpl
