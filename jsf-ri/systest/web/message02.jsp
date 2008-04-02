<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="java.util.Locale"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.FacesMessage"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="com.sun.faces.util.MessageFactory"
%><%@ page import="javax.faces.component.UIViewRoot"
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
  facesContext.setViewRoot(new UIViewRoot());
  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message02.jsp FAILED - No Application returned");
    return;
  }

  // Check message identifiers that should be present (en_US)
  facesContext.getViewRoot().setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message02.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.getViewRoot().setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message02.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // All tests passed
  out.println("/message02.jsp PASSED");

%>
