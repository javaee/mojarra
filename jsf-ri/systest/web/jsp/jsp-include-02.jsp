<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jsp-include-02</title>
</head>
<body>
<h:outputText value="[A]"/>
<jsp:include page="jstl-import-02a.jsp"/>
<h:outputText value="[C]"/>
<jsp:include page="jstl-import-02b.jsp"/>
<h:outputText value="[E]"/>
</body>
</html>
</f:view>
