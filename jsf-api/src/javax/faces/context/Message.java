/*
 * $Id: Message.java,v 1.5 2003/02/20 22:46:21 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


/**
 * <p><strong>Message</strong> represents a single validation (or other)
 * message, which is typically associated with a particular component in
 * the component tree.</p>
 */

public interface Message {


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
