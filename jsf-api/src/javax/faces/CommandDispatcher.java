package javax.faces;

/**
 * A class which implements the dispatching of command events
 * to appropriate target command listener objects.  This command
 * dispatcher implements appropriate flow-control when it
 * dispatches to listeners which implement the <code>Command</code>
 * interface.
 *
 * @see WCommand#addCommandListener
 */
public class CommandDispatcher implements EventDispatcher {

   /**
    * Dispatches the specified command event object to the command
    * listeners registered for the event's source component.  
    * If a command listener implements the <code>Command</code>
    * interface, it will implement appropriate flow-control after
    * the <code>doCommand</code>method executes:
    * <ul>
    * <li>If the command succeeds (no exception is thrown) it will
    *     redirect the request to the path described by the command
    *     object's <code>completionPath</code> property.  If 
    *     <code>completionPath</code> is null, no redirection will
    *     occur and the current request will proceed to be processed.
    * <li>If the command fails (an exception is thrown) it will
    *     redirect the request to the path described by the command
    *     object's <code>errorPath</code> property.  If <code>errorPath</code>
    *     is null, no redirection will occur and the current request
    *     will proceed to be processed.
    * </ul>
    *
    * @param request the ServletRequest object corresponding to the client
    *        request where the associated event was generated
    * @param response the ServletResponse object used to render a response
    *        to the associated request
    * @param e the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if the event is not an instance of
    *         <code>CommandEvent</code>
    */
    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject e) throws IOException, FacesException {
    }

}
