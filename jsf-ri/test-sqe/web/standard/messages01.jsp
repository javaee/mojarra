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
    <title>messages01.jsp</title>

<%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.FacesMessage"
%><%@ page import="com.sun.faces.util.MessageFactory"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/messages01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // get a message
  FacesMessage message = MessageFactory.getMessage(facesContext, 
        "javax.faces.component.UIInput.REQUIRED");
  if (message == null) {
    out.println("/messages01.jsp FAILED - No message returned");
    return;
  }

  facesContext.addMessage(null, message);

%>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<STYLE TYPE="text/css" MEDIA=screen>
<!--
.errors { 
  background-color: #7171A5;
  border: 5px outset #71A5A5;
  border-collapse: collapse;
  font-family: sans-serif;
  font-size: 14pt;
  padding: 10px;
}
-->
</STYLE>
  </head>

  <body>

<f:view>


<h:messages styleClass="errors"/> 

</f:view>



  </body>
</html>
