/*
 * $Id: Message.java,v 1.1 2002/05/16 18:28:48 craigmcc Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


/**
 * <p><strong>Message</strong> represents a single validation (or other)
 * message, which is optionally associated with a particular component in
 * the request tree.  <code>Message</code> instances are accumulated into
 * a {@link MessageList}, which is made available via the
 * <code>getMessageList()</code> method of {@link FacesContext}.</p>
 *
 * <p><code>Message</code> instances have the following JavaBean properties:
 * </p>
 * <ul>
 * <li><em>detail</em> (String) - Localized version of the detailed text
 *     to be displayed for this <code>Message</code> (if any).</li>
 * <li><em>messageId</em> (String) - Identifier used to look up the
 *     detailed information related to this error.</li>
 * <li><em>reference</em> (String) - Compound identifier of the
 *     <code>UIComponent</code>, in the request tree, with which this
 *     <code>Message</code> is associated (if any).</li>
 * <li><em>severity</em> (int) - Indicator of the seriousness of the
 *     situation represented by this <code>Message</code>.  Must be one of
 *     the manifest constants described below.</li>
 * <li><em>summary</em> (String) - Localized version of the summary text
 *     to be displayed for this <code>Message</code>.</li>
 * </ul>
 */

public abstract class Message {


    // ------------------------------------------------ Message Severity Levels


    /**
     * <p>Message severity level indicating that a serious error has
     * occurred.</p>
     */
    public static final int SEVERITY_FATAL = 4;


    /**
     * <p>Message severity level indicating that an error has occurred.</p>
     */
    public static final int SEVERITY_ERROR = 3;


    /**
     * <p>Message severity level indicating that an error might have
     * occurred.</p>
     */
    public static final int SEVERITY_WARN = 2;


    /**
     * <p>Message severity level indicating an informational message
     * rather than an error.</p>
     */
    public static final int SEVERITY_INFO = 1;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the localized detail text for this <code>Message</code>
     * (if any).  This should be additional text that can help the user
     * understand the context of the problem, and offer suggestions for
     * approaches to correcting it.</p>
     */
    public abstract String getDetail();


    /**
     * <p>Return the message identifier of this <code>Message</code>.</p>
     */
    public abstract String getMessageId();


    /**
     * <p>Return the component reference of this <code>Message</code>
     * (if any).</p>
     */
    public abstract String getReference();


    /**
     * <p>Return the severity level of this <code>Message</code>.</p>
     */
    public abstract int getSeverity();


    /**
     * <p>Return the localized summary text for this <code>Message</code>.
     * This should be the text that would normally be displayed to a user
     * to identify the problem that needs to be corrected.</p>
     */
    public abstract String getSummary();


}
