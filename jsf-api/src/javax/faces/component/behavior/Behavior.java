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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;
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
	

    private static final Logger logger = Logger.getLogger("javax.faces.component.behavior",
    "javax.faces.LogStrings");

    /**
     * <p>Our {@link javax.faces.event.FacesListener}s.  This data
     * structure is lazily instantiated as necessary.</p>
     */
    private List<FacesListener> listeners;

    /**
     * <p class="changed_added_2_0">Return the script that implements this
     * Behavior's client-side logic.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param component the component instance that generates event.
     * @param eventName name of the client-side event.  If this argument is
     * <code>null</code> it is assumed the caller will include the 
     * client-side event name with the return value from this method.
     * Default implementation delegates that call to 
     * {@link BehaviorRenderer#getScript(FacesContext, UIComponent, Behavior, String)} 
     * method.
     * @return script that provides the client-side behavior
     *
     * @since 2.0
     */      
    public String getScript(FacesContext context,
                                     UIComponent component,
                                     String eventName) {
    	// TODO - check null parameters.
    	BehaviorRenderer renderer = getRenderer(context);
		String script = null;
    	if(null != renderer){
			script = renderer.getScript(context, component, this, eventName);
    	}
        return script;
    }

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
     * @param context {@link eventName} the event name associated with this {@link Behavior} 
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     *
     * @since 2.0
     */
    public void decode(FacesContext context,
                       UIComponent component,
                       String eventName) {
    	// TODO - check null parameters.
    	BehaviorRenderer renderer = getRenderer(context);
    	if(null != renderer){
    		renderer.decode(context, component, eventName);
    	}
    }
    
    /**
     * <p class="changed_added_2_0">Get type of the {@link BehaviorRenderer} if instance uses
     * bridge patterns for a render-kit depended functionality.
     * </p>
     * @return the {@link BehaviorRenderer} type for this {@link Behavior}, if any.
     *
     * @since 2.0
     */
    public abstract String getRendererType();
    
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
    	if(null == context){
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
    public abstract void broadcast(BehaviorEvent event)
        throws AbortProcessingException;

    /**
     * <p class="changed_added_2_0">Add the specified {@link FacesListener} 
     * to the set of listeners registered to receive event notifications 
     * from this {@link Behavior}.
     * It is expected that {@link Behavior} classes acting as event sources
     * will have corresponding typesafe APIs for registering listeners of the
     * required type, and the implementation of those registration methods
     * will delegate to this method.  For example:</p>
     * <pre>
     * public class AjaxBehaviorEvent extends BehaviorEvent { ... }
     *
     * public interface AjaxBehaviorListener extends FacesListener {
     *   public void processAjaxBehavior(FooEvent event);
     * }
     *
     * public class AjaxBehavior extends Behavior {
     *   ...
     *   public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     addFacesListener(listener);
     *   }
     *   public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     removeFacesListener(listener);
     *   }
     *   ...
     * }
     * </pre>
     *
     * @param listener The {@link FacesListener} to be registered
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     *
     * @since 2.0
     */
    protected void addFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            //noinspection CollectionWithoutInitialCapacity
            listeners = new ArrayList<FacesListener>();
        }
        listeners.add(listener);

    }

    /**
     * <p class="changed_added_2_0">Remove the specified 
     * {@link FacesListener} from the set of listeners
     * registered to receive event notifications from this 
     * {@link Behavior}.
     *
     * @param listener The {@link FacesListener} to be deregistered
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     *
     * @since 2.0
     */
    protected void removeFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }


    
    public void processEvent(BehaviorEvent event) {
		
    }

}
