/*
 * $Id: Message.java,v 1.4 2003/10/30 16:13:48 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


/**
 * <p><strong>Message</strong> represents a single validation (or other)
 * message, which is typically associated with a particular component in
 * the view.</p>
 *
 * <ul>
 *
 * <p>How to create a message</p>
 *
 * <p>Call {@link Application.getMessageBundle}.  If
 * non-<code>null</code>, locate the named <code>ResourceBundle</code>,
 * using the <code>Locale</code> from the current {@link
 * javax.faces.component.UIViewRoot} and see if it has a value for the
 * argument <code>messageId</code>.  If it does, treat the value as the
 * <code>summary</code> of the <code>Message</code>.  If it does not, or
 * if {@link Application.getMessageBundle} returned null, look in the
 * <code>ResourceBundle</code> named by the value of the constant {@link
 * FACES_MESSAGES} and see if it has a value for the argument
 * <code>messageId</code>.  If it does, treat the value as the
 * <code>summary</code> of the <code>Message</code>.  If it does not,
 * return <code>null</code>.</p>
 *
 * <p>In all cases, if a <code>ResourceBundle</code> hit is found for
 * the argument <code>messageId</code>, look for further hits under the
 * keys <code>messageId_detail</code> and <code>messageId_detail</code>,
 * where <code>messageId</code> is the argument to
 * <code>getMessage()</code>.  Use these values, if present, as the
 * <code>detail</code> and <code>severity</code> for the returned
 * <code>Message</code>.</p>
 *
 * <p>Make sure to perform any parameter substitution required.</p>
 *
 * </ul>
 *

 */

public interface Message {


    // ------------------------------------------------ Message Severity Levels

    /**
     * <p><code>ResourceBundle</code> identifier for messages whose
     * message identifiers are defined in the JavaServer Faces
     * specification.</p>
     */
    public static final String FACES_MESSAGES = "javax.faces.Messages";

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
     * <p>Return the localized detail text for this {@link Message}
     * (if any).  This should be additional text that can help the user
     * understand the context of the problem, and offer suggestions for
     * approaches to correcting it.</p>
     */
    public String getDetail();


    /**
     * <p>Return the severity level of this {@link Message}.</p>
     */
    public int getSeverity();


    /**
     * <p>Return the localized summary text for this {@link Message}.
     * This should be the text that would normally be displayed to a user
     * to identify the problem that needs to be corrected.</p>
     */
    public String getSummary();


}
