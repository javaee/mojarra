<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-choose-02</title>
</head>
<body>
<h:outputText value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <f:subview id="naming2a">
      <h:outputText value="[2a]"/>
      <h:outputText value="[2z]"/>
    </f:subview>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <f:subview id="naming2b">
      <h:outputText value="[2b]"/>
      <h:outputText value="[2y]"/>
    </f:subview>
  </c:when>
  <c:otherwise>
    <f:subview id="naming2c">
      <h:outputText value="[2c]"/>
      <h:outputText value="[2x]"/>
    </f:subview>
  </c:otherwise>
</c:choose>
<h:outputText value="[3]"/>
</body>
</html>
</f:view>
