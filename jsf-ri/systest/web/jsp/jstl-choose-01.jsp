<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-choose-01</title>
</head>
<body>
<h:output_text value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <h:output_text id="comp2a" value="[2a]"/>
    <h:output_text id="comp2z" value="[2z]"/>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <h:output_text id="comp2b" value="[2b]"/>
    <h:output_text id="comp2y" value="[2y]"/>
  </c:when>
  <c:otherwise>
    <h:output_text id="comp2c" value="[2c]"/>
    <h:output_text id="comp2x" value="[2x]"/>
  </c:otherwise>
</c:choose>
<h:output_text value="[3]"/>
</body>
</html>
</f:view>
