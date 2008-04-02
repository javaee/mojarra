<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
</head>
<body>

<h1>TLV c:if with JSF id</h1>
This page should succeed.
<br>
<br>

<% request.setAttribute("one", new String("one")); %>

<f:use_faces>

  <c:if test="${one == 'one'}">
    <h:output_text id="has_id" value="HAS ID" key="has_id"/>
  </c:if>

</f:use_faces>

</body>
</head>
</html>
