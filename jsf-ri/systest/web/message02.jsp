<%@ page contentType="text/plain"
%><%@ page import="java.util.Locale"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.Message"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.application.MessageResources"
%><%

  // Initialize list of message ids
  String list[] = {
    // PENDING(craigmcc) - put message ids here
  };

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/message02.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message02.jsp FAILED - No Application returned");
    return;
  }

  // Acquire the MessageResources bundle
  MessageResources mr =
    appl.getMessageResources(MessageResources.FACES_IMPL_MESSAGES);
  if (mr == null) {
    out.println("/message02.jsp FAILED - No MessageResources returned");
    return;
  }

  // Check message identifiers that should be present (en_US)
  facesContext.setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    Message message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message02.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    Message message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message02.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // All tests passed
  out.println("/message02.jsp PASSED");

%>
