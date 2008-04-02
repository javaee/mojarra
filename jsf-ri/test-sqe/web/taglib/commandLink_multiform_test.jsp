<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
                                                                                
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
                                                                                
                                                                                
<html>
  <body>
     <f:view>
     <h:form id="form01">
       <h:commandLink id ="Link1" action="redirect">
          <h:outputText value="Link1"/>
       </h:commandLink>
       <h:commandLink id ="Link2" action="redirect">
          <h:outputText value="Link2"/>
       </h:commandLink>
    </h:form> 
    <h:form id="form02">
       <h:commandLink id ="Link3" action="redirect">
          <h:outputText value="Link3"/>
       </h:commandLink>
       <h:commandLink id ="Link4" action="forward">
          <h:outputText value="Link4"/>
       </h:commandLink>
   </h:form>
   </f:view>
  </body>
</html>
