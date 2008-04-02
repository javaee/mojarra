<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<%--
      NOTE: The included "output_text" component tags are from
      the systest tag library, not the standard html tag library,
      in order to bypass the normal requirement for an "id"
--%>


<f:view>
<html>
<head>
<title>jstl-import-04</title>
</head>
<body>
<h:output_text value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <c:import url="jstl-import-04a.jsp"/>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <c:import url="jstl-import-04b.jsp"/>
  </c:when>
  <c:otherwise>
    <c:import url="jstl-import-04c.jsp"/>
  </c:otherwise>
</c:choose>
<h:output_text value="[3]"/>
</body>
</html>
</f:view>
