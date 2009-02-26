/*
 * $Id: Behavior.java,v 1.0 2009/01/03 18:51:29 rogerk Exp $
 */

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.BehaviorListener;
import javax.faces.render.BehaviorRenderer;
import javax.faces.render.RenderKit;

/**
 * <p class="changed_added_2_0"><strong>Behavior</strong> is the
 * contract for generating component behavior. 
 * A behavior could be client side behavior such as Ajax and 
 * client side validation, or it could be server side behavior. 
 * Instances of <code>Behavior</code> may be attached to components 
 * that implement the {@link BehaviorHolder} contract by 
 * calling {@link BehaviorHolder#addBehavior}.  
 * Once a <code>Behavior</code> has been attached to a 
 * {@link BehaviorHolder} component, the component
 * calls {@link #getScript} to obtain the behavior's script and the 
 * component wires this up to the appropriate client-side event handler.
 * </p>
 *
 * @since 2.0
 */
public abstract class Behavior {
	

    /**
     * <p class="changed_added_2_0">Return the script that implements this
     * Behavior's client-side logic.</p>
     *
     * <p>Behavior.getScript() implementations are allowed to return
     * null to indicate that no script is required for this particular
     * getScript() call.  For example, a Behavior implementation may
     * return null if the Behavior is disabled.
     * </p>
     *
     * @param behaviorContext the {@link BehaviorContext} that provides
     * properties that might influence this getScript() call.  Note that
     * BehaviorContext instances are short-lived objects that are only
     * valid for the duration of the call to getScript().  Behavior
     * implementations must not hold onto references to BehaviorContexts.
     *
     * @return script that provides the client-side behavior, or null
     * if no script is required.
     * @throws NullPointerException if <code>behaviorContext</code> is 
     * <code>null</code>
     *
     * @since 2.0
     */      
    abstract public String getScript(BehaviorContext behaviorContext);

    /**
     * <p class="changed_added_2_0">Decode any new state of this 
     * {@link Behavior} from the
     * request contained in the specified {@link FacesContext}.</p>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners who have registered an interest),  by calling
     * <code>queueEvent()</code>. Default implementation delegates decoding 
     * to {@link BehaviorRenderer#decode(FacesContext, UIComponent, String)}</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param context {@link UIComponent} the component associated with this {@link Behavior} 
     *
     * @throws NullPointerException if <code>context</code> or 
     * <code>component<code> is <code>null</code>.
     *
     * @since 2.0
     */
    abstract public void decode(FacesContext context, UIComponent component);
    
    /**
     * <p class="changed_added_2_0">Get type of the {@link BehaviorRenderer} if instance uses
     * bridge patterns for a render-kit-specific functionality.
     * </p>
     * <p>Note that Behavior implementations are not required to delegate
     * to a BehaviorRenderer.  Implementations that wish to do so should
     * override getRendererType() to return a string that identifies the
     * type of BehaviorRenderer to use.  Implementations that do not wish
     * to delegate to a BehaviorRenderer should override getScript() 
     * to perform script rendering locally in the Behavior implementation.
     * </p>
     * @return the {@link BehaviorRenderer} type for this {@link Behavior}, or null
     * if the Behavior impelentation performs its own script rendering.
     *
     * @since 2.0
     */
    abstract public String getRendererType();
    
    /**
     * <p class="changed_added_2_0">Broadcast the specified 
     * {@link BehaviorEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type.  Listeners are called in the order in which they were
     * added.</p>
     *
     * @param event The {@link BehaviorEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @throws IllegalArgumentException if the implementation class
     *  of this {@link BehaviorEvent} is not supported by this component
     * @throws NullPointerException if <code>event</code> is
     * <code>null</code>
     *
     * @since 2.0
     */
    abstract public void broadcast(BehaviorEvent event);

    /**
     * <p class="changed_added_2_0">Returns hints that describe the 
     * behavior of the Behavior implementation.  The hints may impact 
     * how Renderers behave in the presence of Behaviors.  For example, 
     * when a Behavior that specifies <code>BehaviorHint.SUBMITTING</code> 
     * is present, the Renderer may choose to alternate the scripts that 
     * it generates itself.</p>
     *   
     * @return a non-null, unmodifiable collection of BehaviorHints.
     *
     * @since 2.0
     */
    abstract public Set<BehaviorHint> getHints();

    /**
     * <p class="changed_added_2_0"><strong>Parameter</strong> instances
     * represent name/value pairs that "submitting" Behavior implementations
     * should include when posting back into the Faces lifecycle.  Behavior
     * implementations can determine which Parameters to include by calling
     * BehaviorContext.getParameters().
     * </p>
     *
     * @since 2.0
     */
    public static class Parameter {

        private String name;
        private Object value;

        /**
         * <p class="changed_added_2_0">Creates a Parameter instance.</p>
         * @param name the name of the parameter
         * @param value the value of the parameter
         * @throws NullPointerException if <code>name</code>
         * is null.
         *
         * @since 2.0
         */
        public Parameter(String name, Object value) {

            if (null == name) {
                throw new NullPointerException();
            }

            this.name = name;
            this.value = value;
        }

        /**
         * <p class="changed_added_2_0">Returns the Parameter's name.</p>
         *
         * @since 2.0
         */
        public String getName() {
            return name;
        }

        /**
         * <p class="changed_added_2_0">Returns the Parameter's value.</p>
         *
         * @since 2.0
         */
        public Object getValue() {
            return value;
        }
    }
}
