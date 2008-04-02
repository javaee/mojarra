<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Replace the ViewRoot</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ page import="javax.faces.context.FacesContext" %>
    <%@ page import="javax.servlet.http.HttpServlet" %>
  </head>

  <body>
    <h1>Replace the ViewRoot</h1>

<% 

FacesContext.getCurrentInstance().getApplication().addComponent("javax.faces.ViewRoot", "com.sun.faces.systest.model.ViewRootExtension");
((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().invalidate();

%>

<p><a name="examine" href="examineViewRoot.jsp">examine the replaced viewRoot.</a></p>
    <hr>
  </body>
</html>
