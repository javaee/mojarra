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
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;

/**
 * <p class="changed_added_2_0"><strong>ClientBehavior</strong> is the
 * base contract for {@link Behavior}s that attach script content to 
 * client-side events exposed by {@link ClientBehaviorHolder} components.
 * Instances of <code>ClientBehavior</code> may be attached to components 
 * that implement the {@link ClientBehaviorHolder} contract by 
 * calling {@link ClientBehaviorHolder#addClientBehavior}.  
 * Once a <code>ClientBehavior</code> has been attached to a 
 * {@link ClientBehaviorHolder} component, the component
 * calls {@link #getScript} to obtain the behavior's script and the 
 * component wires this up to the appropriate client-side event handler.
 * </p>
 *
 * @since 2.0
 */
public interface ClientBehavior extends Behavior {
	

    /**
     * <p class="changed_added_2_0">Return the script that implements this
     * ClientBehavior's client-side logic.</p>
     *
     * <p>ClientBehavior.getScript() implementations are allowed to return
     * null to indicate that no script is required for this particular
     * getScript() call.  For example, a ClientBehavior implementation may
     * return null if the Behavior is disabled.
     * </p>
     *
     * @param behaviorContext the {@link ClientBehaviorContext} that provides
     * properties that might influence this getScript() call.  Note that
     * ClientBehaviorContext instances are short-lived objects that are only
     * valid for the duration of the call to getScript().  ClientBehavior
     * implementations must not hold onto references to ClientBehaviorContexts.
     *
     * @return script that provides the client-side behavior, or null
     * if no script is required.
     * @throws NullPointerException if <code>behaviorContext</code> is 
     * <code>null</code>
     *
     * @since 2.0
     */      
    public String getScript(ClientBehaviorContext behaviorContext);

    /**
     * <p class="changed_added_2_0">Decode any new state of this 
     * {@link ClientBehavior} from the
     * request contained in the specified {@link FacesContext}.</p>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners who have registered an interest),  by calling
     * <code>queueEvent()</code>. Default implementation delegates decoding 
     * to {@link javax.faces.render.ClientBehaviorRenderer#decode(FacesContext, UIComponent, ClientBehavior)}</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param component {@link UIComponent} the component associated with this {@link Behavior} 
     *
     * @throws NullPointerException if <code>context</code> or 
     * <code>component<code> is <code>null</code>.
     *
     * @since 2.0
     */
    public void decode(FacesContext context, UIComponent component);
    
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
    public void broadcast(BehaviorEvent event);

    /**
     * <p class="changed_added_2_0">Returns hints that describe the 
     * behavior of the ClientBehavior implementation.  The hints may impact 
     * how Renderers behave in the presence of Behaviors.  For example, 
     * when a Behavior that specifies <code>ClientBehaviorHint.SUBMITTING</code> 
     * is present, the Renderer may choose to alternate the scripts that 
     * it generates itself.</p>
     *   
     * @return a non-null, unmodifiable collection of ClientBehaviorHints.
     *
     * @since 2.0
     */
    public Set<ClientBehaviorHint> getHints();
}
