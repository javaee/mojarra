<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>JavaServer Faces 1.0 Standard RenderKit</title>
  </head>

  <body>

    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <h1>JavaServer Faces 1.0 Standard RenderKit</h1>
    <h3>$Id: StandardRenderKit.jsp,v 1.1 2002/08/18 01:51:01 eburns Exp $</h3>

    <p>The elements in this page should follow the
    StandardRenderKit.html page in the jsf-api.</p>


     <fmt:setBundle basename="basic.Resources" scope="session" 
                    var="basicBundle"/>

     <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />

     <faces:usefaces>  

       <p>Form is rendered after this.</p>
     
       <faces:form id="standardRenderKitForm" 
                   formName="standardRenderKitForm">

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="command_button_row.jsp" %>

           <%@ include file="command_hyperlink_row.jsp" %>

           <%@ include file="graphic_image_row.jsp" %>

           <%@ include file="input_date_row.jsp" %>

         </table>

       </faces:form>

     </faces:usefaces>   

  </body>
</html>
