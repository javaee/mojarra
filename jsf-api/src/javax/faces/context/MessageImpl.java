/*
 * $Id: MessageImpl.java,v 1.6 2003/02/03 22:57:48 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
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
