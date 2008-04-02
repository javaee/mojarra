<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>output_errors01.jsp</title>

<%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.Message"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.MessageResources"
%><%@ page import="javax.faces.context.FacesContext"
%><%

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/output_errors01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // get the API MessageResources
  MessageResources messageResources = 
               appl.getMessageResources(MessageResources.FACES_API_MESSAGES);
  if (messageResources == null) {
    out.println("/output_errors01.jsp FAILED - No MessageResources returned");
    return;
  }

  // get a message
  Message message = messageResources.getMessage(facesContext, 
        "javax.faces.component.UIInput.REQUIRED");
  if (message == null) {
    out.println("/output_errors01.jsp FAILED - No message returned");
    return;
  }

  facesContext.addMessage(null, message);

  message = messageResources.getMessage(facesContext,
         "javax.faces.validator.LongRangeValidator.LIMIT");
  if (message == null) {
    out.println("/output_errors01.jsp FAILED - No message returned");
    return;
  }

  facesContext.addMessage(null, message);

  message = messageResources.getMessage(facesContext,
         "javax.faces.validator.StringRangeValidator.TYPE");
  if (message == null) {
    out.println("/output_errors01.jsp FAILED - No message returned");
    return;
  }

  facesContext.addMessage(null, message);

%>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<STYLE TYPE="text/css" MEDIA=screen>
<!--
.errors { 
  background-color: #7171A5;
  border: 5px outset #71A5A5;
  border-collapse: collapse;
  font-family: sans-serif;
  font-size: 14pt;
  padding: 10px;
}
-->
</STYLE>
  </head>

  <body>

<f:view>


<h:output_errors styleClass="errors"/> 

</f:view>



  </body>
</html>
