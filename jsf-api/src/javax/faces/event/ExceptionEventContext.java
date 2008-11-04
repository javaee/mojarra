/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0">This helper class provides context to
 * the {@link ExceptionEvent} regarding the state of the system at the
 * point in time when the <code>ExceptionEvent</code> occurs and links
 * the <code>ExceptionEvent</code> to the {@link
 * javax.faces.context.ExceptionHandler} by virtue of implementing
 * {@link SystemEventListener}.</p>
 *
 * @since 2.0
 */

public class ExceptionEventContext implements SystemEventListenerHolder {

    /**
     * <p class="changed_added_2_0">The presence of an entry under this
     * key in the <code>Map</code> returned from {@link #getAttributes}
     * indicates the event occurred during the &#8220;before
     * phase&#8221; part of the current lifecycle phase.</p>
     *
     * @since 2.0
     */
    
    public static final String IN_BEFORE_PHASE_KEY = "javax.faces.event.ExceptionEventContext.IN_BEFORE_PHASE";
    
    /**
     * <p class="changed_added_2_0">The presence of an entry under this
     * key in the <code>Map</code> returned from {@link #getAttributes}
     * indicates the event occurred during the &#8220;after
     * phase&#8221; part of the current lifecycle phase.</p>
     *
     * @since 2.0
     */
    
    public static final String IN_AFTER_PHASE_KEY = "javax.faces.event.ExceptionEventContext.IN_AFTER_PHASE";
    
    Throwable thrown;

    /**
     * <p class="changed_added_2_0">Instantiate a new
     * <code>ExceptionEventContext</code> that indicates the argument
     * <code>Throwable</code> just occurred.</p>
     *
     * @param thrown the <code>Throwable</code> that is the context for
     * this <code>ExceptionEventContext</code> instance.
     *
     * @since 2.0
     */
    
    public ExceptionEventContext(Throwable thrown) {
        setException(thrown);
    } 
            
    /**
     * <p class="changed_added_2_0">Instantiate a new
     * <code>ExceptionEventContext</code> that indicates the argument
     * <code>Throwable</code> just occurred, relevant to the argument
     * <code>component</code>, during the lifecycle phase
     * <code>phaseId</code>.</p>
     *
     * @param thrown the <code>Throwable</code> that is the context for
     * this <code>ExceptionEventContext</code> instance.
     *
     * @param component the <code>UIComponent</code> that is relevant to
     * the context.
     *
     * @param phaseId the <code>PhaseId</code> at the time this
     * <code>ExeceptionEventContext</code> is created.

     * @since 2.0
     */
    
    public ExceptionEventContext(Throwable thrown, UIComponent component, 
				 PhaseId phaseId) {
        setException(thrown);
        setComponent(component);
        setPhaseId(phaseId);
    }
    
    /**
     * <p class="changed_added_2_0">Return the <code>exception</code>
     * property.</p>
     *
     * @since 2.0
     */

    public Throwable getException() {
        return thrown;
    }
    
    /**
     * <p class="changed_added_2_0">Set the <code>exception</code>
     * property.</p>
     *
     * @since 2.0
     */

    public void setException(Throwable exception) {
        this.thrown = exception;
    }


    private UIComponent component;
    
    /**
     * <p class="changed_added_2_0">Return the <code>UIComponent</code>
     * which was being processed when the exception was thrown. If none
     * or not available, this will be <code>null</code>.</p>
     *
     * @since 2.0
     */

    public UIComponent getComponent() {
        return this.component;
    }
    
    /**
     * <p class="changed_added_2_0">Set the <code>UIComponent</code>
     * which was being processed when the exception was thrown.</p>
     *
     * @since 2.0
     */

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    private PhaseId phaseId;

    /**
     * <p class="changed_added_2_0">Return the <code>PhaseId</code>
     * which was being processed when the exception was thrown. If none
     * or not available, this will be <code>null</code>.</p>
     *
     * @since 2.0
     */

    public PhaseId getPhaseId() {
        return this.phaseId;
    }
    
    /**
     * <p class="changed_added_2_0">Set the <code>PhaseId</code> which
     * was being processed when the exception was thrown. If none or not
     * available, this will be <code>null</code>.</p>
     *
     * @since 2.0
     */

    public void setPhaseId(PhaseId phaseId) {
        this.phaseId = phaseId;
    }


    private Map<Object, Object> attributes;
    
    /**
     * <p class="changed_added_2_0">A <code>Map</code> of attributes
     * relevant to the context of this <code>ExceptionEvent</code>.</p>
     *
     * @since 2.0
     */
 
    public Map<Object, Object> getAttributes() {
        if (null == attributes) {
            attributes = new HashMap<Object,Object>();
        }
        return attributes;
    }
    
    private List<SystemEventListener> listener;

    /**
     * <p class="changed_added_2_0">Return a <code>List</code> that
     * contains a single entry, the {@link
     * javax.faces.context.ExceptionHandler} for the current
     * request.</p>
     *
     * @since 2.0
     */

    public List<SystemEventListener> getListenersForEventClass(Class<? extends SystemEvent> facesEventClass) {
        if (null == listener) {
            listener = new ArrayList<SystemEventListener>(1);
            listener.add(FacesContext.getCurrentInstance().getExceptionHandler());
        }
        return listener;
    }
    
    

}
