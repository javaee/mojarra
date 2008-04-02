<%@ page contentType="text/html"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.convert.Converter"
%><%@ page import="com.sun.faces.systest.TestConverter"
%><%

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // replace mappings provided by the JSF implementation in order to 
  // customize the behavior of standard JSF features.
  appl.addConverter("number", "com.sun.faces.systest.TestConverter");

  // try to retrieve our component from Application
  Converter result = appl.createConverter("number");
  // report the result
  if (result == null || 
      !(result instanceof com.sun.faces.systest.TestConverter)) {
    out.println("/converter01.jsp FAILED");
    return;
  } else {
      out.println("/converter01.jsp PASSED");
  }
  
%>
