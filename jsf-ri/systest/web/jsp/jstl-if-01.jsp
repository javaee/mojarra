<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-if-01</title>
</head>
<body>
<h:output_text value="[First]"/>
<c:if test="${param.cond}">
  <h:output_text id="cond" value="[Second]"/>
</c:if>
<h:output_text value="[Third]"/>
</body>
</html>
</f:view>
