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
    "javax.faces.validator.DoubleRangeValidator.LIMIT",
    "javax.faces.validator.DoubleRangeValidator.MAXIMUM",
    "javax.faces.validator.DoubleRangeValidator.MINIMUM",
    "javax.faces.validator.DoubleRangeValidator.TYPE",
    "javax.faces.validator.LengthValidator.LIMIT",
    "javax.faces.validator.LengthValidator.MAXIMUM",
    "javax.faces.validator.LengthValidator.MINIMUM",
    "javax.faces.validator.LongRangeValidator.LIMIT",
    "javax.faces.validator.LongRangeValidator.MAXIMUM",
    "javax.faces.validator.LongRangeValidator.MINIMUM",
    "javax.faces.validator.LongRangeValidator.TYPE",
    "javax.faces.component.UIInput.REQUIRED",
    "javax.faces.validator.StringRangeValidator.LIMIT",
    "javax.faces.validator.StringRangeValidator.MAXIMUM",
    "javax.faces.validator.StringRangeValidator.MINIMUM",
    "javax.faces.validator.StringRangeValidator.TYPE",
  };

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/message01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message01.jsp FAILED - No Application returned");
    return;
  }

  // Acquire the MessageResources bundle
  MessageResources mr =
    appl.getMessageResources(MessageResources.FACES_API_MESSAGES);
  if (mr == null) {
    out.println("/message01.jsp FAILED - No MessageResources returned");
    return;
  }

  // Test for replacing a Standard Validator Message
  facesContext.setLocale(new Locale("en", "US"));
  Message msg = mr.getMessage(facesContext, "javax.faces.validator.DoubleRangeValidator.LIMIT");
  if (!msg.getSummary().equals("Validation Error:This summary replaces the RI summary")) {
      out.println("/message01.jsp FAILED - Missing replacement message");
      return;
  }

  // Check message identifiers that should be present (en_US)
  facesContext.setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    Message message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message01.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    Message message = mr.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message01.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // All tests passed
  out.println("/message01.jsp PASSED");

%>
