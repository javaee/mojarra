<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UISelectOne.jsp,v 1.1 2003/07/17 02:58:44 rlubke Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UISelectOne</title>
  </head>

  <body>

    <h1>UISelectOne</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <fmt:setBundle basename="standard.Resources" scope="session"
                    var="standardBundle"/>

     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session" />
     <f:use_faces>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                   formName="standardRenderKitForm">

         <h:command_button id="standardRenderKitSubmit" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="selectone_listbox_row.jsp" %>

           <%@ include file="selectone_menu_row.jsp" %>

           <%@ include file="selectone_radio_row.jsp" %>


         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

       </h:form>

     </f:use_faces>   


  </body>
</html>
