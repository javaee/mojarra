package javax.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.EventObject;

/**
 * The class which defines an object representing all contextual
 * information required for handling the event-processing phase
 * of a request.
 */
public abstract class EventContext {

    protected EventContext() {
    }
    
    /**
     * @return ClientCapabilities object which characterizes the client
     *         which initiated the request
     */
    public ClientCapabilities getClientCapabilities() {
	// Get this from the RenderContext
	return null; //compile
    }

    /**
     * @return ServletRequest object representing the client request
     */
    public ServletRequest getRequest() {
	// We're given this on construction
	return null; //compile
    }

    /**
     * @return ServletResponse object used to write response to the client
     *         request
     */
    public ServletResponse getResponse() {
	// We're given this on construction
	return null; //compile
    }

    /**
     * @return EventQueue object used to post any events originating from client request
     */
    public EventQueue getEventQueue() {
	// We create this
	return null; //compile
    }

    /**
     * @throws NullPointerException if event is null
     * @return EventDispatcher object used to obtain an object which can
     *         dispatched the specified event
     */
    public EventDispatcher getEventDispatcher(EventObject event) {
	// We create this
	return null; //compile
    }

    /**
     * @return NavigationHandler object used to configure the navigational
     *         result of processing events originating from this request
     */
    public NavigationHandler getNavigationHandler() {
	// We create this
	return null; //compile
    }

    /**
     * @return ObjectManager used to manage application objects in scoped
     *         namespace
     */
    public ObjectManager getObjectManager() {
	// We get this from the RenderContext
	return null; //compile
    }

    /**
     * @return ObjectAccessor used to resolve object-reference Strings to
     *         objects
     */
    public ObjectAccessor getObjectAccessor() {
	// We get this from the RenderContext
	return null; //compile
    }


}

