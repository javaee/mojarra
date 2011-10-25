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

package javax.faces.event;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.faces.FacesException;


/**
 * <p><span class="changed_modified_2_2">Typesafe</span> enumeration of
 * the legal values that may be returned by the
 * <code>getPhaseId()</code> method of the {@link FacesEvent} interface.
 */

public class PhaseId implements Comparable {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Private constructor to disable the creation of new instances.</p>
     */
    private PhaseId(String newPhaseName) {
        phaseName = newPhaseName;
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The ordinal value assigned to this instance.</p>
     */
    private final int ordinal = nextOrdinal++;

    /**
     * <p>The (optional) name for this phase.</p>
     */

    private String phaseName = null;


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
        if (null == phaseName) {
            return (String.valueOf(this.ordinal));
        }

        return (String.valueOf(this.phaseName) + ' ' + this.ordinal);
    }
    
    /**
     * <p class="changed_added_2_2">Return the name of this phase.</p>
     * 
     * @since 2.2
     */

    public String getName() {
        return this.phaseName;
    }

    /**
     * <p class="changed_added_2_2">Return a <code>PhaseId</code>
     * representation of the arcument <code>phase</code>.</p>
     * 
     * @param phase the String for which the corresponding
     * <code>PhaseId</code> should be returned.
     *
     * @throws <code>NullPointerException</code> if argument <code>phase</code> is
     * <code>null</code>.
     *
     * @throws <code>FacesException</code> if the <code>PhaseId</code>
     * corresponding to the argument <code>phase</code> cannot be found.
     *
     * @since 2.2
     */
    
    public static PhaseId phaseIdValueOf(String phase) {
        if (null == phase) {
            throw new NullPointerException();
        }
        PhaseId result = null;

        if (ANY_PHASE_NAME.equals(phase)) {
            result = PhaseId.ANY_PHASE;
        } else if (APPLY_REQUEST_VALUES_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.APPLY_REQUEST_VALUES;
        } else if (INVOKE_APPLICATION_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.INVOKE_APPLICATION;
        } else if (PROCESS_VALIDATIONS_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.PROCESS_VALIDATIONS;
        } else if (RENDER_RESPONSE_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.RENDER_RESPONSE;
        } else if (RESTORE_VIEW_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.RESTORE_VIEW;
        } else if (UPDATE_MODEL_VALUES_NAME.equalsIgnoreCase(phase)) {
            result = PhaseId.UPDATE_MODEL_VALUES;
        } else {
            throw new FacesException("Not a valid phase [" + phase + "]");
        }
        
        return result;
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


    private static final String ANY_PHASE_NAME = "ANY";
    /**
     * <p>Identifier that indicates an interest in events, no matter
     * which request processing phase is being performed.</p>
     */
    public static final PhaseId ANY_PHASE = new PhaseId(ANY_PHASE_NAME);


    private static final String RESTORE_VIEW_NAME = "RESTORE_VIEW";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Restore View</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId RESTORE_VIEW = new PhaseId(RESTORE_VIEW_NAME);


    private static final String APPLY_REQUEST_VALUES_NAME = "APPLY_REQUEST_VALUES";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Apply Request Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId APPLY_REQUEST_VALUES = new PhaseId(APPLY_REQUEST_VALUES_NAME);


    private static final String PROCESS_VALIDATIONS_NAME = "PROCESS_VALIDATIONS";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Process Validations</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId PROCESS_VALIDATIONS = new PhaseId(PROCESS_VALIDATIONS_NAME);


    private static final String UPDATE_MODEL_VALUES_NAME = "UPDATE_MODEL_VALUES";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Update Model Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId UPDATE_MODEL_VALUES = new PhaseId(UPDATE_MODEL_VALUES_NAME);


    private static final String INVOKE_APPLICATION_NAME = "INVOKE_APPLICATION";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Invoke Application</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId INVOKE_APPLICATION = new PhaseId(INVOKE_APPLICATION_NAME);

    private static final String RENDER_RESPONSE_NAME = "RENDER_RESPONSE";
    /**
     * <p>Identifier for the <em>Render Response</em> phase of the
     * request processing lifecycle.</p>
     */
    public static final PhaseId RENDER_RESPONSE = new PhaseId(RENDER_RESPONSE_NAME);


    /**
     * <p>Array of all defined values, ascending order of ordinal value.
     * Be sure you include any new instances created above, in the
     * same order.</p>
     */
    private static final PhaseId[] values =
            {ANY_PHASE, RESTORE_VIEW, APPLY_REQUEST_VALUES,
                    PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES, INVOKE_APPLICATION, RENDER_RESPONSE};


    /**
     * <p>List of valid {@link PhaseId} instances, in ascending order
     * of their ordinal value.</p>
     */
    public static final List<PhaseId> VALUES =
            Collections.unmodifiableList(Arrays.asList(values));


}
