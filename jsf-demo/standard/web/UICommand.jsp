<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UICommand.jsp,v 1.7 2003/11/09 03:25:12 eburns Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UICommand</title>
  </head>

  <body>

    <h1>UICommand</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                  >

         <h:command_button id="standardRenderKitSubmit" 
             actionRef="model.defaultAction"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="command_link_row.jsp" %>

         </table>

         <h:command_button id="standardRenderKitSubmit1"
             actionRef="model.defaultAction"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:command_button>

       </h:form>

     </f:view>   


  </body>
</html>
