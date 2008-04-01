package javax.faces;

import javax.servlet.ServletRequest;
import java.util.Locale;

/**
 * The class which defines an object representing all contextual
 * information required for rendering user-interface components
 * described by the resource requested by the client.  A render context
 * provides the following:
 * <ul>
 * <li>information describing the client from which the request
 *     originated, including the client's preferred locale
 * <li>the appropriate render kit to be used to render user-interface
 *     components for the associated client
 * <li>the output object to be used for rendering the user-interface
 *     components in the response
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
     * Returns a RenderKit instance targeted for the client described
     * by the specified ClientCapabilities instance.
     * @return RenderKit object used to render components for the
     *         associated request
     */
    public abstract RenderKit getRenderKit();

    /**
     * The current value of the locale object.
     * return Locale object respresenting client's locale
     */
    public Locale getLocale(){
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
     * method is invoked just prior to passing this render context
     * to the render method on the specified component.
     * @param c the component to be pushed on the render stack
     * @throws NullPointerException if c is null
     */
    public void pushChild(WComponent c){
    
    }

    /**
     * Pops the current component off the render stack.  This
     * method is invoked just after this render context is passed
     * to the postRender method on the specified component.  If
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

