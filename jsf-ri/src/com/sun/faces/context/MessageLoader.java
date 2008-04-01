/*
 * $Id: MessageLoader.java,v 1.1 2002/06/07 22:55:32 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import com.sun.faces.util.Util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.HashMap;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.Message;
import java.io.IOException;
import com.sun.faces.RIConstants;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.impl.SimpleLog;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>MessageLoader</B> is responsible for loading messages from a particular
 *  resource, in this a case a XML file and storing them as a catalog of message
 *  catalogs.
 *
 * @version $Id: MessageLoader.java,v 1.1 2002/06/07 22:55:32 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.contexr.MessageCatalog
 * @see com.sun.faces.context.MesageTemplate
 *
 */

public class MessageLoader extends Object
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
    private Digester digester = null;

    // Attribute Instance Variables
    
    // The key is a String and formed from resource + language + country + varient.  
    // The value is a MessageCatalog.
    private HashMap catalogList = null;
    
   // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageLoader(){
        super();
        digester = initConfig();
        catalogList = new HashMap();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    
    public Message getMessage(String msgId, String reference, Locale locale,
            Object[] params) throws FacesException {
        
        ParameterCheck.nonNull(locale);
        ParameterCheck.nonNull(msgId);
        
        MessageCatalog catalog = findCatalog(locale);
        if (catalog == null) {
            // PENDING (visvan) log error
            throw new FacesException("No message catalogs for resource " + 
                    RIConstants.JSF_RESOURCE_FILENAME);
        }
        MessageTemplate template = (MessageTemplate) catalog.get(msgId);
        if (template == null) {
            throw new FacesException("The message id '" +
                  msgId + "' was not found in the message catalog");
        }
        return new MessageImpl(template, reference, params);
    }     
    
    private MessageCatalog findCatalog(Locale locale) {
        
        ParameterCheck.nonNull(locale);
        String[] name = new String[4];
        int i = 3;
        StringBuffer b = new StringBuffer(100);
        String resource = RIConstants.JSF_RESOURCE_FILENAME;
        b.append(resource);
        name[i--] = resource;

        if ( locale.getLanguage().length() > 0 ) {
            b.append('_');
            b.append(locale.getLanguage());
            name[i--] = b.toString();
        }    
      
        if( locale.getCountry().length() > 0 ) {
            b.append('_');
            b.append(locale.getCountry());
            name[i--] = b.toString();
        }    

        if (locale.getVariant().length() > 0) {
            b.append('_');
            b.append(locale.getVariant());
            name[i--] = b.toString();
        }

        for (int j = i+1; j < name.length; j++) {
            // if catalog does not exist it needs to be loaded from XML file.
            MessageCatalog cat = (MessageCatalog)catalogList.get(name[j]);
            if (cat == null) {
                cat = loadMessages(name[j]+".xml", locale);
                if ( cat != null ) {
                    catalogList.put(name[j], cat);
                }    
            }
            // store the loaded catalog.
            if (cat != null ){
                for (int k = i+1; k < j; k++) {
                    catalogList.put(name[k], cat);
                    return cat;
                }
            }
        }    
        return null;
    }
        
    private Digester initConfig() {
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        
        // PENDING (visvan) log level should be configurable.
        SimpleLog sLog = new SimpleLog("MessageLog");
        sLog.setLevel(SimpleLog.LOG_LEVEL_ERROR);
        digester.setLogger(sLog);
        
        digester.addObjectCreate("messages/message", 
                "com.sun.faces.context.MessageTemplate");
        digester.addSetProperties("messages/message");
        digester.addCallMethod("messages/message/detail", "setDetail", 0);
        digester.addSetNext("messages/message", "addMessage", 
                 "com.sun.faces.context.MessageTemplate");
        return digester;
    }
    
    public MessageCatalog loadMessages(String fileName, Locale locale) {
        InputStream in;
        MessageCatalog catalog = null;
        
        ParameterCheck.nonNull( fileName );
        ParameterCheck.nonNull(locale);
        
        in = this.getClass().getClassLoader().getResourceAsStream(
                fileName);
        if ( in == null ) {
            // PENDING (visvan) log error
            return null;
        }    
        
        try {
            catalog = new MessageCatalog(locale);
            digester.push( catalog );
            digester.parse(in);
            in.close();
        } catch (Throwable t) {
            return null;
           // throw new IllegalStateException(
           //         "Unable to parse file:"+t.getMessage());
        }
        return catalog;
    }    

    // The testcase for this class is TestMessageListImpl.java 
} // end of class className

