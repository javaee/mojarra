<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-choose-02</title>
</head>
<body>
<h:output_text value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <f:subview id="naming2a">
      <h:output_text value="[2a]"/>
      <h:output_text value="[2z]"/>
    </f:subview>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <f:subview id="naming2b">
      <h:output_text value="[2b]"/>
      <h:output_text value="[2y]"/>
    </f:subview>
  </c:when>
  <c:otherwise>
    <f:subview id="naming2c">
      <h:output_text value="[2c]"/>
      <h:output_text value="[2x]"/>
    </f:subview>
  </c:otherwise>
</c:choose>
<h:output_text value="[3]"/>
</body>
</html>
</f:view>
