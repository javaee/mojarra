/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * <p><strong class="changed_added_2_0 changed_modified_2_2">
 * ComponentSystemEvent</strong> is
 * the base class for {@link SystemEvent}s that are specific to a {@link
 * UIComponent} instance.</p>
 *
 * @since 2.0
 */
public abstract class ComponentSystemEvent extends SystemEvent {

    private static final long serialVersionUID = -4726746661822507506L;

    
    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Pass the argument
     * <code>component</code> to the superclass constructor.</p>

     * @param component the <code>UIComponent</code> reference to be
     * passed to the superclass constructor.
     *
     * @throws <code>IllegalArgumentException</code> if the argument is <code>null</code>.
     * 
     * @since 2.0
     */
    public ComponentSystemEvent(UIComponent component) {
        super(component);
    }

    /**
     * <p class="changed_added_2_2">Return <code>true</code> if the argument
     * {@link FacesListener} is an instance of the appropriate listener class that this event
     * supports.  The default implementation returns true if the listener
     * is a {@link ComponentSystemEventListener} or if <code>super.isAppropriateListener()</code>
     * returns true.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     * @since 2.2
     */
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        boolean result = (listener instanceof ComponentSystemEventListener);
        if (!result) {
            result = super.isAppropriateListener(listener);
        }
        return result;
    }

    /**
     * <p class="changed_added_2_2">Broadcast this event instance to 
     * the specified {@link FacesListener} by calling the superclass's
     * <code>processListener()</code> implementation.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     * @since 2.2
     */
    @Override
    public void processListener(FacesListener listener) {
        UIComponent c = getComponent();
        UIComponent cFromStack;
        boolean didPush = false;
        FacesContext context = FacesContext.getCurrentInstance();
        cFromStack = UIComponent.getCurrentComponent(context);
        if (null == cFromStack) {
            didPush = true;
            c.pushComponentToEL(context, null);
        }
        try {
            if (listener instanceof SystemEventListener) {
                super.processListener(listener);
            } else if (listener instanceof ComponentSystemEventListener) {
                ((ComponentSystemEventListener)listener).processEvent(this);
            }
        } finally {
            if (didPush) {
                c.popComponentFromEL(context);
            }
        }
    }
    
    

    // -------------------------------------------------------------- Properties


    /**
     * <p class="changed_added_2_0">the source {@link UIComponent} that sent this event.</p>
     * 
     * @since 2.0
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }    

}
