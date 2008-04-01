/*
 * $Id: RenderContext.java,v 1.9 2002/01/10 22:16:33 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * The class which defines an object representing all contextual
 * information required for handling the rendering phase of a request.
 * A render context provides the following:
 * <ul>
 * <li>information describing the client from which the request
 *     originated, including the client's preferred locale
 * <li>the appropriate render kit to be used to render user-interface
 *     components for the associated client
 * <li>access to objects managed in the scoped namespace
 * <li>the output object to be used for rendering the user-interface
 *     components in the response
 * <li>a render-stack for representing the branch of the component
 *     hierarchy being rendered
 * </ul>
 * <p>
 * In order for a render context to be created, the toolkit must
 * be able to map the request to a specific client-capabilities
 * description, and from that client-capabilities description to
 * a specific render kit.  If the toolkit is unable to make either
 * of those mappings, either because the request came from an
 * unrecognizable client or there exists no render kit mapping for
 * that particular client, then render context creation will fail
 * and a FacesException will be thrown.
 */
public abstract class RenderContext {

    protected RenderContext() {
    }
    
    /**
     * The current value of the ClientCapabilities object.
     * @return ClientCapabilities object which characterizes the client
     *         which initiated the request
     */
    public ClientCapabilities getClientCapabilities() {
	return null;
    }

    /**
     * The current value of the render kit object.
     * @return RenderKit object targeted for the client described by
     *         the ClientCapabilities object
     */
    public abstract RenderKit getRenderKit();

    /**
     * The current value of the locale object.  This may be different
     * from the Locale object in the ClientCapabiliites object.
     * @return Locale object respresenting the user's preferred locale
     */
    public Locale getLocale(){
	return null;
    }

    /**
     * The current value of the Object manager object.
     * @return ObjectManager used to manage application objects in scoped
     *         namespace
     */
    public ObjectManager getObjectManager() {
	return null;
    }

    /**
     * The current value of the Object accessor object.
     * @return ObjectAccessor used to resolve object-reference Strings to
     *         objects
     */
    public ObjectAccessor getObjectAccessor() {
	return null;
    }


    /**
     * The current value of the OutputMethod object.
     * @return OutputMethod object to be used to write all rendering 
     *         of user-interface components for the associated request.
     */
    public abstract OutputMethod getOutputMethod();

    /**
     * Set the current value of the OutputMethod object.
     * @param OutputMethod object to be used to write all rendering
     *        of user-interface components for the associated request.
     */
    public abstract void setOutputMethod(OutputMethod outputMethod);

    /**

    * The HttpSession for this RenderContext. <P>

    * PENDING(edburns): Amy, not sure if a RenderContext should have an
    * HttpSession.


    */

    public abstract HttpSession getSession();

    /**
     * Returns the ancestor of the component currently at the top
     * of the rendering stack by walking down the stack the specified
     * number of levels.  For example, if <code>1</code> was specified
     * as the level, then the immediate parent on the render stack
     * is returned.
     * @param level the number of levels down the stack to access
     *        the ancestor
     * @return the WComponent object on the render stack down the
     *         specified number of levels
     */
    public WComponent peekAtAncestor(int level) {
        return null;
    }

    /**
     * Pushes the specified component on the render stack.  This
     * method is invoked just prior to invoking rendering processing
     * on the specified component.
     * @param c the component to be pushed on the render stack
     * @throws NullPointerException if c is null
     */
    public void pushChild(WComponent c){
    
    }

    /**
     * Pops the current component off the render stack.  This
     * method is invoked after the specified component and all of
     * its descendents have been rendered.  If
     * there is no component currently on the stack, returns null.
     * @return WComponent object corresponding to the component
     *         which was most recently rendered from this render context
     */
    public WComponent popChild() {
        return null;
    }


    // Aim11-2-01: methods for communicating between Renderers (?)
    // per Oracle's suggestion

    // Aim11-2-01: methods for accessing objects in scoped namespace
    // per Oracle's suggestion

    // Aim11-2-01: method for encoding URLs (?)
    // per Oracle's suggestion


}

