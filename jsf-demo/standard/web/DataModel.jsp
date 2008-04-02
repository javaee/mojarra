<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: DataModel.jsp,v 1.7 2004/01/10 01:21:05 horwat Exp $ --%>

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

    <h:panel_grid   columnClasses="form-prompt,form-field"
                          columns="2"
                       styleClass="form-background">

      <h:output_text        value="Account Id:"/>

      <h:selectone_menu        id="accountId"
                          binding="#{DataModelBean.accountId}">
           <f:selectitems   value="#{DataModelBean.accountIds}"/>
      </h:selectone_menu>

      <h:command_button action="#{DataModelBean.select}"
                            value="Select"/>

    </h:panel_grid>

    <h:panel_grid   columnClasses="form-prompt,form-field"
                          columns="2"
                       styleClass="form-background">

      <h:output_text        value="Account Id:"/>

      <h:output_text        value="#{DataModelBean.customers.rowData.accountId}"/>

      <h:output_text        value="Customer Name:"/>

      <h:output_text        value="#{DataModelBean.customers.rowData.name}"/>

      <h:output_text        value="Stock Symbol:"/>

      <h:output_text        value="#{DataModelBean.customers.rowData.symbol}"/>

      <h:output_text        value="Total Sales:"/>

      <h:output_text        value="#{DataModelBean.customers.rowData.totalSales}"/>

    </h:panel_grid>

  </h:form>

  <hr>
  <p><a href='<%= request.getContextPath() + "/" %>'>Back</a>
  to home page.</p>

</body>
</html>
</f:view>
