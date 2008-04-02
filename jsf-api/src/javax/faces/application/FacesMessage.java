/*
 * $Id: FacesMessage.java,v 1.7 2004/01/19 06:26:52 craigmcc Exp $
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
 * component in the view.  A {@link FacesMessage} instance may be created
 * based on a specific <code>messageId</code>.  The specification defines
 * the set of <code>messageId</code>s for which there must be
 * {@link FacesMessage} instances.</p>
 *
 * <p>The implementation must take the following steps when creating
 * <code>FacesMessage</code> instances given a <code>messageId</code>:</p>
 *
 * <ul>
 *
 * <p>Call {@link Application#getMessageBundle}.  If
 * non-<code>null</code>, locate the named <code>ResourceBundle</code>,
 * using the <code>Locale</code> from the current {@link
 * javax.faces.component.UIViewRoot} and see if it has a value for the
 * argument <code>messageId</code>.  If it does, treat the value as the
 * <code>summary</code> of the <code>FacesMessage</code>.  If it does
 * not, or if {@link Application#getMessageBundle} returned
 * <code>null</code>, look in the <code>ResourceBundle</code> named by
 * the value of the constant {@link #FACES_MESSAGES} and see if it has a
 * value for the argument <code>messageId</code>.  If it does, treat the
 * value as the <code>summary</code> of the <code>FacesMessage</code>.
 * If it does not, there is no initialization information for the
 * <code>FacesMessage</code> instance.</p>
 *
 * <p>In all cases, if a <code>ResourceBundle</code> hit is found for
 * the <code>{messageId}</code>, look for further hits under the key
 * <code>{messageId}_detail</code>. Use this value, if present, as 
 * the <code>detail</code> for the returned <code>FacesMessage</code>.</p>
 *
 * <p>Make sure to perform any parameter substitution required for the
 * <code>summary</code> and <code>detail</code> of the
 * <code>FacesMessage</code>.</p>
 *
 * </ul>
 *
 */

public class FacesMessage implements Serializable {


    // --------------------------------------------------------------- Constants


    /**
     * <p><code>ResourceBundle</code> identifier for messages whose
     * message identifiers are defined in the JavaServer Faces
     * specification.</p>
     */
    public static final String FACES_MESSAGES = "javax.faces.Messages";


    // ------------------------------------------------- Message Severity Levels


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
     * <p>Immutable <code>List</code> of valid {@link FacesMessage.Severity}
     * instances, in ascending order of their ordinal value.</p>
     */
    public static final List VALUES = 
	Collections.unmodifiableList(Arrays.asList(values));

    private static Map _MODIFIABLE_MAP = new HashMap();
    
    static {
	for (int i = 0, len = values.length; i < len; i++) {
	    _MODIFIABLE_MAP.put(values[i].severityName, values[i]);
	}
    }
    

    /**
     * <p>Immutable <code>Map</code> of valid {@link FacesMessage.Severity}
     * instances, keyed by name.</p>
     */
    public final static Map VALUES_MAP = 
	Collections.unmodifiableMap(_MODIFIABLE_MAP);
    

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link FacesMessage} with no initial
     * values. The severity is set to Severity.INFO.</p>
     */
    public FacesMessage() {

        super();

    }

    /**
     * <p>Construct a new {@link FacesMessage} with just a summary.  The
     * detail is <code>null</code>, the severity is set to
     * <code>Severity.INFO</code>.</p>
     */
    public FacesMessage(String summary) {

        super();
	setSummary(summary);
    }




    /**
     * <p>Construct a new {@link FacesMessage} with the specified initial
     * values.  The severity is set to Severity.INFO.</p>
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
     * initial values.</p>
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


    // ------------------------------------------------------ Instance Variables


    private Severity severity = FacesMessage.SEVERITY_INFO;
    private String summary = null;
    private String detail = null;


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the localized detail text.  If no localized detail text has
     * been defined for this message, return the localized summary text
     * instead.</p>
     */
    public String getDetail() {

	if (this.detail == null) {
	    return (this.summary);
	} else {
	    return (this.detail);
	}

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
     * <p>Return the severity level.</p>
     */
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


    /**
     * <p>Return the localized summary text.</p>
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


    /**
     * <p>Class used to represent message severity levels in a typesafe
     * enumeration.</p>
     */
    public static class Severity extends Object implements Comparable {


	// -------------------------------------------------------  Constructors

	
	/**
	 * <p>Private constructor to disable the creation of new
	 * instances.</p>
	 */
	private Severity(String newSeverityName) {
	    severityName = newSeverityName;
	}
	
	
	// -------------------------------------------------- Instance Variables
	
	
	/**
	 * <p>The ordinal value assigned to this instance.</p>
	 */
	private final int ordinal = nextOrdinal++;
	

	/**
	 * <p>The (optional) name for this severity.</p>
	 */
        String severityName = null;
	
	
	// -----------------------------------------------------  Public Methods


	/**
	 * <p>Compare this {@link FacesMessage.Severity} instance to the
	 * specified one.  Returns a negative integer, zero, or a
	 * positive integer if this object is less than, equal to, or
	 * greater than the specified object.</p>
	 *
	 * @param other The other object to be compared to
	 */
	public int compareTo(Object other) {
	    return this.ordinal - ((Severity) other).ordinal;
	}
	
	
	/**
	 * <p>Return the ordinal value of this {@link
	 * FacesMessage.Severity} instance.</p>
	 */
	public int getOrdinal() {
	    return (this.ordinal);
	}
	

	/**
	 * <p>Return a String representation of this {@link
	 * FacesMessage.Severity} instance.</p>
	 */
	public String toString() {
	    if (null == severityName) {
		return ("" + this.ordinal);
	    }
	    return ("" + this.severityName + " " + this.ordinal);
	}
	
	
	// ---------------------------------------------------  Static Variables
	
	
	/**
	 * <p>Static counter returning the ordinal value to be assigned to the
	 * next instance that is created.</p>
	 */
	private static int nextOrdinal = 0;
	
    }


}
