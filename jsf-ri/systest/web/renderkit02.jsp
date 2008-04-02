<%@ page contentType="text/html"
      %>
<%@ page import="javax.faces.FactoryFinder"
      %>
<%@ page import="javax.faces.component.UIViewRoot"
      %>
<%@ page import="javax.faces.context.FacesContext"
      %>
<%@ page import="javax.faces.context.FacesContextFactory"
      %>
<%@ page import="javax.faces.lifecycle.Lifecycle"
      %>
<%@ page import="javax.faces.lifecycle.LifecycleFactory"
      %>
<%@ page import="javax.faces.render.RenderKitFactory"
      %>
<%

    // This test demonstrates the request processing lifecycle of 
// a "non-faces" request --->  faces response
// It uses the "default" renderkit to show how a renderkit can be
// set.
//
    // Create a Lifecycle
    //
    LifecycleFactory lFactory = (LifecycleFactory)
          FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle =
          lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    if (lifecycle == null) {
        out.println("/renderkit02.jsp FAILED - Could not create Lifecycle");
        return;
    }

    // Create a FacesContext 
    //
    FacesContextFactory facesContextFactory = (FacesContextFactory)
          FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = facesContextFactory.getFacesContext(
          config.getServletContext(), request, response, lifecycle);
    if (facesContext == null) {
        out.println("/renderkit02.jsp FAILED - Could not create FacesContext");
        return;
    }

    // Acquire a View..
    //
    UIViewRoot view = facesContext.getApplication().getViewHandler()
          .restoreView(facesContext, "/renderkit02A.jsp");
    if (view == null) {
        view = facesContext.getApplication().getViewHandler()
              .createView(facesContext, "/renderkit02A.jsp");
    }
    // Set the RenderKitFactory.HTML_BASIC_RENDER_KIT renderkit Id
    //
    view.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    facesContext.setViewRoot(view);

    facesContext.renderResponse();

    lifecycle.execute(facesContext);
    lifecycle.render(facesContext);

    // All tests passed
    //
    out.println("/renderkit02.jsp PASSED");
%>
