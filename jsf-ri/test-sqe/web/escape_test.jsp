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

<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: escape_test.jsp,v 1.2 2005/08/22 22:11:47 ofung Exp $ --%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<%@ page import="javax.faces.context.FacesContext"%>
<%
  String textToEscape = "This text <b>has angle brackets</b>.";
  FacesContext.getCurrentInstance().getExternalContext().
   getRequestMap().put("textToEscape", textToEscape);  
%>

<f:view>

  <html>

    <head>
      <title>Test of outputText Escaping</title>
    </head>

    <body>

      <h1>Test of outputText Escaping</h1>

      <p>
        [DEFAULT]
        <h:outputText value="#{textToEscape}"/>
        The angle brackets MUST be escaped.
      </p>

      <p>
        [FALSE]
        <h:outputText value="#{textToEscape}" escape="false"/>
        The angle brackets MUST NOT be escaped.
      </p>

      <p>
        [TRUE]
        <h:outputText value="#{textToEscape}" escape="true"/>
        The angle brackets MUST be escaped.
      </p>

    </body>

  </html>

</f:view>
