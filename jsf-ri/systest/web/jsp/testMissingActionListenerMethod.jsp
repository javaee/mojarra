<html>
<title>Missing ActionListener Method Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<h1>commandButton w/missing ActionListener method</h1>
This page should Fail.
<br>
<br>
<f:view>
  <p>This command button has a missing ActionListener method</p>
  <h:form id="testForm">
    <h:commandButton id="testButton" actionListener="#{methodRef.missingMethod}" value="Test Button"/>
  </h:form>
</f:view>
</body>
</head>
</html>
