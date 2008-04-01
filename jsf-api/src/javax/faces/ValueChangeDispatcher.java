package javax.faces;

/**
 * A class which implements the dispatching of value-change events
 * to appropriate target value-change listener objects.  
 *
 * @see WSelectBoolean#addValueChangeListener
 * @see WSelectOne#addValueChangeListener
 * @see WTextEntry#addValueChangeListener
 */
public class ValueChangeDispatcher implements EventDispatcher {

   /**
    * Dispatches the specified value-change event to the value-change
    * listeners registered for the event's source component.  
    *
    * @param request the ServletRequest object corresponding to the client
    *        request where the associated event was generated
    * @param response the ServletResponse object used to render a response
    *        to the associated request
    * @param e the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if the event is not an instance of
    *         <code>ValueChangeEvent</code>
    */
    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject e) throws IOException, FacesException {
    }

}
