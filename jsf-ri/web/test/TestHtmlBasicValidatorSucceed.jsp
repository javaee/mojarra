<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<h1>TLV command_button, no lable or key</h1>
This page should Succeed.
<br>
<br>

<f:use_faces>

  <h:command_button label="hello" commandName="required"/>

  <h:command_button key="hello" commandName="required"/>

</f:use_faces>

</body>
</head>
</html>
