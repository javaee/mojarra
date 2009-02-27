/*
 * $Id: BehaviorRenderer.java,v 1.39.12.7 2008/04/17 18:51:29 edburns Exp $
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

package javax.faces.render;


import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.BehaviorContext;
import javax.faces.context.FacesContext;


/**
 * <p>A <strong class="changed_added_2_0">BehaviorRenderer</strong> produces
 * the client-side script that implements a {@link Behavior}'s client-side 
 * logic.  It also enqueues server-side {@link Behavior} events that may be
 * processed later by event listeners that have registered an interest.</p> 
 *
 * <p>Individual {@link BehaviorRenderer} instances will be instantiated as 
 * requested during the rendering process, and will remain in existence for the
 * remainder of the lifetime of a web application.  Because each instance
 * may be invoked from more than one request processing thread simultaneously,
 * they MUST be programmed in a thread-safe manner.</p>
 *
 * @since 2.0
 */

public abstract class BehaviorRenderer {
    
    
    // ------------------------------------------------------ Rendering Methods

    /**
     * <p class="changed_added_2_0">Return the script that implements this
     * Behavior's client-side logic.  The default implementation returns 
     * <code>null</code>.</p>
     *
     * <p>BehaviorRenderer.getScript() implementations are allowed to return
     * null to indicate that no script is required for this particular
     * getScript() call.  For example, a BehaviorRenderer implementation may
     * return null if the associated Behavior is disabled.
     * </p>
     *
     * @param behaviorContext the {@link BehaviorContext} that provides
     * properties that might influence this getScript() call.  Note that
     * BehaviorContext instances are short-lived objects that are only
     * valid for the duration of the call to getScript().  BehaviorRenderer
     * implementations must not hold onto references to BehaviorContexts.
     *
     * @param behavior the behavior instance that generates script.
     *
     * @return script that provides the client-side behavior, or null
     * if no script is required.
     *
     * @since 2.0
     *
     */
    public String getScript(BehaviorContext behaviorContext, 
                            Behavior behavior) {

        return null;
    }


    /**
     * <p class="changed_added_2_0">Decode any new state of this {@link Behavior} 
     * from the request contained in the specified {@link FacesContext}.</p>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners who have registered an interest),  by calling
     * <code>queueEvent()</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param component {@link UIComponent} the component associated with this {@link Behavior}
     * @param behavior {@link Behavior} the behavior instance
     *
     * @throws NullPointerException if <code>context</code>,
     *  <code>component</code> <code>behavior</code> is <code>null</code>
     *
     * @since 2.0
     */
    public void decode(FacesContext context,
                       UIComponent component,
                       Behavior behavior) {

        if (null == context || null == component || behavior == null) {
            throw new NullPointerException();
        }

    }

}
