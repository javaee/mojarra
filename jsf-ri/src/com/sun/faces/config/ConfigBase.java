/*
 * $Id: ConfigBase.java,v 1.13 2003/07/08 15:38:29 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.context.MessageResources;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;


/**
 * <p>Base bean for parsing configuration information.</p>
 */
public class ConfigBase {


    private static Log log = LogFactory.getLog(ConfigBase.class);

    // ------------------------------------------------------------ <message-resources>

    private Map messageResources = null;
    public void addMessageResources(ConfigMessageResources newResource) {
	ConfigMessageResources oldResource = null;
	String 
	    curKey = null,
	    messageResourcesId = null;
	Map 
	    oldMap = null,
	    newMap = null;
	Iterator iter;

        if (messageResources == null) {
            messageResources = new HashMap();
        }

	// If there is no message-resources-id
	if (null == 
	    (messageResourcesId = newResource.getMessageResourcesId())) {
	    // use the impl one
	    messageResourcesId = MessageResources.FACES_IMPL_MESSAGES;
	}
	    
	// if we have an existing ConfigMessageResources for this
	// messageResourcesId
	if (null != (oldResource = (ConfigMessageResources) 
		     messageResources.get(messageResourcesId))) {
	    // merge the new MessageResources content into the existing one
	    
	    // copy the new Messages into the oldResource
	    oldMap = oldResource.getMessages();
	    Assert.assert_it(null != oldMap);
	    newMap = newResource.getMessages();
	    Assert.assert_it(null != newMap);

	    iter = newMap.keySet().iterator();
	    // for each Message in the newResource
	    while (iter.hasNext()) {
		curKey = (String) iter.next();
		// put it in the oldResource
		oldMap.put(curKey, newMap.get(curKey));
	    }
	}
	else { 
	    // we don't have an existing ConfigMessageResources for this
	    // messageResourcesId
	    messageResources.put(messageResourcesId, newResource);
	}
    }
    public Map getMessageResources() {
        if (messageResources == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.messageResources);
        }
    }
}
