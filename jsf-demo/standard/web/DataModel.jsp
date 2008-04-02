<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<%-- $Id: DataModel.jsp,v 1.12 2004/05/12 18:47:12 ofung Exp $ --%>

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
