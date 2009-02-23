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

package javax.faces.component.behavior;

import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><strong>BehaviorContext</strong>
 * provides context information that may be useful to Behavior.getScript()
 * implementations.
 * </p>
 *
 * @since 2.0
 */
public abstract class BehaviorContext {
	
    /**
     * Creates a BehaviorContext instance.
     * @param context the <code>FacesContext</code> for the current request.
     * @param component the component instance to which the 
     * <code>Behavior</code> is attached.
     * @param eventName the name of the behavior event to which the
     * <code>Behavior</code> is attached.
     * @param sourceId the id to use as the Behavior's "source".
     * @param parameters the collection of parameters for submitting
     * Behaviors to include in the request.
     @ @return a <code>BehaviorContext</code> instance configured with the
     * provided values.
     * @throws NullPointerException if <code>context</code>,
     * <code>component</code> or <code>eventName</code>
     *  is <code>null</code>
     */
    public static BehaviorContext createBehaviorContext(FacesContext context,
                                                        UIComponent component,
                                                        String eventName,
                                                        String sourceId,
                                                        Collection<Behavior.Parameter> parameters) {

        return new BehaviorContextImpl(context, component, eventName, sourceId, parameters);
    }

    /**
     * <p>Returns the FacesContext for the current request.</p>
     */
    abstract public FacesContext getFacesContext();

    /**
     * <p>Returns the component that is requesting the Behavior script.</p>
     */
    abstract public UIComponent getComponent();

    /**
     * <p>Returns the name of the behavior event for which the Behavior
     * script is being requested. </p>
     */
    abstract public String getEventName();

    /**
     * <p>Returns an id for use as the Behavior source.</p>
     * <p>Behavior implementations that submit back to the Faces lifecycle
     * are required to identify which component triggered the
     * Behavior-initiated request via the "javax.faces.behavior.source"
     * request parameter.  In most cases, th source id can be
     * trivially derived from the element to which the Behavior's
     * client-side script is attached - ie. the source id is typically
     * the id of this element.  However, in components which produce
     * more complex content, the Behavior script may not be able to
     * determine the correct id to use for the javax.faces.behavior.source
     * value.  The BehaviorContext.getSourceId() method allows the component 
     * to pass this information into the Behavior.getScript()
     * implementation.</p>
     *
     * @return the id for the Behavior's script to use as the "source", or
     * null if the Behavior's script can identify the source from the DOM.
     */
    abstract public String getSourceId();

    /**
     * <p>Returns parameters that "submitting" Behavior implementations
     * should include when posting back data into the Faces lifecycle.</p>
     * <p>If no parameters are specified, returns an empty (non-null)
     * collection.</p>
     */
    abstract public Collection<Behavior.Parameter> getParameters();

    // Little static member class that provides a default implementation
    private static final class BehaviorContextImpl extends BehaviorContext {

        private FacesContext context;
        private UIComponent component;
        private String eventName;
        private String sourceId;
        private Collection<Behavior.Parameter> parameters;

        private BehaviorContextImpl(FacesContext context,
                                    UIComponent component,
                                    String eventName,
                                    String sourceId,
                                    Collection<Behavior.Parameter> parameters) {

            if (null == context) {
                throw new NullPointerException();
            }

            if (null == component) {
                throw new NullPointerException();
            }

            if (null == eventName) {
                throw new NullPointerException();
            }

            this.context = context;
            this.component = component;
            this.eventName = eventName;
            this.sourceId = sourceId;

            this.parameters =  (parameters == null) ? 
                                   Collections.<Behavior.Parameter>emptyList() : 
                                   parameters;
        }        

        @Override
        public FacesContext getFacesContext() {
            return context;
        }

        @Override
        public UIComponent getComponent() {
            return component;
        }

        @Override
        public String getEventName() {
            return eventName;
        }

        @Override
        public String getSourceId() {
            return sourceId;
        }

        @Override
        public Collection<Behavior.Parameter> getParameters() {
            return parameters;
        }
    }
}
