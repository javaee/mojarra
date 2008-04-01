package javax.faces;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.EventObject;

/**
 * A class which implements the dispatching of form events
 * to appropriate target form listener objects.  
 *
 * @see WForm#addFormListener
 */
public class FormDispatcher implements EventDispatcher {

   /**
    * Dispatches the specified form event to the form
    * listeners registered for the event's source component.  
    *
    * @param request the ServletRequest object corresponding to the request
    *        which generated this event
    * @param response the ServletResponse object used to render a response
    *        to the associated request
    * @param event the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if the event is not an instance of
    *         <code>FormEvent</code>
    */
    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException {
    }

}
