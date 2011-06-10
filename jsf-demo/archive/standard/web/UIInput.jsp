<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UIInput</title>
  </head>

  <body>

    <h1>UIInput</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" >

         <h:commandButton id="standardRenderKitSubmit" 
             action="#{model.postbackAction}"
             value="#{standardBundle.standardRenderKitPostbackLabel}">
         </h:commandButton>

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
		<h:messages id="globalErrors" />
</td>

</tr>

         </table>

         <h:commandButton id="standardRenderKitSubmit1" 
             action="#{model.postbackAction}"
             value="#{standardBundle.standardRenderKitPostbackLabel}">
         </h:commandButton>

       </h:form>

     </f:view>   


  </body>
</html>
