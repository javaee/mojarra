<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@page import="javax.faces.component.*,javax.faces.context.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ProcessSaveRestoreStateTest</title>
    </head>
    <body>
    <%
       UIComponent comp1 = new UIOutput();
       UIComponent child11 = new UIInput();
       child11.setTransient(true);
       UIComponent child12 = new UIInput();
       UIComponent child111 = new UIInput();
       UIComponent child121 = new UIInput();
       comp1.getChildren().add(child11); 
       comp1.getChildren().add(child12); 
       child11.getChildren().add(child111);
       child12.getChildren().add(child121);
       Object state = comp1.processSaveState(FacesContext.getCurrentInstance());
       comp1.processRestoreState(FacesContext.getCurrentInstance(), state);
    %>
    PASSED
    </body>
</html>
