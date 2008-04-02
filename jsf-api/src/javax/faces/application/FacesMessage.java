/*
 * $Id: FacesMessage.java,v 1.2 2003/10/30 21:47:12 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * <p><strong>FacesMessage</strong> represents a single validation (or
 * other) message, which is typically associated with a particular
 * component in the view.  A <code>FacesMessage</code> instance is
 * associated with a <code>messageId</code>.  The specification defines
 * the set of <code>messageId</code>s for which there must be
 * <code>FacesMessage</code> instances.</p>
 *
 * <p>How to create and initialize a <code>FacesMessage</code>
 * instance</p>
 *
 * <p>Creating it is easy, just use one of the public constructors.</p>
 *
 * <p>The implementation must take the following steps when creating
 * <code>FacesMessage</code> instances given a
 * <code>messageId</code>.</p>
 *
 * <ul>
 *
 * <p>Call {@link Application.getMessageBundle}.  If
 * non-<code>null</code>, locate the named <code>ResourceBundle</code>,
 * using the <code>Locale</code> from the current {@link
 * javax.faces.component.UIViewRoot} and see if it has a value for the
 * argument <code>messageId</code>.  If it does, treat the value as the
 * <code>summary</code> of the <code>FacesMessage</code>.  If it does not, or
 * if {@link Application.getMessageBundle} returned <code>null</code>,
 * look in the <code>ResourceBundle</code> named by the value of the
 * constant {@link #FACES_MESSAGES} and see if it has a value for the
 * argument <code>messageId</code>.  If it does, treat the value as the
 * <code>summary</code> of the <code>FacesMessage</code>.  If it does not,
 * there is no initialization information for the
 * <code>FacesMessage</code> instance.</p>
 *
 * <p>In all cases, if a <code>ResourceBundle</code> hit is found for
 * the <code>messageId</code>, look for further hits under the keys
 * <code>messageId_detail</code> and <code>messageId_summary</code>.
 * Use these values, if present, as the <code>detail</code> and
 * <code>severity</code> for the returned <code>FacesMessage</code>.</p>
 *
 * <p>Make sure to perform any parameter substitution required for the
 * <code>summary</code> and <code>detail</code> of the
 * <code>FacesMessage</code>.</p>
 *
 * </ul>
 *
 */

public class FacesMessage implements Serializable {

    // ------------------------------------------------------ Constants

    /**
     * <p><code>ResourceBundle</code> identifier for messages whose
     * message identifiers are defined in the JavaServer Faces
     * specification.</p>
     */
    public static final String FACES_MESSAGES = "javax.faces.Messages";


    // ------------------------------------------------ Message Severity Levels

    // Any new Severity values must go at the end of the list, or we will break
    // backwards compatibility on serialized instances

    private static final String SEVERITY_INFO_NAME = "INFO";
    /**
     * <p>Message severity level indicating an informational message
     * rather than an error.</p>
     */
    public static final Severity SEVERITY_INFO = 
	new Severity(SEVERITY_INFO_NAME);

    private static final String SEVERITY_WARN_NAME = "WARN";
    /**
     * <p>Message severity level indicating that an error might have
     * occurred.</p>
     */
    public static final Severity SEVERITY_WARN = 
	new Severity(SEVERITY_WARN_NAME);

    private static final String SEVERITY_ERROR_NAME = "ERROR";
    /**
     * <p>Message severity level indicating that an error has
     * occurred.</p>
     */
    public static final Severity SEVERITY_ERROR = 
	new Severity(SEVERITY_ERROR_NAME);

    private static final String SEVERITY_FATAL_NAME = "FATAL";
    /**
     * <p>Message severity level indicating that a serious error has
     * occurred.</p>
     */
    public static final Severity SEVERITY_FATAL = 
	new Severity(SEVERITY_FATAL_NAME);


    /**
     * <p>Array of all defined values, ascending order of ordinal value.
     *  Be sure you include any new instances created above, in the
     * same order.</p>
     */
    private static final Severity[] values =
    { SEVERITY_INFO, SEVERITY_WARN, SEVERITY_ERROR, SEVERITY_FATAL };
    
