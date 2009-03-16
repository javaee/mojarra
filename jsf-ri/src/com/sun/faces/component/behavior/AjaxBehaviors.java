/*
 * $Id: AjaxBehaviors.java,v 1.0 2008/11/03 18:51:29 rogerk Exp $
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
 
package com.sun.faces.component.behavior;

import java.io.Serializable;
import java.util.LinkedList;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">An instance of the class is used to 
 * manage {@link AjaxBehavior} instances.</p>
 *
 * @since 2.0
 */
public class AjaxBehaviors implements Serializable {

    public static final String AJAX_BEHAVIORS = "javax.faces.component.AjaxBehaviors";

    private LinkedList<BehaviorInfo> ajaxBehaviors = null;

    public AjaxBehaviors() {
        ajaxBehaviors = new LinkedList<BehaviorInfo>();
    }

    /**
     * <p>Push the {@link AjaxBehavior} into scope making it available 
     * for subsequent calls to {@link #getBehaviorForEvent}.</p>
     *
     * @param ajaxBehavior the {@link AjaxBehavior} instance
     * @param eventName the name of the event that the behavior is associated
     *     with.
     *
     * @since 2.0
     */ 
    public void pushBehavior(ClientBehavior ajaxBehavior, String eventName) {
        ajaxBehaviors.add(new BehaviorInfo(ajaxBehavior, eventName));
    }

    /**
     * <p>Pop the last {@link AjaxBehavior} instance 
     * from the <code>List</code>.</p>
     *
     * @since 2.0
     */
    public void popBehavior() {
         if (ajaxBehaviors.size() > 0) {
             ajaxBehaviors.removeLast();
         }
    }   

    /**
     * <p>Return the current {@link AjaxBehavior} instance from 
     * the <code>List</code> of {@link AjaxBehavior} instances. 
     * This will be the last instance added to the 
     * <code>List</code>.  Return <code>null</code>
     * if no {@link AjaxBehavior} is available.<p>
     *
     * @return the last (most current) {@link AjaxBehavior} 
     *
     * @since 2.0
     */
    public BehaviorInfo getCurrentBehavior() {
        BehaviorInfo ajaxBehavior  = null;
        if (ajaxBehaviors.size() > 0) {
            ajaxBehavior = ajaxBehaviors.getLast();
        }
        return ajaxBehavior;
    }

    // Little value holder class that holds onto an AjaxBehavior
    // and its event.
    public static class BehaviorInfo {
        private ClientBehavior behavior;
        private String eventName;

        public BehaviorInfo(ClientBehavior behavior, String eventName) {
            this.behavior = behavior;
            this.eventName = eventName;
        }

        public ClientBehavior getBehavior() {
            return behavior;
        }

        public String getEventName() {
            return eventName;
        }

        private BehaviorInfo() {
        }
    }

}
