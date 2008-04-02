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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>component01.jsp</title>

<%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.component.UIInput"
%><%@ page import="javax.el.ValueExpression"
%>
 <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
 <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>
  <body>
      <f:view>
          <h:inputText id="username" binding="#{usernamecomponent}" size="20"/> 
      </f:view>
  </body>
</html>
<%

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/component01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  ValueExpression binding = appl.getExpressionFactory().
    createValueExpression(facesContext.getELContext(),"usernamecomponent", Object.class);
  Object result = binding.getValue(facesContext.getELContext());
  if (result == null || !(result instanceof UIInput)) {
      System.out.println("/component01.jsp FAILED - Couldn't retrieve component.");
      return;
  }

  UIInput usernamecomponent = (UIInput)result;
  String size = (String) usernamecomponent.getAttributes().get("size");
  if ( !(size.equals("20"))) {
      System.out.println("/component01.jsp FAILED - Invalid value for size attribute");
      return;
  } 

  String maxlength = (String) usernamecomponent.getAttributes().get("maxlength");
  if ( !(maxlength.equals("32"))) {
      System.out.println("/component01.jsp FAILED - Invalid value for maxlength attribute");
      return;
  } 
%>

