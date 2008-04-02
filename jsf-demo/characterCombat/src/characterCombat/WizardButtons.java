package characterCombat;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import java.util.Iterator;

/**
 * <p>Backing bean for wizard style navigation.  This class provides
 * methods that you can point to from your wizard buttons that will
 * return true or false depending on the current page in the
 * application.</p>
 */

public class WizardButtons {

    /** 
     * @return true if the current page has a "back" page.
     */

    public boolean isHasBack() {
	FacesContext context = 
	    createShadowFacesContext(FacesContext.getCurrentInstance());
	NavigationHandler nav = 
	    context.getApplication().getNavigationHandler();
	System.out.println("nav: " + nav);
	nav.handleNavigaton(context, null, "back");
	
	return false;
    }

    /** 
     * @return true if the current page has a "next" page.
     */

    public boolean isHasNext() {
	FacesContext context = 
	    createShadowFacesContext(FacesContext.getCurrentInstance());
	NavigationHandler nav = 
	    context.getApplication().getNavigationHandler();
	//	nav.handleNavigaton(context, null, "next");


	return false;
    }

    /**
     *
     * @return a shallow copy of the argument FacesContext, but with a
     * deep copy of the viewRoot property.  This allows us to call the
     * NavigationHandler.handleNavigaton method without modifying the
     * real FacesContext.
     */

    public FacesContext createShadowFacesContext(FacesContext context) {
	final FacesContext oldContext = context;

	FacesContext result = new FacesContext() {
		private Application application = oldContext.getApplication();
		public Application getApplication() { return application; }

		public Iterator getClientIdsWithMessages() {
		    return oldContext.getClientIdsWithMessages();
		}

		public ExternalContext getExternalContext() {
		    return oldContext.getExternalContext();
		}
		
		public Severity getMaximumSeverity() {
		    return oldContext.getMaximumSeverity();
		}
		
		public Iterator getMessages() {
		    return oldContext.getMessages();
		}
		
		public Iterator getMessages(String clientId) {
		    return oldContext.getMessages(clientId);
		}
		
		public RenderKit getRenderKit() {
		    return oldContext.getRenderKit();
		}
		
		
		public boolean getRenderResponse() {
		    return oldContext.getRenderResponse();
		}
		
		
		public boolean getResponseComplete() {
		    return oldContext.getResponseComplete();
		}
		
		
		public ResponseStream getResponseStream() {
		    return oldContext.getResponseStream();
		}
		
		
		public void setResponseStream(ResponseStream responseStream) {
		    oldContext.setResponseStream(responseStream);
		}
		
		public ResponseWriter getResponseWriter() {
		    return oldContext.getResponseWriter();
		}
		
		
		public void setResponseWriter(ResponseWriter responseWriter) {
		    oldContext.setResponseWriter(responseWriter);
		}
		
		private UIViewRoot root = oldContext.getViewRoot();
		
		public UIViewRoot getViewRoot() { 
		    return root; 
		}
				
		public void setViewRoot(UIViewRoot root) {
		    this.root = root;
		}
		
		public void addMessage(String clientId, FacesMessage message) {
		    oldContext.addMessage(clientId, message);
		}
		
		public void release() {}
		
		public void renderResponse() {} 
		
		
		public void responseComplete() {}
	    };
	return result;
    }
}
