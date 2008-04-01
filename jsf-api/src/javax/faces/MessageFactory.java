/*
 * $Id: MessageFactory.java,v 1.2 2002/04/11 22:51:21 eburns Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Locale;

import javax.faces.FactoryConfigurationError;

/**
 * A class that knows how to create new messages. 
 * 
 * Usually this class is not used directly, but rather it is accessed indirectly
 * through the MessageList class.
 * 
 * In the default implementation the static part of the messages are stored in 
 * an XML file that can be found by the web application class loader. The search
 * for this resource is similar to the search used for ResourceBundles. The
 * normal search would be:
 * 
 * <ul>
 * <li>JSFMessages_language_country_variant.xml
 * <li>JSFMessages_language_country.xml
 * <li>JSFMessages_language.xml
 * <li>JSFMessages.xml
 * </ul>
 * 
 * Both the default resource name (JSFMessages) and the default classloader can be
 * changed.
 * 
 * <p>This is a pluggable object, so the implementation may choose to support
 * additional mechanisms for retrieving the messages.
 * 
 * @see MessageList
 */
public abstract class MessageFactory
{
    
    protected MessageFactory(){};
    
    /**
     * The class loader to use when searching for resources. In the default case
     * this will be set to the web application classloader by the JSF framework
     * as part of creating a new request level MessageList.
     */
    public abstract void setClassLoader(ClassLoader loader);
    
    /**
     * Set the the resource that these messages are based on. 
     * 
     * <p>In the default implementation this would be the name of the source file (without
     * a file extension). 
     * 
     * @param resource name of the resource that holds the static (and 
     * localized) part of the message. In the default case this
     * is JSFMessages. 
     */
    public abstract void setResource(Object resource);
    
    /**
     * Create a new message.
     * 
     * @param msgId the message id of the message
     * @param reference the object manger id of a control that is related to the message.
     * 	It may be null.
     * @param locale the locale to use. The locale controls the static message
     * text and the formatting rules. 
     * @param parms the substitution parameters for the message
     */
    public abstract Message newMessage(String msgId, String reference, Locale locale, Object[] parms) 
            throws FacesException;
    
    
    /**
     * A convenience method for creating a new Message.
     * 
     * @see MessageFactory#newMessage(String, String, Locale, Object[])
     */ 
	public Message newMessage(String msgId, String reference, Locale locale, Object parm1)
		throws FacesException
	{
		return newMessage(msgId, reference, locale, new Object[]{parm1});
	}
    
    /**
     * A convenience method for creating a new Message.
     * 
     * @see MessageFactory#newMessage(String, String, Locale, Object[])
     */ 
	public Message newMessage(String msgId, String reference, Locale locale, Object parm1, Object parm2)
		throws FacesException
	{
		return newMessage(msgId, reference, locale, new Object[]{parm1, parm2});
	}
    
}