    /**
     * <p>List of valid {@link Severity} instances, in ascending order
     * of their ordinal value.</p>
     */
    public static final List VALUES = 
	Collections.unmodifiableList(Arrays.asList(values));
    
    public static Map VALUES_MAP = null;
    
    static {
	int 
	    i = 0,
	    len = 0;

	VALUES_MAP = new HashMap() {
		public void clear() {
		    throw new UnsupportedOperationException();
		}

		public java.util.Set entrySet() {
		    throw new UnsupportedOperationException();
		}

		public java.util.Set keySet() {
		    throw new UnsupportedOperationException();
		}

		public Object put(Object key, Object value) {
		    throw new UnsupportedOperationException();
		}

		public void putAll(Map otherMap) {
		    throw new UnsupportedOperationException();
		}

		public Object remove(Object key) {
		    throw new UnsupportedOperationException();
		}
		    
	    };
	
	for (i = 0; i < len; i++) {
	    VALUES_MAP.put(values[i].severityName, values[i]);
	}
    }


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new <code>FacesMessage</code> with no initial
     * values. The severity is set to Severity.ERROR.
     */
    public FacesMessage() {

        super();

    }


    /**
     * <p>Construct a new <code>FacesMessage</code> with the specified initial
     * values.  The severity is set to Severity.ERROR.  </p>
     *
     * @param summary Localized summary message text
     * @param detail Localized detail message text
     *
     * @exception IllegalArgumentException if the specified severity level
     *  is not one of the supported values
     */
    public FacesMessage(String summary, String detail) {

        super();
        setSummary(summary);
        setDetail(detail);

    }

    /**
     * <p>Construct a new <code>FacesMessage</code> with the specified
     * initial values.  </p>
     *
     * @param severity the severity
     * @param summary Localized summary message text
     * @param detail Localized detail message text
     *
     * @exception IllegalArgumentException if the specified severity level
     *  is not one of the supported values
     */
    public FacesMessage(Severity severity, String summary, 
			String detail) {

        super();
	setSeverity(severity);
        setSummary(summary);
        setDetail(detail);

    }


    // ----------------------------------------------------- Instance Variables


    private Severity severity = FacesMessage.SEVERITY_INFO;
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

    public Severity getSeverity() {

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
    public void setSeverity(Severity severity) {
	
        if ((severity.getOrdinal() < SEVERITY_INFO.getOrdinal()) || 
	    (severity.getOrdinal() > SEVERITY_FATAL.getOrdinal())) {
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

    public static class Severity extends Object implements Comparable {

	// ------------------------------------------------------  Constructors

	
	/**
	 * <p>Private constructor to disable the creation of new
	 * instances.</p>
	 */
	private Severity(String newSeverityName) {
	    severityName = newSeverityName;
	}
	
	
	// ------------------------------------------------ Instance Variables
	
	
	/**
	 * <p>The ordinal value assigned to this instance.</p>
	 */
	private final int ordinal = nextOrdinal++;
	
	/**
	 * <p>The (optional) name for this severity.</p>
	 */
	
	String severityName = null;
	
	
	// ---------------------------------------------------  Public Methods

	/**
	 * <p>Compare this {@link Severity} instance to the specified
	 * one.  Returns a negative integer, zero, or a positive integer
	 * if this object is less than, equal to, or greater than the
	 * specified object.</p>
	 *
	 * @param other The other object to be compared to
	 */
	public int compareTo(Object other) {
	    return this.ordinal - ((Severity) other).ordinal;
	}
	
	
	/**
	 * <p>Return the ordinal value of this {@link Severity}
	 * instance.</p>
	 */
	public int getOrdinal() {
	    return (this.ordinal);
	}
	
	/**
	 * <p>Return a String representation of this {@link Severity}
	 * instance.</p>
	 */
	public String toString() {
	    if (null == severityName) {
		return ("" + this.ordinal);
	    }
	    return ("" + this.severityName + " " + this.ordinal);
	}
	
	
	// --------------------------------------------------  Static Variables
	
	
	/**
	 * <p>Static counter returning the ordinal value to be assigned to the
	 * next instance that is created.</p>
	 */
	private static int nextOrdinal = 0;
	
    }


}
