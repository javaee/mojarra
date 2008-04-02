<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIInput.jsp,v 1.6 2003/11/11 01:22:47 eburns Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UIInput</title>
  </head>

  <body>

    <h1>UIInput</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" >

         <h:command_button id="standardRenderKitSubmit" 
             actionRef="model.postbackAction"
             value="#{standardBundle.standardRenderKitPostbackLabel}">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="input_date_row.jsp" %>

           <%@ include file="input_text_row.jsp" %>

           <%@ include file="input_secret_row.jsp" %>

           <%@ include file="input_textarea_row.jsp" %>

           <%@ include file="input_datetime_row.jsp" %>

           <%@ include file="input_time_row.jsp" %>
  
           <%@ include file="input_number_row.jsp" %>

           <%@ include file="input_hidden_row.jsp" %>

<tr>
<td><b>Errors:</b>
</td>

<td>
		<h:messages id="globalErrors" for="" />
</td>

</tr>

         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             actionRef="model.postbackAction"
             value="#{standardBundle.standardRenderKitPostbackLabel}">
         </h:command_button>

       </h:form>

     </f:view>   


  </body>
</html>
