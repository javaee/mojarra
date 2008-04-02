<%@ page import="javax.faces.FactoryFinder"
      %>
<%@ page import="javax.faces.context.FacesContext"
      %>
<%@ page import="javax.faces.context.FacesContextFactory"
      %>
<%@ page import="javax.faces.context.ResponseWriter"
      %>
<%@ page import="javax.faces.lifecycle.Lifecycle"
      %>
<%@ page import="javax.faces.lifecycle.LifecycleFactory"
      %>
<%@ page import="javax.faces.render.RenderKit"
      %>
<%@ page import="javax.faces.render.RenderKitFactory"
      %>
<%@ page import="java.io.StringWriter"
      %>
<%

    LifecycleFactory lFactory = (LifecycleFactory)
          FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle =
          lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

    FacesContextFactory facesContextFactory = (FacesContextFactory)
          FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = facesContextFactory.getFacesContext(
          config.getServletContext(), request, response, lifecycle);

    RenderKitFactory renderKitFactory = (RenderKitFactory)
          FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = renderKitFactory.getRenderKit(facesContext,
                                                        RenderKitFactory.HTML_BASIC_RENDER_KIT);
    ResponseWriter writer = null;

    // see that the proper content type is picked up based on the
    // preferred XHTML ServletContext Init setting..
    writer = renderKit
          .createResponseWriter(new StringWriter(), null, "ISO-8859-1");
    out.println(writer.getContentType());
%>
