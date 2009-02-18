/*
 * $Id: 
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

package javax.faces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;

/**
 * <p><strong class="changed_added_2_0">AjaxBehaviorEvent</strong>
 * represents the component behavior  specific to 
 * <code>Ajax</code>).</p>
 *
 * @since 2.0
 */
public class AjaxBehaviorEvent extends BehaviorEvent {


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Construct a new event object 
     * from the specified source component and Ajax behavior.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param ajaxBehavior {@link AjaxBehavior} for this event
     *
     * @throws IllegalArgumentException if <code>component</code> or
     * <code>ajaxBehavior</code> is <code>null</code>
     *
     * @since 2.0
     */
    public AjaxBehaviorEvent(UIComponent component, AjaxBehavior ajaxBehavior) {

        super(component, ajaxBehavior);

    }


    // ------------------------------------------------- Event Broadcast Methods


    /**
     * <p class="changed_added_2_0">Return <code>true</code> if this 
     * {@link FacesListener} is an instance of a the appropriate 
     * listener class that this event supports.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     *
     * @since 2.0
     */
    public  boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof AjaxBehaviorListener);

    }

    /**
     * <p class="changed_added_2_0">Broadcast this event instance 
     * to the specified {@link FacesListener}, by whatever mechanism 
     * is appropriate.  Typically, this will be accomplished by calling 
     * an event processing method, and passing this instance as a 
     * parameter.</p>
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     *
     * @since 2.0
     */ 
    public void processListener(FacesListener listener) {

        ((AjaxBehaviorListener) listener).processAjaxBehavior(this);

    }

}
