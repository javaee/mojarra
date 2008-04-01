/*
 * $Id: MessageList.java,v 1.2 2002/04/05 19:40:16 jvisvanathan Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.FactoryFinder.ConfigurationError;

/**
 * A centralized place to store messages, that are are usually shown to an
 * end user. It manages objects of the type Message.
 * 
 * <p>Normal usage:
 * 
 * <p>An application developer would place the static part of their messages
 * in the file WEB-INF/classes/JSFMessages.xml.  They could have
 * as many localized versions of this file that they required, for
 * example, if the application served people from the People's Republic of China,
 * there could be a file called JSFMessages_zh_CN.xml.
 * 
 * <p>There is one central message list for each request. This message list
 * is setup by the JSF framework. It is available from the FacesContext.
 * It is also available from the ObjectManager with the key
 * MessageList.MESSAGE_LIST_ID. This message list is associated with the locale
 * of the request that caused it to be created. In addition to this centralized
 * message list, there is no restriction on the developer creating other message
 * lists.
 * 
 * <p>When the application wants to add a message they simply use one of the
 * addMessage methods. For example:
 * 
 * <p><code>addMessage("PMT0001", null, "62.50")</code>
 * 
 * <p>Other parts of the application can determine the state of the request by
 * using the <code>highestSeverity</code> method. 
 * 
 * <p>Lastly the renders display the messages in a user friendly way to the 
 * end user.
 * 
 * <p>While this describes the normal usage, message lists are pluggable objects
 * and individal implementations may add different usage patterns. For example
 * the application developer may not need to use the XML feature as the messages
 * may be available from some other source. 
 * 
 * 
 * @see Message
 */
public abstract class MessageList
{
	protected MessageList(){};
	
	/**
	 * Answer a new instance of a MessageList. The concrete instance that is
	 * returned depends on the "javax.faces.MessageList" property.
	 */
	public static MessageList newInstance()
            throws FactoryConfigurationError  
	{
        try 
        {
            return (MessageList) FactoryFinder.find(
               /* The default property name according to the JSFaces spec */
               "javax.faces.MessageList",
               /* The fallback implementation class name */
               "com.sun.faces.MessageListImpl");
        } catch (FactoryFinder.ConfigurationError fe) 
        {
            throw new FactoryConfigurationError(fe.getException(), fe.getMessage());
        }
    }
        
    /**
     * Create a new message and add it to this list. 
     * 
     * @param msgId the message id of the message
     * @param reference the object manger id of a control that is related to the message.
     * 	It may be null.
     * @param parms the substitution parameters for the message
     */
    public abstract Message addMessage(String msgId, String reference, Object[] parms) 
            throws FacesException;
    
    
    /**
     * A convenience method for creating a new Message and adding it to this list.
     * 
     * @see MessageList#addMessage(String, String, Object[])
     */ 
	public Message addMessage(String msgId, String reference, Object parm1)
		throws FacesException
	{
		return addMessage(msgId, reference, new Object[]{parm1});
	}
    
    /**
     * A convenience method for creating a new Message and adding it to this list.
     * 
     * @see MessageList#addMessage(String, String, Object[])
     */ 
	public Message addMessage(String msgId, String reference, Object parm1, Object parm2)
		throws FacesException
	{
		return addMessage(msgId, reference, new Object[]{parm1, parm2});
	}
    
    /**
     * A convenience method for creating a new Message and adding it to this list.
     * 
     * @see MessageList#addMessage(String, String, Object[])
    */ 
	public Message addMessage(String msgId, String reference, Object parm1, Object parm2,
		Object parm3)
		throws FacesException
	{
		return addMessage(msgId, reference, new Object[]{parm1, parm2, parm3});
	}
	
	/**
	 * Add a message to the list.
	 */
	public abstract void addMessage(Message msg);
    
    /**
     * A convenience method for creating a new Message and adding it to this list.
     * 
     * @see MessageList#addMessage(String, String, Object[])
     */ 
	public Message addMessage(String msgId, String reference, Object parm1, Object parm2,
		Object parm3, Object parm4)
		throws FacesException
	{
		return addMessage(msgId, reference, new Object[]{parm1, parm2, parm3, parm4});
	}
    
    /**
     * Answer the highest severity for all of the messages in the list.
     */
    public abstract int highestSeverity();
    
    /**
     * Answer true if the request is valid.
     */
    public boolean isValid()
    {
    	return highestSeverity() < Message.SEV_ERROR;
    }
    
    /**
     * Answer all the messages in the list.
     * 
     * @return an iteration of Message's
     */
    public abstract Iterator iterator();
    
    /**
     * Answer all the messages that have this reference.
     * 
     * @param reference usually the object manager id of a UIControl. If it is
     * null then it will return all of the messages that don't have any references.
     * 
     * @return an iteration of Message's
     */
    public abstract Iterator iterator(String reference);
    
   	/**
   	 * Set the locale for the message list. Add messages added to the
   	 * list will use this locale. 
   	 * 
   	 * <p>For the centralized message list, the locale of the request will
   	 * be set by the JSF framework.
   	 */
    public abstract void setLocale(Locale locale);
    
    /**
     * Set the message factory for this List.  If this method is not called
     * then the default message factory will be used. This is usually appropriate
     * but if you need more control over the message factory, you can explicitly
     * set it. 
     * 
     * @param factoryId - an Object manager id, that resolves to a MessageFactory.
     * 
     * @see MessageFactory
     */
    public abstract void setMessageFactory(String factoryId);
}

