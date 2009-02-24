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
 * <p class="changed_added_2_0"><strong>BehaviorBase</strong> is a
 * convenience base class that implements the default concrete behavior
 * of all methods defined by {@link Behavior}.</p>
 * <p>Subclasses should either override getRendererType() to identify
 * the BehaviorRenderer to delegate to, or they should override 
 * <code>getScript()</code> to locally generate the desired Behavior 
 * script, and <code>decode()</code>.
 * </p>
 *
 * @since 2.0
 */
public class BehaviorBase extends Behavior {
	

    private static final Logger logger = Logger.getLogger("javax.faces.component.behavior",
    "javax.faces.LogStrings");

    /**
     * <p>Our {@link javax.faces.event.BehaviorListener}s.  This data
     * structure is lazily instantiated as necessary.</p>
     */
    private List<BehaviorListener> listeners;

    /**
     * <p class="changed_added_2_0">Default implementation of 
     * of {@link Behavior#getScript}.  If a {@link BehaviorRenderer} 
     * is available for the specified behavior renderer type, this method
     * delegates to the {@link BehaviorRenderer#getScript} method.  
     * Otherwise, this method returns null.
     * </p>
     *
     * @param behaviorContext the {@link BehaviorContext}
     *
     * @return the script provided by the associated BehaviorRenderer, or
     * null if no BehaviorRenderer is available.
     *
     * @throws NullPointerException if <code>behaviorContext</code> is 
     * <code>null</code>
     *
     * @since 2.0
     */
    @Override
    public String getScript(BehaviorContext behaviorContext) {

        if (null == behaviorContext) {
            throw new NullPointerException();
        }

    	BehaviorRenderer renderer = getRenderer(behaviorContext.getFacesContext());
        String script = null;
    	if (null != renderer){
            script = renderer.getScript(behaviorContext, this);
    	}
        return script;
    }

    /**
     * <p class="changed_added_2_0">Default implementation of 
     * of Behavior.decode().  If a BehaviorRenderer is available
     * for the specified behavior renderer type, this method
     * delegates to the BehaviorRenderer's decode() method.  
     * Otherwise, no decoding is performed.
     * </p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param context {@link UIComponent} the component associated with this {@link Behavior} 
     *
     * @throws NullPointerException if <code>context</code> or 
     * <code>component<code> is <code>null</code>.
     *
     * @since 2.0
     */
    @Override
    public void decode(FacesContext context,
                       UIComponent component) {
    
        if (null == context || null == component) {
            throw new NullPointerException();
        }

    	BehaviorRenderer renderer = getRenderer(context);
    	if (null != renderer){
            renderer.decode(context, component, this);
    	}
    }
    
    /**
     * <p class="changed_added_2_0">Default implementation of 
     * {@link Behavior#getRendererType}. The default implementation 
     * returns null.  Subclasses should either override this method to 
     * return a string that identifies the type of {@link BehaviorRenderer} 
     * to use, or should override {@link #getScript} and perform script 
     * rendering locally in the {@link Behavior} implementation.
     * </p>
     * @return the default renderer type, which is null.
     *
     * @since 2.0
     */
    @Override
    public String getRendererType() {
        return null;
    }
    
    /**
     * <p class="changed_added_2_0">Default implementation of 
     * Behavior.braodcast().  Delivers the specified {@link BehaviorEvent} to 
     * all registered {@link BehaviorListener} event listeners who have 
     * expressed an interest in events of this type.  Listeners are called 
     * in the order in which they were registered (added).</p>
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
    @Override
    public void broadcast(BehaviorEvent event)
        throws AbortProcessingException {

        if (null != listeners) {
            for (BehaviorListener listener : listeners) {
                if (event.isAppropriateListener(listener)) {
                    event.processListener(listener);
                }
            }
        }
    }

    /**
     * <p class="changed_added_2_0">Default implementation of Behavior.getHints().  
     * By default, no hints are specified, and this method returns an empty,
     * umodifiable set.</p>
     *   
     * @return an empty, unmodifiable set of BehaviorHints.
     *
     * @since 2.0
     */
    @Override
    public Set<BehaviorHint> getHints() {
        return Collections.emptySet();
    }

    /**
     * <p class="changed_added_2_0">Add the specified {@link BehaviorListener} 
     * to the set of listeners registered to receive event notifications 
     * from this {@link Behavior}.
     * It is expected that {@link Behavior} classes acting as event sources
     * will have corresponding typesafe APIs for registering listeners of the
     * required type, and the implementation of those registration methods
     * will delegate to this method.  For example:</p>
     * <pre>
     * public class AjaxBehaviorEvent extends BehaviorEvent { ... }
     *
     * public interface AjaxBehaviorListener extends BehaviorListener {
     *   public void processAjaxBehavior(FooEvent event);
     * }
     *
     * public class AjaxBehavior extends Behavior {
     *   ...
     *   public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     addBehaviorListener(listener);
     *   }
     *   public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     removeBehaviorListener(listener);
     *   }
     *   ...
     * }
     * </pre>
     *
     * @param listener The {@link BehaviorListener} to be registered
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     *
     * @since 2.0
     */
    protected void addBehaviorListener(BehaviorListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            //noinspection CollectionWithoutInitialCapacity
            listeners = new ArrayList<BehaviorListener>();
        }
        listeners.add(listener);

    }

    /**
     * <p class="changed_added_2_0">Remove the specified 
     * {@link BehaviorListener} from the set of listeners
     * registered to receive event notifications from this 
     * {@link Behavior}.
     *
     * @param listener The {@link BehaviorListener} to be deregistered
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     *
     * @since 2.0
     */
    protected void removeBehaviorListener(BehaviorListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    /**
     * <p class="changed_added_2_0">Convenience method to return the {@link BehaviorRenderer} 
     * instance associated with this {@link Behavior}, if any; otherwise, return
     * <code>null</code>.
     * </p>
     * @param context {@link FacesContext} for the request we are processing
     * @return {@link BehaviorRenderer} instance from the current {@link RenderKit} or null.
     *
     * @throws NullPointerException if <code>context</code> is null. 
     *
     * @since 2.0
     */
    protected BehaviorRenderer getRenderer(FacesContext context) {
    	if (null == context){
            throw new NullPointerException();
    	}
    	BehaviorRenderer renderer = null;
        String rendererType = getRendererType();
        if (null != rendererType){
            RenderKit renderKit = context.getRenderKit();
            if (null != renderKit){
                renderer = renderKit.getBehaviorRenderer(rendererType);
            }
            if (null == renderer){
                if (logger.isLoggable(Level.FINE)){
                    logger.fine("Can't get  behavior renderer for type " + rendererType);
                }				
            }
        } else {
            if(logger.isLoggable(Level.FINE)){
                logger.fine("No renderer-type for behavior " + this.getClass().getName());
            }
        }
        return renderer;
    }
}
