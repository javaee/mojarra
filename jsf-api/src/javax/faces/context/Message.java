/*
 * $Id: Message.java,v 1.3 2002/07/26 22:04:52 craigmcc Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


/**
 * <p><strong>Message</strong> represents a single validation (or other)
 * message, which is typically associated with a particular component in
 * the request component tree.</p>
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
     * <p>Return the localized detail text for this <code>Message</code>
     * (if any).  This should be additional text that can help the user
     * understand the context of the problem, and offer suggestions for
     * approaches to correcting it.</p>
     */
    public String getDetail();


    /**
     * <p>Return the severity level of this <code>Message</code>.</p>
     */
    public int getSeverity();


    /**
     * <p>Return the localized summary text for this <code>Message</code>.
     * This should be the text that would normally be displayed to a user
     * to identify the problem that needs to be corrected.</p>
     */
    public String getSummary();


}
