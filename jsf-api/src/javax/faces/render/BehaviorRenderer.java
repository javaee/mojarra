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
import javax.faces.context.FacesContext;


/**
 * <p>A <strong class="changed_added_2_0">BehaviorRenderer</strong> produces
 * the client-side script that implements a {@link Behavior}'s client-side 
 * logic.  It also enqueues server-side {@link Behavior} events that may be
 * processed later by event listeners that have registered an interested.</p> 
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
     * Behavior's client-side logic.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param component the component instance that generates event.
     * @param behavior the behavior instance that generates script.
     * @param eventName name of the client-side event.  If this argument is
     * <code>null</code> it is assumed the caller will include the
     * client-side event name with the return value from this method.
     *
     * @return script that provides the client-side behavior
     *
     * PENDING: flesh out functionality
     */
    public String getScript(FacesContext context,
                                     UIComponent component,
                                     Behavior behavior,
                                     String eventName) {

        return null;
    }


    /**
     * <p>Decode any new state of this {@link Behavior} from the
     * request contained in the specified {@link FacesContext}.</p>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners who have registered an interest),  by calling
     * <code>queueEvent()</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param context {@link UIComponent} the component associated with this {@link Behavior}
     * @param context {@link eventName} the event name associated with this {@link Behavior}
     *
     * PENDING: Flesh out functionality
     */
    public void decode(FacesContext context,
                       UIComponent component,
                       String eventName) {
    }

}
