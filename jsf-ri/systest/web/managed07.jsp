<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Property that violates scope rules</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Property that violates scope rules</h1>

<% try { %> 
<f:view>

  <h:form>

    
    <h:outputText value="#{cyclic1.bean.stringProperty}" />
   
  </h:form>

</f:view>
<% 
  } catch (Exception fe) {
       fe.printStackTrace();
       if (!(fe instanceof javax.faces.FacesException)) {
           throw fe;
       }
   }
%>
    <hr>
  </body>
</html>
