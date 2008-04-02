<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIOutput.jsp,v 1.8 2004/02/05 16:25:19 rlubke Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UIOutput</title>
  </head>

  <body>

    <h1>UIOutput</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>

     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session" />

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                  >

         <h:commandButton id="standardRenderKitSubmit" 
             action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:commandButton>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="output_date_row.jsp" %>

           <%@ include file="output_datetime_row.jsp" %>

           <%@ include file="output_message_row.jsp" %>

           <%@ include file="output_number_row.jsp" %>

           <%@ include file="output_text_row.jsp" %>

           <%@ include file="output_time_row.jsp" %>

<tr>
<td><b>Errors:</b>
</td>

<td>
		<h:messages id="globalErrors" />
</td>

</tr>

         </table>

         <h:commandButton id="standardRenderKitSubmit1" 
             action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:commandButton>

       </h:form>

     </f:view>   


  </body>
</html>
