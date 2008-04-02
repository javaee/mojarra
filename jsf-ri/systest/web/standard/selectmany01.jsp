<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%
  if (session.getAttribute("status") == null) {
    String[] a = {"1", "2"};
    session.setAttribute("status", a);
  }
%>
<f:view>
  <h:form id="form1">
    <h:selectManyCheckbox value="#{status}">
      <f:selectItem itemValue="1" itemLabel="Open" />
      <f:selectItem itemValue="2" itemLabel="Submitted" />
      <f:selectItem itemValue="3" itemLabel="Accepted" />
      <f:selectItem itemValue="4" itemLabel="Rejected" />
    </h:selectManyCheckbox>
    <h:commandButton id="modify" value="Update" />
    <p>Current model value:
    ${status[0]}, ${status[1]}, ${status[2]}, ${status[3]}
  </h:form>
  <h:form id="form2">
    Resets the rendered values but not the model:
    <h:commandButton id="doNotModify" value="Click" />
  </h:form>
</f:view>
