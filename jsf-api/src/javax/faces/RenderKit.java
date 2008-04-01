package javax.faces;

import java.util.Iterator;


/**
 * The base class for defining rendering capabilities targeted for
 * a specific client.  This class defines:
 * <ol>
 * <li>The list of component types it supports
 * <li>For each supported component type, a set of one or more
 *     renderers
 * </ol>
 */
public abstract class RenderKit {
    /**
     * Returns a RenderKit instance targeted for the client described
     * by the specified ClientCapabilities instance.
     * @param client ClientCapabilities object describing client
     * @return RenderKit object representing the RenderKit capable of
     *          rendering to the target client
     * @throws NullPointerException if client is null
     */
    public static RenderKit getRenderKitForClient(ClientCapabilities client) {
        return null;
    }

    /**
     * Returns a String representing the name of this render kit.
     * Concrete subclasses must override this method and return a
     * descriptive name.
     * @return A String corresponding to the name of this render kit
     */
    public abstract String getName();

    /**
     * Returns an iterator containing Strings corresponding to the
     * component types supported by this render kit.
     * @see WComponent#getType
     * @return Iterator containing Strings corresponding to supported
     *          component types
     */
    public abstract Iterator getSupportedComponentTypes();

    /**
     * Returns an iterator containing Strings corresponding to the
     * renderers supported for the specified component type.
     * @param componentType string representing the type of component
     * @return Iterator containing Strings corresponding to supported
     *          renderers for the component type
     * @throws NullPointerException if componentType is null
     * @throws FacesException if the specifed componentType is not supported
     *         by this render kit
     */
    // Aim10-26-01: should we have notion of "default" renderer per type?
    public abstract Iterator getRendererNamesForComponent(String componentType);

    /**
     * Returns a renderer corresponding to the specified name.
     * @param name the name of the renderer
     * @return the Renderer instance corresponding to the specified name
     * @throws NullPointerException if name is null
     * @throws FacesException if no renderer exists with the specified name
     */ 
    public abstract Renderer getRenderer(String name) throws FacesException;
 
    /**
     * Invoked when this render kit is first instantiated.  Subclasses
     * should override this method to perform any required initialization.
     */
    protected void initialize() {}

    /**
     * Invoked prior to ..Aim10-29-01: look at servlet spec
     */
    protected void destroy() {}

}
