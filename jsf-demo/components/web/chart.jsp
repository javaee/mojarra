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

<%-- $Id: chart.jsp,v 1.5 2004/04/01 20:55:09 rkitain Exp $ --%>



<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>
<html>
<head>
  <title>Chart Example</title>
</head>
<body bgcolor="white">

<h:form>
       
     <hr>
     <table> 
      <tr>
         <th><h:outputText value="Vertical Bar Chart with data specifed via JSP" /></th>
      </tr> 
      <tr> 
      <td>
      <d:chart width="300" height="300" type="bar" orientation="vertical" 
         title="Employee Number By Department" xlabel="Departments" ylabel="Employees">
         <d:chartItem itemLabel="Eng" itemValue="200" itemColor="red" />
         <d:chartItem itemLabel="Mktg" itemValue="400" itemColor="green" />
         <d:chartItem itemLabel="Sales" itemValue="250" itemColor="blue" />
         <d:chartItem itemLabel="R&D" itemValue="350" itemColor="orange" />
         <d:chartItem itemLabel="HR" itemValue="450" itemColor="cyan" />
      </d:chart> 
      </td>
      <td>
      <table>
      <tr>
         <th><h:outputText value="Horizontal Bar Chart with data specifed via JSP" /></th>
      </tr>
      <tr>
      <td>
      <d:chart width="300" height="300" type="bar" orientation="horizontal" 
         title="Employee Number By Department" xlabel="Employees" ylabel="Departments">
         <d:chartItem itemLabel="Eng" itemValue="200" itemColor="red" />
         <d:chartItem itemLabel="Mktg" itemValue="400" itemColor="green" />
         <d:chartItem itemLabel="Sales" itemValue="250" itemColor="blue" />
         <d:chartItem itemLabel="R&D" itemValue="350" itemColor="orange" />
         <d:chartItem itemLabel="HR" itemValue="450" itemColor="cyan" />
      </d:chart>
      </td>
      </tr>
      </table>
      <td>
      </tr> 
    </table>

     <table>
      <tr>
         <th><h:outputText value="Pie Chart with data specifed via JSP" /></th>
</h:form>
      </tr>
      <tr>
     <td>
     <d:chart width="400" height="200" type="pie" 
         title="Employee Number By Department">
         <d:chartItem itemLabel="Eng" itemValue="200" itemColor="red" />
         <d:chartItem itemLabel="Mktg" itemValue="400" itemColor="green" />
         <d:chartItem itemLabel="Sales" itemValue="600" itemColor="blue" />
         <d:chartItem itemLabel="R&D" itemValue="700" itemColor="orange" />
         <d:chartItem itemLabel="HR" itemValue="800" itemColor="cyan" />
     </d:chart> </td>
     </tr> </table>



<!--
  <hr>
  <a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

<h1>How to Use this Component</h1>

<p>This component produces a search engine style scroller that facilitates
   easy navigation over results that span across several pages.
</p>

<h2>JSP Attributes</h2>

<p>This component relies on the presence of a data grid to display the results
   in the form of a table. You can compare the
rendered source of this page, using the "View Source" feature of your
browser, with <a href="ShowSource.jsp?filename=/result-set.jsp">the JSP
source</A> for this page.</p>

<table border="1">

<tr>
<th>JSP Attribute Name
</th>
<th>What it Does
</th>
</tr>

<tr>

<td><code>navFacetOrientation</code>
</td>

<td>"NORTH", "SOUTH", "EAST", or "WEST".  This attribute tells where to
put the number that means "skip to page N in the result set" in relation
the facet.
</td>
</tr>

<tr>
<td><code>forValue</code>
</td>
<td>The data grid component for which this acts as a scroller.
</td>
</tr>

<tr>
<td><code>actionListener</code></td>
<td>Method binding reference to handle an action event generated as a result of 
    clicking on a link that points a particular page in the result-set.
</td>
</tr>
</table>

<h2>Facets</h2>

<p>You can define Facets for each of the following elements of the
result set component.</p>

<table border="1">
<tr>
<th>Facet Name
</th>
<th>What it Does
</th>
</tr>

<tr>
<td><code>next</code>
</td>

<td>If present, this facet is output as the "Next" widget.  If absent,
the word "Next" is used.
</td>
</tr>

<tr>
<td><code>previous</code>
</td>
<td>If present, this facet is output as the "Previous" widget.  If absent,
the word "Previous" is used.
</td>
</tr>


<tr>
<td><code>number</code>
</td>

<td>If present, this facet is output, leveraging the
<code>navFacetOrientation</code> attribute, to represent "skip to page N
in the result set".
</td>

</tr>

<tr>
<td><code>current</code>
</td>

<td>If present, this facet is output, leveraging the
<code>navFacetOrientation</code> attribute, to represent the "current
page" in the result set.
</td>

</tr>
</table>
-->
<hr>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.
</body>
</html>
</f:view>
