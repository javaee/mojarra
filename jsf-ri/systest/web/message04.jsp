<%@ page contentType="text/plain"
%><%@ page import="java.util.Locale"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.Message"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.context.MessageResources"
%><%

  // Initialize list of message ids
  String list[] = {
    "Custom2A",
    "Custom2B",
    "Custom2C",
  };

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/message04.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message04.jsp FAILED - No Application returned");
    return;
  }

  // Acquire the MessageResources bundle
  MessageResources mr =
    appl.getMessageResources("Custom2A");
  if (mr == null) {
    out.println("/message04.jsp FAILED - No MessageResources returned");
    return;
  }
  Message message = null;

  // Check message identifiers that should be present (en_US)
  facesContext.setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message04.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check specific message characteristics (en_US)
  message = mr.getMessage(facesContext, "Custom2B");
  if (!"This Is Custom 2B Detail (en)".equals(message.getDetail())) {
    out.println("/message04.jsp FAILED - Bad en_US detail '" +
                message.getDetail() + "'");
    return;
  }
  if (!"This Is Custom 2B Summary (en)".equals(message.getSummary())) {
    out.println("/message04.jsp FAILED - Bad en_US summary '" +
                message.getSummary() + "'");
    return;
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message04.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check specific message characteristics (fr_FR)
  message = mr.getMessage(facesContext, "Custom2B");
  if (!"This Is Custom 2B Detail (fr)".equals(message.getDetail())) {
    out.println("/message04.jsp FAILED - Bad fr_FR detail '" +
                message.getDetail() + "'");
    return;
  }
  if (!"This Is Custom 2B Summary (fr)".equals(message.getSummary())) {
    out.println("/message04.jsp FAILED - Bad fr_FR summary '" +
                message.getSummary() + "'");
    return;
  }

  // All tests passed
  out.println("/message04.jsp PASSED");

%>
