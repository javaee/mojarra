<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>componentRef01.jsp</title>

<%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.Message"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.MessageResources"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.component.UIInput"
%><%@ page import="javax.faces.el.ValueBinding"
%>
 <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
 <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>
  <body>
      <f:view>
          <h:input_text id="username" componentRef="usernamecomponent" size="20"/> 
      </f:view>
  </body>
</html>
<%

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/componentRef01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  ValueBinding binding = appl.getValueBinding("usernamecomponent");
  Object result = binding.getValue(facesContext);
  if (result == null || !(result instanceof UIInput)) {
      System.out.println("/componentRef01.jsp FAILED - Couldn't retrieve component.");
      return;
  }

  UIInput usernamecomponent = (UIInput)result;
  String size = (String) usernamecomponent.getAttribute("size");
  if ( !(size.equals("20"))) {
      System.out.println("/componentRef01.jsp FAILED - Invalid value for size attribute");
      return;
  } 

  String maxlength = (String) usernamecomponent.getAttribute("maxlength");
  if ( !(maxlength.equals("32"))) {
      System.out.println("/componentRef01.jsp FAILED - Invalid value for maxlength attribute");
      return;
  } 
%>

