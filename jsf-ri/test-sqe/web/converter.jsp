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

  // try to retrieve the converter id from Application
  Converter result = appl.createConverter("TestConverter");
  // report the result
  if (result == null || 
      !(result instanceof com.sun.faces.systest.TestConverter)) {
    out.println("/converter.jsp FAILED");
    return;
  } else {
      out.println("/converter.jsp PASSED");
  }
  
%>
