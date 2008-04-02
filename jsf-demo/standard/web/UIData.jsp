<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIData.jsp,v 1.8 2003/12/17 15:20:00 rkitain Exp $ --%>

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

  <h:form id="standardRenderKitForm">

  <h:data_table columnClasses="list-column-center,
                               list-column-center, list-column-left,
                               list-column-center, list-column-right,
                               list-column-center"
                      binding="#{UIDataBean.data}"
                  footerClass="list-footer"
                  headerClass="list-header"
                           id="table"
	                 rows="5"
                   rowClasses="list-row-even, list-row-odd"
                   styleClass="list-background"
                        value="#{list}"
                          var="customer">

    <h:column>
      <%-- Visible checkbox for selection --%>
      <h:selectboolean_checkbox
                           id="checked"
                      binding="#{UIDataBean.checked}"/>
      <%-- Invisible checkbox for "created" flag --%>
      <h:selectboolean_checkbox
                           id="created"
                      binding="#{UIDataBean.created}"
                     rendered="false"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Account Id"/>
      </f:facet>
      <h:input_text        id="accountId"
                      binding="#{UIDataBean.accountId}"
                     required="true"
                         size="6"
                        value="#{customer.accountId}">
        <f:valuechange_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:messages    for="accountId"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Customer Name"/>
      </f:facet>
      <h:input_text        id="name"
                     required="true"
                         size="50"
                        value="#{customer.name}">
        <f:valuechange_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:messages    for="name"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Symbol"/>
      </f:facet>
      <h:input_text        id="symbol"
                     required="true"
                         size="6"
                        value="#{customer.symbol}">
        <f:validate_length
                      maximum="6"
                      minimum="2"/>
        <f:valuechange_listener
                         type="standard.LogValueChangedListener"/>
      </h:input_text>
      <h:messages    for="symbol"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Total Sales"/>
      </f:facet>
      <h:output_text       id="totalSales"
                        value="#{customer.totalSales}"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:output_text  value="Commands"/>
      </f:facet>
      <h:command_button    id="press"
                    action="#{UIDataBean.press}"
                    immediate="true"
                        value="#{UIDataBean.pressLabel}"
                         type="SUBMIT"/>
      <h:command_link id="click"
                    action="#{UIDataBean.click}"
                    immediate="true">
        <h:output_text
                        value="#{UIDataBean.clickLabel}"/>
      </h:command_link>
    </h:column>

  </h:data_table>

  <h:command_button        id="create"
                    action="#{UIDataBean.create}"
                    immediate="true"
                        value="Create New Row"
                         type="SUBMIT"/>

  <h:command_button        id="delete"
                    action="#{UIDataBean.delete}"
                    immediate="true"
                        value="Delete Checked"
                         type="SUBMIT"/>

  <h:command_button        id="first"
                    action="#{UIDataBean.first}"
                    immediate="true"
                        value="First Page"
                         type="SUBMIT"/>

  <h:command_button        id="last"
                    action="#{UIDataBean.last}"
                    immediate="true"
                        value="Last Page"
                         type="SUBMIT"/>

  <h:command_button        id="next"
                    action="#{UIDataBean.next}"
                    immediate="true"
                        value="Next Page"
                         type="SUBMIT"/>

  <h:command_button        id="previous"
                    action="#{UIDataBean.previous}"
                    immediate="true"
                        value="Prev Page"
                         type="SUBMIT"/>

  <h:command_button        id="reset"
                    action="#{UIDataBean.reset}"
                    immediate="true"
                        value="Reset Changes"
                         type="SUBMIT"/>

  <h:command_button        id="update"
                    action="#{UIDataBean.update}"
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
