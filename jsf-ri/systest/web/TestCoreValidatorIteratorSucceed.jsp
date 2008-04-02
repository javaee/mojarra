<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
</head>
<body>

<h1>TLV c:iterator with JSF id</h1>
This page should succeed.
<br>
<br>

<f:view>

  <c:forEach begin="0" end="3"  var="i" varStatus="status">
    <c:set var="i" scope="request" value="${i}"/>
    <c:set var="status" scope="request" value="${status}"/>
    <c:set var="id" scope="request" value="foo${status.index}"/>
    Array[<c:out value="${i}"/>]: 
    <h:output_text id="#{id}" value="#{i}"/><br>
  </c:forEach>

</f:view>

</body>
</head>
</html>
