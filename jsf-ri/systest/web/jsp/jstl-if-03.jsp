<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-if-03</title>
</head>
<body>
[1]
<c:if test="${param.cond}">
  [2]
  <h:outputText id="cond" value="[3]"/>
  [4]
</c:if>
[5]
</body>
</html>
</f:view>
