<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: DataModel.jsp,v 1.4 2003/11/01 02:35:20 craigmcc Exp $ --%>

<%@ page import="standard.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt"  prefix="fmt" %>
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
                     componentRef="DataModelBean.accountId">
        <f:selectitems   valueRef="DataModelBean.accountIds"/>
      </h:selectone_menu>

      <h:command_button actionRef="DataModelBean.select"
                            value="Select"/>

    </h:panel_grid>

    <h:panel_grid   columnClasses="form-prompt,form-field"
                          columns="2"
                       styleClass="form-background">

      <h:output_text        value="Account Id:"/>

      <h:output_text     valueRef="DataModelBean.customers.rowData.accountId"/>

      <h:output_text        value="Customer Name:"/>

      <h:output_text     valueRef="DataModelBean.customers.rowData.name"/>

      <h:output_text        value="Stock Symbol:"/>

      <h:output_text     valueRef="DataModelBean.customers.rowData.symbol"/>

      <h:output_text        value="Total Sales:"/>

      <h:output_text     valueRef="DataModelBean.customers.rowData.totalSales"/>

    </h:panel_grid>

  </h:form>

  <hr>
  <p><a href='<%= request.getContextPath() + "/" %>'>Back</a>
  to home page.</p>

</body>
</html>
</f:view>
