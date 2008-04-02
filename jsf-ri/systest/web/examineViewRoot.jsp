<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Show that createView uses overridden ViewRoot</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
    <%@ page import="javax.faces.context.FacesContext" %>
  </head>

  <body>
    <h1>Show that createView uses overridden ViewRoot</h1>

<% 
FacesContext context = FacesContext.getCurrentInstance();

context.getExternalContext().getRequestMap().put("root", context.getViewRoot().getClass().getName());
FacesContext.getCurrentInstance().getApplication().addComponent("javax.faces.ViewRoot", "javax.faces.component.UIViewRoot");
%>

<f:view>

<p>Replaced ViewRoot is <h:outputText value="#{requestScope.root}" /></p>

<p><a name="replace" href="replaceViewRoot.jsp">Go back to replace</a></p>

</f:view>

    <hr>
  </body>
</html>
