/*
 * $Id: MessageResourcesImpl.java,v 1.14 2003/07/24 23:24:15 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.faces.FacesException;
import javax.faces.application.MessageImpl;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import java.text.MessageFormat;
import java.io.IOException;

import org.apache.commons.logging.impl.SimpleLog;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class MessageResourcesImpl extends MessageResources
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
    private String messageResourceId = null;
    private String resourceFile = null;
    
    // The key is a String and formed from resource + language + country + varient.  
    // The value is a MessageCatalog.
    private HashMap catalogList = null;
     
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageResourcesImpl() {
        catalogList = new HashMap();
    }

    public MessageResourcesImpl(String messageResourceId) {
	ParameterCheck.nonNull(messageResourceId);
	this.messageResourceId = messageResourceId;
        catalogList = new HashMap();
    }
    
    //
    // Class methods
    //

    //
    // General Methods
    //
    
    public MessageCatalog findCatalog(Locale locale) {
       
        MessageCatalog cat = null;
 
        ParameterCheck.nonNull(locale);
        String[] name = new String[3];
        int i = 2;
        StringBuffer b = new StringBuffer(100);
        if ( locale.getLanguage().length() > 0 ) {
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
            // start with variant and iterate
            // until a catalog is found to match locale.
            synchronized( catalogList ) {
                cat = (MessageCatalog)catalogList.get(name[j]);
                if (cat == null) {
                    continue;
                } else {  
                    break;
                }
            }    
        }    
        return cat;
    }
        
    public void addCatalog(Locale locale, MessageCatalog catalog) {
        ParameterCheck.nonNull(locale);
        ParameterCheck.nonNull(catalog);

        if (catalogList == null) {
	    catalogList = new HashMap();
	}

	if (findCatalog(locale) != null) {
	    return;
	}

        String[] name = new String[3];
        int i = 2;
        StringBuffer b = new StringBuffer(100);
        if ( locale.getLanguage().length() > 0 ) {
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
	    catalogList.put(name[j], catalog);
	}
    }

    public String substituteParams(Locale locale, String msgtext, Object params[]) {
        String localizedStr = null;
        
        if (params == null || msgtext == null ) {
            return msgtext;
        }    
        StringBuffer b = new StringBuffer(100);
        MessageFormat mf = new MessageFormat(msgtext);
        if (locale != null) {
            mf.setLocale(locale);
            b.append(mf.format(params));
            localizedStr = b.toString();
        }    
        return localizedStr;
    }

    /**

    * This version of getMessage() is used in the RI for localizing RI
    * specific messages.

    */

    public Message getMessage(String messageId, Object params[]) {
        Locale locale = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            locale = FacesContext.getCurrentInstance().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }
        
	return getMessage(locale, messageId, params);
    }

    protected Message getMessage(Locale locale, String messageId, 
				 Object params[]) {
        MessageCatalog catalog = findCatalog(locale);
        if (catalog == null) {
	    return null;
        }
        MessageTemplate template = (MessageTemplate) catalog.get(messageId);
        if (template == null) {
	    return null;
        }
        
        // substitute parameters
        String summary = substituteParams(locale, template.getSummary(), params);
        String detail = substituteParams(locale, template.getDetail(),params);
        return (new MessageImpl(template.getSeverity(), summary, detail));
    }


    //
    // Methods from MessageResources
    // 
    public Message getMessage(FacesContext context, String messageId) {
        return getMessage(context, messageId, null);
    }    

    public Message getMessage(FacesContext context, String messageId,
			      Object params[]) {
        if (context == null || messageId == null ) {
            throw new NullPointerException("One or more parameters could be null");
        }
        
        Locale locale = context.getLocale();
        Assert.assert_it(locale != null);
//        return getMessage(locale, messageId, params);
        Message message = getMessage(locale, messageId, params);
        if (message != null) {
            return message;
        }
        locale = Locale.getDefault();
        return (getMessage(locale, messageId, params));
    }  
    
    public Message getMessage(FacesContext context, String messageId,
                                       Object param0) {
        return getMessage(context, messageId, new Object[]{param0});                                       
    }                                       
    
    public Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1) {
         return getMessage(context, messageId, new Object[]{param0, param1});                                        
    }                                       

    public Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2) {
         return getMessage(context, messageId, 
             new Object[]{param0, param1, param2});                                        
    }                                       

    public Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2, Object param3) {
         return getMessage(context, messageId, 
                 new Object[]{param0, param1, param2, param3});                                        
    }                                       

    // The testcase for this class is TestclassName.java 

} // end of class MessageResourcesImpl
