<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIData.jsp,v 1.2 2003/09/11 21:40:00 craigmcc Exp $ --%>

<%@ page import="standard.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<%
  // Construct a preconfigured customer list in session scope
  List list = (List)
    pageContext.getAttribute("list", PageContext.SESSION_SCOPE);
  if (list == null) {
    list = new ArrayList();
    list.add(new CustomerBean("123456", "Alpha Beta Company", "ABC", 1234.56));
    list.add(new CustomerBean("445566", "General Services, Ltd.", "GS", 33.33));
    list.add(new CustomerBean("654321", "Summa Cum Laude, Inc.", "SCL", 76543.21));
    list.add(new CustomerBean("333333", "Yabba Dabba Doo", "YDD",  333.33));
    for (int i = 10; i < 20; i++) {
      list.add(new CustomerBean("8888" + i,
                                "Customer " + i,
                                "CU" + i,
                                ((double) i) * 10.0));
    }
    pageContext.setAttribute("list", list,
                             PageContext.SESSION_SCOPE);
  }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>
<html>
<head>
  <title>UIData</title>
  <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>
<body>

  <h1>UIData</h1>

  <c:if test="${not empty message}">
    The following events and <code>UIDataBean</code> activities were performed:
    <ul><c:out value="${message}" escapeXml="false"/></ul>
    <hr>
  </c:if>

  <h:form id="standardRenderKitForm" formName="standardRenderKitForm">

  <h:data_table columnClasses="list-column-center,
                               list-column-center, list-column-left,
                               list-column-center, list-column-right,
                               list-column-center"
                 componentRef="UIDataBean.data"
                  footerClass="list-footer"
                  headerClass="list-header"
                           id="table"
	                 rows="5"
                   rowClasses="list-row-even, list-row-odd"
                   styleClass="list-background"
                     valueRef="list"
                          var="customer">

    <h:column>
      <%-- Visible checkbox for selection --%>
      <h:selectboolean_checkbox
                           id="checked"
                 componentRef="UIDataBean.checked"/>
      <%-- Invisible checkbox for "created" flag --%>
      <h:selectboolean_checkbox
                           id="created"
                 componentRef="UIDataBean.created"
                     rendered="false"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Account Id"/>
      </f:facet>
      <h:input_text        id="accountId"
                 componentRef="UIDataBean.accountId"
                     required="true"
                         size="6"
                     valueRef="customer.accountId">
        <f:valuechanged_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:output_errors    for="accountId"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Customer Name"/>
      </f:facet>
      <h:input_text        id="name"
                     required="true"
                         size="50"
                     valueRef="customer.name">
        <f:valuechanged_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:output_errors    for="name"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Symbol"/>
      </f:facet>
      <h:input_text        id="symbol"
                     required="true"
                         size="6"
                     valueRef="customer.symbol">
        <f:validate_length
                      maximum="6"
                      minimum="2"/>
        <f:valuechanged_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:output_errors    for="symbol"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Total Sales"/>
      </f:facet>
      <h:output_text       id="totalSales"
                     valueRef="customer.totalSales"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Commands"/>
      </f:facet>
      <h:command_button    id="press"
                    actionRef="UIDataBean.press"
                    immediate="true"
                     valueRef="UIDataBean.pressLabel"
                         type="SUBMIT"/>
      <h:command_hyperlink id="click"
                    actionRef="UIDataBean.click"
                    immediate="true">
        <h:output_text
                     valueRef="UIDataBean.clickLabel"/>
      </h:command_hyperlink>
    </h:column>

  </h:data_table>

  <h:command_button        id="create"
                    actionRef="UIDataBean.create"
                    immediate="true"
                        value="Create New Row"
                         type="SUBMIT"/>

  <h:command_button        id="delete"
                    actionRef="UIDataBean.delete"
                    immediate="true"
                        value="Delete Checked"
                         type="SUBMIT"/>

  <h:command_button        id="first"
                    actionRef="UIDataBean.first"
                    immediate="true"
                        value="First Page"
                         type="SUBMIT"/>

  <h:command_button        id="last"
                    actionRef="UIDataBean.last"
                    immediate="true"
                        value="Last Page"
                         type="SUBMIT"/>

  <h:command_button        id="next"
                    actionRef="UIDataBean.next"
                    immediate="true"
                        value="Next Page"
                         type="SUBMIT"/>

  <h:command_button        id="previous"
                    actionRef="UIDataBean.previous"
                    immediate="true"
                        value="Prev Page"
                         type="SUBMIT"/>

  <h:command_button        id="reset"
                    actionRef="UIDataBean.reset"
                    immediate="true"
                        value="Reset Changes"
                         type="SUBMIT"/>

  <h:command_button        id="update"
                    actionRef="UIDataBean.update"
                    immediate="false"
                        value="Save Changes"
                         type="SUBMIT"/>

  </h:form>

  <hr>
  <p><a href='<%= request.getContextPath() + "/" %>'>Back</a>
  to home page.</p>

</body>
</html>
</f:view>
