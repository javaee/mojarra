<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<h1>TLV command_button, no lable or key</h1>
This page should FAIL.
<br>
<br>

<f:use_faces>

  <p>This must have either a label or key attribute.</p>
  <h:command_button commandName="required">
    <h:output_text value="hello"/>
  </h:command_button>

</f:use_faces>

</body>
</head>
</html>
