package javax.faces;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.EventObject;

/**
 * The interface used to implement objects which are capable of
 * dispatching an event to appropriate target listeners.
 */
public interface EventDispatcher {

   /**
    * Dispatches the specified Event object to its appropriate target
    * listeners.
    *
    * @param request the ServletRequest object corresponding to the client
    *        request where the associated event was generated
    * @param response the ServletResponse object used to render a response
    *        to the associated request
    * @param event the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if dispatcher is unable to dispatch the
    *         specified event
    */
    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException;

}
