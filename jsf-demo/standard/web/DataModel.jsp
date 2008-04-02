<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: DataModel.jsp,v 1.9 2004/01/27 21:31:40 eburns Exp $ --%>

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

      <h:selectoneMenu        id="accountId"
                          binding="#{DataModelBean.accountId}">
           <f:selectItems   value="#{DataModelBean.accountIds}"/>
      </h:selectoneMenu>

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
