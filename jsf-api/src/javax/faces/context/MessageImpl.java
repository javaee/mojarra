/*
 * $Id: MessageImpl.java,v 1.1 2002/06/08 02:57:55 craigmcc Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


/**
 * <p><strong>MessageImpl</strong> is a concrete class that implements
 * {@link Message}, and is a convenient base class for other
 * {@link Message} implementations.</p>
 */

public class MessageImpl extends Message {


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
     * @param messageId Message identifier (if any)
     * @param reference Component reference of this {@link Message}
     * @param severity Severity level of this {@link Message}
     * @param summary Localized summary message text
     * @param detail Localized detail message text
     */
    public MessageImpl(String messageId, String reference, int severity,
                       String summary, String detail) {

        super();
        setMessageId(messageId);
        setReference(reference);
        setSeverity(severity);
        setSummary(summary);
        setDetail(detail);

    }


    // ----------------------------------------------------- Instance Variables


    private String messageId = null;
    private String reference = null;
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
     * <p>Return the message identifier of this <code>Message</code>.</p>
     */
    public String getMessageId() {

        return (this.messageId);

    }


    /**
     * <p>Set the message identifier.</p>
     *
     * @param messageId The new message identifier
     */
    public void setMessageId(String messageId) {

        this.messageId = messageId;

    }


    /**
     * <p>Return the component reference of this <code>Message</code>
     * (if any).</p>
     */
    public String getReference() {

        return (this.reference);

    }


    /**
     * <p>Set the component reference.</p>
     *
     * @param reference The new component reference
     */
    public void setReference(String reference) {

        this.reference = reference;

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
     */
    public void setSeverity(int severity) {

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
     * <p>SEt the localized summary text.</p>
     *
     * @param summary The new localized summary text
     */
    public void setSummary(String summary) {

        this.summary = summary;

    }


}
