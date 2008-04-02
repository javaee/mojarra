<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.component.UIComponent"
%><%@ page import="com.sun.faces.systest.TestComponent"
%><%

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // replace mappings provided by the JSF implementation in order to 
  // customize the behavior of standard JSF features.
  UIComponent oldForm = appl.createComponent("javax.faces.Form");
  appl.addComponent("javax.faces.Form", "com.sun.faces.systest.TestComponent");

  // try to retrieve our component from Application
  UIComponent result = appl.createComponent("javax.faces.Form");
  // report the result
  if (result == null || 
      !(result instanceof com.sun.faces.systest.TestComponent)) {
    out.println("/component01.jsp FAILED");
    return;
  } else {
      out.println("/component01.jsp PASSED");
  }

  // restore the old mapping
  appl.addComponent("javax.faces.Form", oldForm.getClass().getName());
  
%>
