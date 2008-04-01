/*
 * $Id: Message.java,v 1.1 2002/03/15 20:49:21 jvisvanathan Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.Serializable;

/**
 * Represents a Message that can be displayed to an end user. Often the message
 * will be fully localized.
 * 
 * <p>The mostly commonly used method will be the <code>toString()</code> method. This
 * method returns a String that can be displayed to an end user. It is
 * recommended that this String not include any HTML markup. 
 */
public interface Message extends Serializable
{
	/** 
	 * This message severity indicates that the message is for informational purposes
	 * only and does not indicate that an error has occured.
	 */
	public final int 	SEV_INFO	= 0;
	
	/**
	 * This message severity indicates that an error <b>may</b> have occurred. It 
	 * is likely that the processing can continue. The default processing of JSF 
	 * considers requests with only warnings as sucessful requests.
	 */
	public final int	SEV_WARN 	= 10;
	
	/**
	 * This message severity indicates that an error <b>probably</b> has occurred.
	 * It is unlikely that the request can be completed successfully.
	 */ 
	public final int	SEV_ERROR 	= 20;
	
	/**
	 * This message severity indicates that a very serious error has occured. The
	 * request can not be completed.
	 */
	public final int	SEV_FATAL 	= 30;
	
	/**
	 * Answer the severity of this message.
	 * 
	 * @return one of the SEV_XXX constants.
	 */
	public int getSeverity();
	
	/**
	 * Answer the first level text before parameter substitution has occurred. This
	 * is typically a brief one or two sentence message.
	 */
	public String	getFirstLevelText();
	
	/**
	 * Answer the second level text before parameter substitution has occurred. The
	 * second level text usually contains more details than the first level text.
	 * 
	 * <p>Not all, and perhaps none of the messages will have second level text.
	 * Typically the user is first shown the first level text. If they request more help
	 * the second level text (if any) is shown.  
	 */
	public String	getSecondLevelText();
	
	/**
	 * Answer a Object Manager reference Id for this message. UIComponents may 
	 * optionaly add their Id to the message so that the user may be able to determine which
	 * component triggered the message.
	 * 
	 * @return null if no componet has associated itself with this message.
	 */
	public String 	getReferenceId();
	
	/**
	 * Answer the second level text, after parameter substitution has ocurred.
	 * The toString() method is used to get the first level message. 
	 */
	public String	getSecondLevelMessage();
	
	/**
	 * Allows an arbitrary attribute to be associated with this message.
	 */ 
	public abstract void putAttribute(Object key, Object value);
	
	/**
	 * Answers the attribute that has the key.
	 */
	public abstract Object getAttribute(Object key);
	
}

