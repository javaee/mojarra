<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib.tld" %>

<f:view>
<html>
<head>
<title>jstl-if-05</title>
</head>
<body>
[1]
<c:if test="${param.cond}">
  <h:output_text       id="other2"  value="[2]"/>
  [3]
  <s:children id="kids">
    [4a]
    <h:output_text     id="kids4b" value="[4b]"/>
    [4c]
  </s:children>
  <h:output_text       id="other5" value="[5]"/>
  [6]
</c:if>
[7]
</body>
</html>
</f:view>
