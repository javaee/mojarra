<%@ page contentType="text/html"
      %>
<%@ page import="javax.faces.context.FacesContext"
      %>
<%
    FacesContext fc = FacesContext.getCurrentInstance();
    fc.getApplication().setDefaultRenderKitId("CUSTOM");
%>
