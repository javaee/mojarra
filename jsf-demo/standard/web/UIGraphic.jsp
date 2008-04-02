<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIGraphic.jsp,v 1.4 2003/11/09 03:25:12 eburns Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UIGraphic</title>
  </head>

  <body>

    <h1>UIGraphic</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>
     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session">
       <jsp:setProperty name="LoginBean" property="imagePath" 
                        value="duke.gif"/> 
     </jsp:useBean>

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" >

         <h:command_button id="standardRenderKitSubmit" action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="graphic_image_row.jsp" %>

         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:command_button>

       </h:form>

     </f:view>   


  </body>
</html>
