<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
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

<% 
  Date sampleDate = (new SimpleDateFormat("yyyy/MM/dd")).parse("2003/12/25");
  pageContext.setAttribute("sampleDate", sampleDate, PageContext.REQUEST_SCOPE);
%>

<f:view>

<f:loadBundle basename="demo.model.Resources" var="rb"/>

<html>
<head>
  <title><h:outputText value="#{rb['calendar.title']}" escape="false"/></title>
  <link rel="STYLESHEET" type="text/css"
       href='<%= request.getContextPath() + "/images/calendar.css" %>'/>
  <script language="JavaScript" type="text/javascript"
        src='<%= request.getContextPath() + "/images/simplecalendar.js" %>'></script>

</head>

<body>

<h1><h:outputText value="#{rb['calendar.title']}" escape="false"/></h1>

<h:form>

  <p>
  <h:commandLink 
    id="en" action="#{CalendarBean.selectLocaleEN}" immediate="true">
    <h:outputText value="English"/>
  </h:commandLink>
  &nbsp;&nbsp;
  <h:commandLink 
    id="fr" action="#{CalendarBean.selectLocaleFR}" immediate="true">
    <h:outputText value="Francais"/>
  </h:commandLink>
  </p>

  <table>
    <tr>
      <td>
        <h:outputText value="#{rb['calendar.enterDate']}"/> 
        <h:outputFormat value="#{rb['calendar.sampleDate']}" escape="false"> 
          <f:param value="#{sampleDate}"/>
          <f:param value="#{sampleDate}"/>
        </h:outputFormat>
      </td>
    </tr>
    <tr>
      <td>
        <d:calendar id="calendar"
               binding="#{CalendarBean.calendar}"
              required="true"
                 value="#{CalendarBean.date}"/>
      </td>
    </tr>
      <td>
	<h:commandButton id="submit" action="#{CalendarBean.process}"
                      value="#{rb['calendar.submit']}" type="SUBMIT"/>
      </td>
    </tr>
    <tr>
      <td>
	<h:message id="errors" for="date"/>
        <h:messages globalOnly="true"/>
      </td>
    </tr>
  </table>
</h:form>

<hr>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>
<h:outputText value="#{rb['backToHomePage']}"/></a>.
<hr>

<h1>How to Use this Component</h1>

<p>The &lt;d:calendar&gt; component is a UIInput component specialized for
dates and augmented with a graphical representation of a calendar to ease
the selection of a specific date.<p>


<h2>JSP Attributes</h2>

<table border="1">

<tr>
  <th>JSP Attribute Name</th>
  <th>What it Does</th>
</tr>

<tr>
  <td><code>binding</code></td>
  <td>
    <p>Value reference expression to link this component to a backing bean.</p>
  </td>
</tr>

<tr>
  <td><code>dateStyle</code></td>
  <td>
    <p>Date style to be used for rendering the date in the text box.
    Default value is "short".</p>
  </td>
</tr>

<tr>
  <td><code>id</code></td>
  <td>
    <p>Component identifier of this component.</p>
  </td>
</tr>

<tr>
  <td><code>immediate</code></td>
  <td>
    <p>Flag indicating that conversion and validation (if any) should happen
    immediately in Apply Request Values phase, rather than waiting for
    Process Validations phase.  Default value is <code>false</code>.</p>
  </td>
</tr>

<tr>
  <td><code>maxlength</code></td>
  <td>
    <p>Maximum length of the text input field.  Default value is 8.</p>
  </td>
</tr>

<tr>
  <td><code>pattern</code></td>
  <td>
    <p>Text formatting pattern for the date in the text field.  See
    Javadocs for java.text.SimpleDateFormat for legal values.  If not
    specified, the value of the <code>dateStyle</code> attribute controls
    the text field format.</p>
  </td>
</tr>

<tr>
  <td><code>required</code></td>
  <td>
    <p>Flag indicating that this input field is required.  If set to
    <code>true</code>, a validation error will occur if the text field
    is left empty.  Default value is <code>false</code>.</p>
  </td>
</tr>

<tr>
  <td><code>style</code></td>
  <td>
    <p>CSS style(s) to use when rendering the text input field.</p>
  </td>
</tr>

<tr>
  <td><code>styleClass</code></td>
  <td>
    <p>CSS style class(es) to use when rendering the text input field.</p>
  </td>
</tr>

<tr>
  <td><code>value</code></td>
  <td>
    <p>Value binding expression pointing at a model data property to be
    updated when this input field is processed.</p>
  </td>
</tr>

</table>

<hr>

</body>
</html>

</f:view>
