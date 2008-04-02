/*
 * $Id: ConfigMessageResources.java,v 1.2 2003/05/18 20:54:45 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import org.mozilla.util.Assert;


/**
 * <p>Config Bean for a MessageResources instance.</p>
 */
public class ConfigMessageResources extends ConfigFeature {

    private String messageResourcesId;
    public String getMessageResourcesId() {
        return (this.messageResourcesId);
    }
    public void setMessageResourcesId(String messageResourcesId) {
        this.messageResourcesId = messageResourcesId;
    }

    private String messageResourcesClass;
    public String getMessageResourcesClass() {
        return (this.messageResourcesClass);
    }
    public void setMessageResourcesClass(String messageResourcesClass) {
        this.messageResourcesClass = messageResourcesClass;
    }

    private Map messages = null;
    public void addMessage(ConfigMessage newMessage) {
	ConfigMessage oldMessage = null;
	Map oldMap, newMap = null;
	Iterator iter = null;
	String curKey = null;

        if (messages == null) {
            messages = new HashMap();
        }

	// if we have an existing ConfigMessage under this messageId
	if (null != (oldMessage = 
		    (ConfigMessage) messages.get(newMessage.getMessageId()))) {
	    // merge the new message's content into the existing one
	    
	    // if the severities are different, take the new one
	    if (oldMessage.getSeverity() != newMessage.getSeverity()) {
		oldMessage.setSeverity(newMessage.getSeverity());
	    }
	    
	    // copy the summaries into the oldMessage
	    oldMap = oldMessage.getSummaries();
	    Assert.assert_it(null != oldMap);
	    newMap = newMessage.getSummaries();
	    Assert.assert_it(null != newMap);

	    iter = newMap.keySet().iterator();
	    // for each summary in the newMessage
	    while (iter.hasNext()) {
		curKey = (String) iter.next();
		// put it in the oldMessage
		oldMap.put(curKey, newMap.get(curKey));
	    }

	    // copy the details into the oldMessage
	    oldMap = oldMessage.getDetails();
	    Assert.assert_it(null != oldMap);
	    newMap = newMessage.getDetails();
	    Assert.assert_it(null != newMap);
	    
	    iter = newMap.keySet().iterator();
	    // for each detail in the newMessage
	    while (iter.hasNext()) {
		curKey = (String) iter.next();
		// put it in the oldMessage
		oldMap.put(curKey, newMap.get(curKey));
	    }
	}
	else {
	    // we don't have an existing ConfigMessage for this messageId
	    messages.put(newMessage.getMessageId(), newMessage);
	}
    }

    public Map getMessages() {
        if (messages == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.messages);
        }
    }

}
