<!--
 Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% 
  Date sampleDate = (new SimpleDateFormat("yyyy/MM/dd")).parse("2003/12/25");
  pageContext.setAttribute("sampleDate", sampleDate);
%>

<f:view>

<f:loadBundle basename="demo.model.Resources" var="rb"/>

<html>
<head>
  <title><fmt:message key="calendar.title"/></title>
</head>

<body>

<h1><fmt:message key="calendar.title"/></h1>

<h:form>

  <p>
  <h:command_link 
    id="en" action="#{CalendarBean.selectLocaleEN}" immediate="true">
    <h:output_text value="English"/>
  </h:command_link>
  &nbsp;&nbsp;
  <h:command_link 
    id="fr" action="#{CalendarBean.selectLocaleFR}" immediate="true">
    <h:output_text value="Francais"/>
  </h:command_link>
  </p>

  <table>
    <tr>
      <td>
        <fmt:message key="calendar.enterDate"/> 
        <fmt:message key="calendar.sampleDate"> 
          <fmt:param value="${sampleDate}"/>
          <fmt:param value="${sampleDate}"/>
        </fmt:message>
      </td>
    </tr>
    <tr>
      <td>
        <d:calendar value="#{CalendarBean.date}" required="true"/>
      </td>
    </tr>
      <td>
	<h:command_button id="submit" action="#{CalendarBean.process}" value='#{rb["calendar.submit"]}' type="SUBMIT"/>
      </td>
    </tr>
    <tr>
      <td>
	<h:messages id="errors" for="date"/>
        <h:messages globalOnly="true"/>
      </td>
    </tr>
  </table>
</h:form>

<hr>
<a href='<%= request.getContextPath() + "/index.jsp" %>'><fmt:message key="backToHomePage"/></a>.
<hr>

<h1>How to Use this Component</h1>

<p>The &lt;d:calendar&gt; is essentially
a UIInput component specialized for dates and augmented
with a graphical representation of a calendar to ease the selection of
a specific date.<p>

<h2>JSP Attributes</h2>

<table border="1">

<tr>
  <th>JSP Attribute Name</th>
  <th>What it Does</th>
</tr>

<tr>
  <td><code>value</code></td>
  <td>
    <p>Value reference expression to link this <code>Calendar</code> component
    to a property in your backing file bean.</p>
  </td>
</tr>

<tr>
  <td><code>required</code></td>
  <td>
    <p>Tells whether a value is required or not in the textbox</p>
  </td>
</tr>

</table>

<hr>





</body>
</html>

</f:view>
