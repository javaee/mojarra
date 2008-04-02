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

<%-- $Id: DataModel.jsp,v 1.13 2005/08/22 22:09:43 ofung Exp $ --%>

<%@ page import="standard.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>DataModel</title>
  <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>
<body>

  <h:form id="myform">

    <h:panelGrid   columnClasses="form-prompt,form-field"
                          columns="2"
                       styleClass="form-background">

      <h:outputText        value="Account Id:"/>

      <h:selectOneMenu        id="accountId"
                          binding="#{DataModelBean.accountId}">
           <f:selectItems   value="#{DataModelBean.accountIds}"/>
      </h:selectOneMenu>

      <h:commandButton action="#{DataModelBean.select}"
                            value="Select"/>

    </h:panelGrid>

    <h:panelGrid   columnClasses="form-prompt,form-field"
                          columns="2"
                       styleClass="form-background">

      <h:outputText        value="Account Id:"/>

      <h:outputText        value="#{DataModelBean.customers.rowData.accountId}"/>

      <h:outputText        value="Customer Name:"/>

      <h:outputText        value="#{DataModelBean.customers.rowData.name}"/>

      <h:outputText        value="Stock Symbol:"/>

      <h:outputText        value="#{DataModelBean.customers.rowData.symbol}"/>

      <h:outputText        value="Total Sales:"/>

      <h:outputText        value="#{DataModelBean.customers.rowData.totalSales}"/>

    </h:panelGrid>

  </h:form>

  <hr>
  <p><a href='<%= request.getContextPath() + "/" %>'>Back</a>
  to home page.</p>

</body>
</html>
</f:view>
