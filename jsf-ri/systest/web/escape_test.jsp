<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%--
   Copyright 2004 Sun Microsystems, Inc.  All rights reserved.
   SUN PROPRIETARY/CONFIDENTIAL.  Use is subject license terms.
--%>

<%-- $Id: escape_test.jsp,v 1.6 2004/05/12 18:30:52 ofung Exp $ --%>

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
