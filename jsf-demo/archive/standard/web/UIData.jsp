<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>



<%@ page import="standard.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
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

  <h:dataTable columnClasses="list-column-center,
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

    <f:facet             name="header">
      <h:outputText    value="Overall Table Header"/>
    </f:facet>

    <f:facet             name="footer">
      <h:outputText    value="Overall Table Footer"/>
    </f:facet>

    <h:column>
      <%-- Visible checkbox for selection --%>
      <h:selectBooleanCheckbox
                           id="checked"
                      binding="#{UIDataBean.checked}"/>
      <%-- Invisible checkbox for "created" flag --%>
      <h:selectBooleanCheckbox
                           id="created"
                      binding="#{UIDataBean.created}"
                     rendered="false"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Account Id"/>
      </f:facet>
      <f:facet           name="footer">
        <h:outputText  value="A.I. Footer"/>
      </f:facet>
      <h:inputText        id="accountId"
                      binding="#{UIDataBean.accountId}"
                     required="true"
                         size="6"
                        value="#{customer.accountId}">
        <f:valueChangeListener
                         type="standard.LogValueChangedListener"/>
      </h:inputText>
      <h:message          for="accountId"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Customer Name"/>
      </f:facet>
      <f:facet           name="footer">
        <h:outputText  value="C.N. Footer"/>
      </f:facet>
      <h:inputText        id="name"
                     required="true"
                         size="50"
                        value="#{customer.name}">
        <f:valueChangeListener
                         type="standard.LogValueChangedListener"/>
      </h:inputText>
      <h:message          for="name"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Symbol"/>
      </f:facet>
      <f:facet           name="footer">
        <h:outputText  value="S. Footer"/>
      </f:facet>
      <h:inputText        id="symbol"
                     required="true"
                         size="6"
                        value="#{customer.symbol}">
        <f:validateLength
                      maximum="6"
                      minimum="2"/>
        <f:valueChangeListener
                         type="standard.LogValueChangedListener"/>
      </h:inputText>
      <h:message          for="symbol"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Total Sales"/>
      </f:facet>
      <f:facet           name="footer">
        <h:outputText  value="T.S. Footer"/>
      </f:facet>
      <h:outputText       id="totalSales"
                        value="#{customer.totalSales}">
        <f:convertNumber type="currency"/>
      </h:outputText>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:commandButton  id="headerButton"
                       action="#{UIDataBean.header}"
                    immediate="true"
                        value="Header"
                         type="SUBMIT"/>
      </f:facet>
      <f:facet           name="footer">
        <h:commandButton  id="footerButton"
                       action="#{UIDataBean.footer}"
                    immediate="true"
                        value="Footer"
                         type="SUBMIT"/>
      </f:facet>
      <h:commandButton    id="press"
                    action="#{UIDataBean.press}"
                    immediate="true"
                        value="#{UIDataBean.pressLabel}"
                         type="SUBMIT"/>
      <h:commandLink id="click"
                    action="#{UIDataBean.click}"
                    immediate="true">
        <h:outputText
                        value="#{UIDataBean.clickLabel}"/>
      </h:commandLink>
    </h:column>

  </h:dataTable>

  <h:commandButton        id="create1"
                    action="#{UIDataBean.create}"
                    immediate="true"
                        value="Create New Row (immediate=true)"
                         type="SUBMIT"/>

  <h:commandButton        id="create2"
                    action="#{UIDataBean.create}"
                    immediate="false"
                        value="Create New Row (immediate=false)"
                         type="SUBMIT"/>

  <h:commandButton        id="delete1"
                    action="#{UIDataBean.deleteImmediate}"
                    immediate="true"
                        value="Delete Checked (immediate=true)"
                         type="SUBMIT"/>

  <h:commandButton        id="delete2"
                    action="#{UIDataBean.deleteDeferred}"
                    immediate="false"
                        value="Delete Checked (immediate=false)"
                         type="SUBMIT"/>

  <h:commandButton        id="first"
                    action="#{UIDataBean.first}"
                    immediate="true"
                        value="First Page"
                         type="SUBMIT"/>

  <h:commandButton        id="last"
                    action="#{UIDataBean.last}"
                    immediate="true"
                        value="Last Page"
                         type="SUBMIT"/>

  <h:commandButton        id="next"
                    action="#{UIDataBean.next}"
                    immediate="true"
                        value="Next Page"
                         type="SUBMIT"/>

  <h:commandButton        id="previous"
                    action="#{UIDataBean.previous}"
                    immediate="true"
                        value="Prev Page"
                         type="SUBMIT"/>

  <h:commandButton        id="reset"
                    action="#{UIDataBean.reset}"
                    immediate="true"
                        value="Reset Changes"
                         type="SUBMIT"/>

  <h:commandButton        id="update"
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
