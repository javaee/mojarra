/*
 * $Id: MessageImpl.java,v 1.4 2002/07/26 22:04:52 craigmcc Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.io.Serializable;


/**
 * <p><strong>MessageImpl</strong> is a concrete class that implements
 * {@link Message}, and is a convenient base class for other
 * {@link Message} implementations.</p>
 */

public class MessageImpl implements Message, Serializable {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new {@link Message} with no initial values.
     */
    public MessageImpl() {

        super();

    }


    /**
     * <p>Construct a new {@link Message} with the specified initial values.
     * </p>
     *
     * @param severity Severity level of this {@link Message}
     * @param summary Localized summary message text
     * @param detail Localized detail message text
     *
     * @exception IllegalArgumentException if the specified severity level
     *  is not one of the supported values
     */
    public MessageImpl(int severity, String summary, String detail) {

        super();
        setSeverity(severity);
        setSummary(summary);
        setDetail(detail);

    }


    // ----------------------------------------------------- Instance Variables


    private int severity = Message.SEVERITY_INFO;
    private String summary = null;
    private String detail = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the localized detail text for this <code>Message</code>
     * (if any).  This should be additional text that can help the user
     * understand the context of the problem, and offer suggestions for
     * approaches to correcting it.</p>
     */
    public String getDetail() {

        return (this.detail);

    }


    /**
     * <p>Set the localized detail text.</p>
     *
     * @param detail The new localized detail text
     */
    public void setDetail(String detail) {

        this.detail = detail;

    }


    /**
     * <p>Return the severity level of this <code>Message</code>.</p>
     */
    public int getSeverity() {

        return (this.severity);

    }


    /**
     * <p>Set the severity level.</p>
     *
     * @param severity The new severity level
     *
     * @exception IllegalArgumentException if the specified severity level
     *  is not one of the supported values
     */
    public void setSeverity(int severity) {

        if ((severity < SEVERITY_INFO) || (severity > SEVERITY_FATAL)) {
            throw new IllegalArgumentException("" + severity);
        }
        this.severity = severity;

    }


    /**
     * <p>Return the localized summary text for this <code>Message</code>.
     * This should be the text that would normally be displayed to a user
     * to identify the problem that needs to be corrected.</p>
     */
    public String getSummary() {

        return (this.summary);

    }


    /**
     * <p>Set the localized summary text.</p>
     *
     * @param summary The new localized summary text
     */
    public void setSummary(String summary) {

        this.summary = summary;

    }


}
