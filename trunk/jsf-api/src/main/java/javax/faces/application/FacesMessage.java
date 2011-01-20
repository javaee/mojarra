/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.application;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * <p><strong>FacesMessage</strong> represents a single validation (or
 * other) message, which is typically associated with a particular
 * component in the view.  A {@link javax.faces.application.FacesMessage} instance may be created
 * based on a specific <code>messageId</code>.  The specification defines
 * the set of <code>messageId</code>s for which there must be
 * {@link javax.faces.application.FacesMessage} instances.</p>
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
     * <p>Immutable <code>List</code> of valid {@link javax.faces.application.FacesMessage.Severity}
     * instances, in ascending order of their ordinal value.</p>
     */
    public static final List VALUES = 
	Collections.unmodifiableList(Arrays.asList(values));

    private static Map<String,Severity> _MODIFIABLE_MAP =
         new HashMap<String,Severity>(4, 1.0f);
    
    static {
	for (int i = 0, len = values.length; i < len; i++) {
	    _MODIFIABLE_MAP.put(values[i].severityName, values[i]);
	}
    }
    

    /**
     * <p>Immutable <code>Map</code> of valid {@link javax.faces.application.FacesMessage.Severity}
     * instances, keyed by name.</p>
     */
    public final static Map VALUES_MAP = 
	Collections.unmodifiableMap(_MODIFIABLE_MAP);
    
    private static final long serialVersionUID = -1180773928220076822L;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link javax.faces.application.FacesMessage} with no initial
     * values. The severity is set to Severity.INFO.</p>
     */
    public FacesMessage() {

        super();

    }

    /**
     * <p>Construct a new {@link javax.faces.application.FacesMessage} with just a summary.  The
     * detail is <code>null</code>, the severity is set to
     * <code>Severity.INFO</code>.</p>
     */
    public FacesMessage(String summary) {

        super();
        setSummary(summary);
    }




    /**
     * <p>Construct a new {@link javax.faces.application.FacesMessage} with the specified initial
     * values.  The severity is set to Severity.INFO.</p>
     *
     * @param summary Localized summary message text
     * @param detail Localized detail message text
     *
     * @throws IllegalArgumentException if the specified severity level
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
     * @throws IllegalArgumentException if the specified severity level
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
    private boolean rendered;


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
     * @throws IllegalArgumentException if the specified severity level
     *  is not one of the supported values
     */
    public void setSeverity(Severity severity) {
	
        if ((severity.getOrdinal() < SEVERITY_INFO.getOrdinal()) || 
	    (severity.getOrdinal() > SEVERITY_FATAL.getOrdinal())) {
            throw new IllegalArgumentException(String.valueOf(severity));
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
     * @return <code>true</code> if {@link #rendered()} has been called, otherwise
     *  <code>false</code>
     *
     * @since 2.0
     */
    public boolean isRendered() {

        return rendered;

    }

    /**
     * <p>Marks this message as having been rendered to the client.</p>
     *
     * @since 2.0
     */
    public void rendered() {

        this.rendered = true;

    }
    

    /**
     * <p>Persist {@link javax.faces.application.FacesMessage} artifacts,
     * including the non serializable <code>Severity</code>.</p>
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(getSeverity().getOrdinal());
        out.writeObject(getSummary());
        out.writeObject(getDetail());
        out.writeObject(isRendered());
    }

    /**
     * <p>Reconstruct {@link javax.faces.application.FacesMessage} from
     * serialized artifacts.</p>
     */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        severity = SEVERITY_INFO;
        summary = null;
        detail = null;
        int ordinal = in.readInt();
        if (ordinal == SEVERITY_INFO.getOrdinal()) {
            setSeverity(FacesMessage.SEVERITY_INFO);
        } else if (ordinal == SEVERITY_WARN.getOrdinal()) {
            setSeverity(FacesMessage.SEVERITY_WARN);
        } else if (ordinal == SEVERITY_ERROR.getOrdinal()) {
            setSeverity(FacesMessage.SEVERITY_ERROR);
        } else if (ordinal == SEVERITY_FATAL.getOrdinal()) {
            setSeverity(FacesMessage.SEVERITY_FATAL);
        }
        setSummary((String)in.readObject());
        setDetail((String)in.readObject());
        this.rendered = (Boolean) in.readObject();
    }

    /**
     * <p>Class used to represent message severity levels in a typesafe
     * enumeration.</p>
     */
    public static class Severity implements Comparable {


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
	 * <p>Compare this {@link javax.faces.application.FacesMessage.Severity} instance to the
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
		return (String.valueOf(this.ordinal));
	    }
	    return (String.valueOf(this.severityName) + ' ' + this.ordinal);
	}
	
	
	// ---------------------------------------------------  Static Variables
	
	
	/**
	 * <p>Static counter returning the ordinal value to be assigned to the
	 * next instance that is created.</p>
	 */
	private static int nextOrdinal = 0;
	
    }


}
