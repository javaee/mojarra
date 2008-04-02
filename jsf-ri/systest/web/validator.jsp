<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.validator.Validator"
%><%@ page import="com.sun.faces.systest.TestValidator"
%><%

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // try to retrieve our validator from Application
  Validator result = appl.createValidator("TestValidator");
  // report the result
  if (result == null || 
      !(result instanceof com.sun.faces.systest.TestValidator)) {
    out.println("/validator.jsp FAILED");
    return;
  } else {
      out.println("/validator.jsp PASSED");
  }
  
%>
