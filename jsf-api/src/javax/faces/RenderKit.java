/*
 * $Id: RenderKit.java,v 1.12 2002/01/25 18:35:07 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;
import java.util.Iterator;


/**
 * The base class for encapsulating the rendering capabilities targeted for
 * a specific client.  A render kit is responsible for transforming
 * the definition of the user-interface (represented by a hierarchy
 * of UIComponent objects) into output appropriate for a target client.  
 */
public abstract class RenderKit {

    /*
     * Returns a String representing the name of this render kit
     * Concrete subclasses must override this method and return a
     * descriptive name.
     * @return A String corresponding to the name of this render kit
     */
    public abstract String getName();

    /**
     * Returns an iterator containing Strings corresponding to the
     * component types supported by this render kit.
     * @see UIComponent#getType
     * @return Iterator containing Strings corresponding to supported
     *          component types
     */
    public abstract Iterator getSupportedComponentTypes();

    /**
     * Returns an iterator containing Strings corresponding to the
     * renderers supported for the specified component type.
     * @see UIComponent#getRendererType
     * @param componentType string representing the type of component
     * @return Iterator containing Strings corresponding to supported
     *          renderers for the component type
     * @throws NullPointerException if componentType is null
     * @throws FacesException if the specifed componentType is not supported
     *         by this render kit
     */
    // Aim10-26-01: should we have notion of "default" renderer per type?
    public abstract Iterator getRendererTypesForComponent(String componentType) throws FacesException;

    /**
     * Returns a renderer corresponding to the specified type.
     * @param type String containing the type of the renderer
     * @return the Renderer instance corresponding to the specified name
     * @throws NullPointerException if type is null
     * @throws FacesException if no renderer exists with the specified type
     */ 
    public abstract Renderer getRenderer(String type) throws FacesException;

    /**
     * Decodes any events described by the request represented
     * in the specified event context and places appropriate Event
     * objects on the event queue provided by the event context.
     * Event decoding is handled by the render kit because the render kit
     * is responsible for encoding the events when rendering user-interface
     * components in the response to the client.  The precise encoding
     * and decoding of client-generated events is defined by the render kit.
     *
     * @param ec the event context used for the event processing phase of
     *           the request
     */
    public abstract void queueEvents(EventContext ec);
 
    /**
     * Invoked when this render kit is first instantiated.  Subclasses
     * should override this method to perform any required initialization.
     */
    protected void initialize() {}

    /**
     * Subclasses should override this method to perform any clean-up
     * and releasing of resources.
     */
    protected void destroy() {}
    //Aim11-2-02: who would invoke destroy??


}

