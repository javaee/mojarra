/*
 * $Id: MessageResourcesFactoryImpl.java,v 1.3 2002/07/26 21:04:19 eburns Exp $
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

    static final String JSF_API_RESOURCE_FILENAME = "JSFMessages";

    static final String JSF_RI_RESOURCE_FILENAME = "JSFImplMessages";


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
	// Pre-populate our list with the required MessageResources
	// instances.
	getMessageResources(FACES_API_MESSAGES);
	getMessageResources(FACES_IMPL_MESSAGES);
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

        MessageResources messageResource = null;
	if (messageResourcesId.equals(FACES_API_MESSAGES)) {
	    messageResource = new MessageResourcesImpl(messageResourcesId,
						       JSF_API_RESOURCE_FILENAME);
	}
	else if (messageResourcesId.equals(FACES_IMPL_MESSAGES)) {
	    messageResource = new MessageResourcesImpl(messageResourcesId,
						       JSF_RI_RESOURCE_FILENAME);
	}
	else {
	    throw new IllegalArgumentException("Can't create MessageResources");
	}
	
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
