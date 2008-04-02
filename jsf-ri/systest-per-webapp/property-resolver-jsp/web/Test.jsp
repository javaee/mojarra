
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
  <head><title>Simple jsp page</title></head>
  <body>
  <%
      pageContext.setAttribute("resolved", "resolved!", PageContext.REQUEST_SCOPE);
  %>
  <p>${requestScope.resolved}</p>
  </body>
</html>