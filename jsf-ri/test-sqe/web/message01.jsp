<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<%@ page contentType="text/html"
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
    "javax.faces.validator.NOT_IN_RANGE",
    "javax.faces.validator.DoubleRangeValidator.MAXIMUM",
    "javax.faces.validator.DoubleRangeValidator.MINIMUM",
    "javax.faces.validator.DoubleRangeValidator.TYPE",
    "javax.faces.validator.LengthValidator.MAXIMUM",
    "javax.faces.validator.LengthValidator.MINIMUM",
    "javax.faces.validator.LongRangeValidator.MAXIMUM",
    "javax.faces.validator.LongRangeValidator.MINIMUM",
    "javax.faces.validator.LongRangeValidator.TYPE",
    "javax.faces.component.UIInput.REQUIRED"
  };

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/message01.jsp FAILED - No FacesContext returned");
    return;
  }
  facesContext.setViewRoot(new UIViewRoot());
  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message01.jsp FAILED - No Application returned");
    return;
  }

  // Test for replacing a Standard Validator Message
  facesContext.getViewRoot().setLocale(new Locale("en", "US"));
  FacesMessage msg = MessageFactory.getMessage(facesContext, "javax.faces.validator.DoubleRangeValidator.LIMIT");
  if (!msg.getSummary().equals("Validation Error:This summary replaces the RI summary")) {
      out.println("/message01.jsp FAILED - Missing replacement message");
      return;
  }

  // Check message identifiers that should be present (en_US)
  facesContext.getViewRoot().setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message01.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.getViewRoot().setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    FacesMessage message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message01.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // All tests passed
  out.println("/message01.jsp PASSED");

%>
