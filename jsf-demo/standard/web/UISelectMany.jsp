<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UISelectMany.jsp,v 1.3 2003/10/19 14:54:07 eburns Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UISelectMany</title>
  </head>

  <body>

    <h1>UISelectMany</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <fmt:setBundle basename="standard.Resources" scope="session"
                    var="standardBundle"/>

     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session" />
     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                  >

         <h:command_button id="standardRenderKitSubmit" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="selectmany_menu_row.jsp" %>

           <%@ include file="selectmany_listbox_row.jsp" %>

           <%@ include file="selectmany_checkbox_row.jsp" %>

         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

       </h:form>

     </f:view>   


  </body>
</html>
