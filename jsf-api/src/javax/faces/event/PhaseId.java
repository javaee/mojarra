/*
 * $Id: PhaseId.java,v 1.6 2003/02/20 22:46:29 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * <p>Typesafe enumeration of the legal values that may be returned by the
 * <code>getPhaseId()</code> method of the {@link FacesListener} interface.
 */

public class PhaseId implements Comparable {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Private constructor to disable the creation of new instances.</p>
     */
    private PhaseId() {
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The ordinal value assigned to this instance.</p>
     */
    private final int ordinal = nextOrdinal++;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Compare this {@link PhaseId} instance to the specified one.
     * Returns a negative integer, zero, or a positive integer if this
     * object is less than, equal to, or greater than the specified object.</p>
     *
     * @param other The other object to be compared to
     */
    public int compareTo(Object other) {

        return this.ordinal - ((PhaseId) other).ordinal;

    }


    /**
     * <p>Return the ordinal value of this {@link PhaseId} instance.</p>
     */
    public int getOrdinal() {

        return (this.ordinal);

    }


    /**
     * <p>Return a String representation of this {@link PhaseId} instance.</p>
     */
    public String toString() {

        return ("" + this.ordinal);

    }


    // ------------------------------------------------------- Static Variables


    /**
     * <p>Static counter returning the ordinal value to be assigned to the
     * next instance that is created.</p>
     */
    private static int nextOrdinal = 0;


    // ------------------------------------------------------ Create Instances


    // Any new Phase values must go at the end of the list, or we will break
    // backwards compatibility on serialized instances


    /**
     * <p>Identifier that indicates an interest in events, on matter
     * which request processing phase is being performed.</p>
     */
    public static final PhaseId ANY_PHASE = new PhaseId();


    /**
     * <p>Identifier that indicates an interest in events queued during
     * the <em>Reconstitute Request</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId RECONSTITUTE_REQUEST = new PhaseId();


    /**
     * <p>Identifier that indicates an interest in events queued during
     * the <em>Apply Request Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId APPLY_REQUEST_VALUES = new PhaseId();


    /**
     * <p>Identifier that indicates an interest in events queued during
     * the <em>Process Validations</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId PROCESS_VALIDATIONS = new PhaseId();


    /**
     * <p>Identifier that indicates an interest in events queued during
     * the <em>Update Model Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId UPDATE_MODEL_VALUES = new PhaseId();


    /**
     * <p>Array of all defined values, ascending order of ordinal value.
     *  Be sure you include any new instances created above, in the
     * same order.</p>
     */
    private static final PhaseId[] values =
    { ANY_PHASE, RECONSTITUTE_REQUEST, APPLY_REQUEST_VALUES,
      PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES };


    /**
     * <p>List of valid {@link PhaseId} instances, in ascending order
     * of their ordinal value.</p>
     */
    public static final List VALUES =
        Collections.unmodifiableList(Arrays.asList(values));


}
