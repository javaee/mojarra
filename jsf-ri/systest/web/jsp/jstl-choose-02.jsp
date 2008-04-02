<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib.tld" %>

<%--
      NOTE: The nested "output_text" component tags are from
      the systest tag library, not the standard html tag library,
      in order to bypass the normal requirement for an "id"
--%>

<f:view>
<html>
<head>
<title>jstl-choose-02</title>
</head>
<body>
<h:output_text value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <s:naming id="naming2a">
      <s:output_text value="[2a]"/>
      <s:output_text value="[2z]"/>
    </s:naming>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <s:naming id="naming2b">
      <s:output_text value="[2b]"/>
      <s:output_text value="[2y]"/>
    </s:naming>
  </c:when>
  <c:otherwise>
    <s:naming id="naming2c">
      <s:output_text value="[2c]"/>
      <s:output_text value="[2x]"/>
    </s:naming>
  </c:otherwise>
</c:choose>
<h:output_text value="[3]"/>
</body>
</html>
</f:view>
