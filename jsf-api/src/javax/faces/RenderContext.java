package javax.faces;

import java.util.Locale;

/**
 * The interface which defines an object representing all contextual
 * information required for rendering a user-interface component
 * in a particular request cycle.
 */
public interface RenderContext {

    /**
     * Returns the render kit used to render components for this
     * render context.
     * @return RenderKit object used to render components
     */
    public RenderKit getRenderKit();

    /**
     * Returns the Locale of the client where the request originated.
     * returns Locale object respresenting client's locale
     */
    public Locale getLocale();

    // Aim10-26-01: need to ask Oracle about these..
    public WComponent getAncestorComponent(int index);

    /**
     * Pushes the specified component on the render stack.  This
     * method is invoked just prior to passing this render context
     * to the preRender method on the specified component.
     * @param c the component to be pushed on the render stack
     * @throws NullPointerException if c is null
     */
    public void pushChild(WComponent c);

    /**
     * Pops the current component off the render stack.  This
     * method is invoked just after this render context is passed
     * to the postRender method on the specified component.
     */
    public void popChild();

    /**
     * Returns the OutputMethod object which should be used to
     * write all rendering of user-interfaces components.
     * @return OutputMethod object to be used to render output
     */
    public OutputMethod getOutputMethod();

    // methods for communicating between Renderers (?)
    // per Oracle's suggestion

    // methods for accessing objects in scoped namespace
    // per Oracle's suggestion

    // method for encoding URLs (?)
    // per Oracle's suggestion

}
