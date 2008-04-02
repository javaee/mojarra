<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib.tld" %>

<f:view>
<html>
<head>
<title>jsp-dynamic-01</title>
</head>
<body>
<h:output_text value="[A]"/>
<s:dynamic id="dynamic"/>
<h:output_text value="[Z]"/>
</body>
</html>
</f:view>
