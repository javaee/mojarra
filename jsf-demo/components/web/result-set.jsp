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

      <%@ page import="demo.model.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core"   prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"   prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<%

  // Construct a preconfigured customer list in session scope
  List list = (List)
    pageContext.getAttribute("list", PageContext.SESSION_SCOPE);
  if (list == null) {
    list = new ArrayList();
    list.add(new CustomerBean("123456", "Alpha Beta Company", "ABC", 1234.56));
    list.add(new CustomerBean("445566", "General Services, Ltd.", "GS", 33.33));
    list.add(new CustomerBean("654321", "Summa Cum Laude, Inc.", "SCL", 76543.21));
    list.add(new CustomerBean("333333", "Yabba Dabba Doo", "YDD",  333.33));
    for (int i = 0; i < 1000; i++) {
      list.add(new CustomerBean(Integer.toString(i), 
                                "name_" + Integer.toString(i),
                                "symbol_" + Integer.toString(i),
                                i));
    }
      
    pageContext.setAttribute("list", list,
                             PageContext.SESSION_SCOPE);
  }

%>

<f:use_faces>
<html>
<head>
  <title>Result Set Example</title>
  <d:stylesheet path="/result-set.css"/>
</head>
<body bgcolor="white">

<h:form formName="resultsForm" bundle="demoBundle">  

Rendered via Faces components:

                 <d:panel_resultset
                 columnClasses="list-column-center,list-column-left,
                 list-column-center, list-column-right"
                 headerClass="list-header"
                  panelClass="list-background"
                  rowClasses="list-row-even,list-row-odd"
                  navFacetOrientation="NORTH"
                  rowsPerPage="20">

                  <f:facet name="header">
		    <h:panel_group>
		      <h:output_text value="Account Id"/>
		      <h:output_text value="Customer Name"/>
		      <h:output_text value="Symbol"/>
		      <h:output_text value="Total Sales"/>
		    </h:panel_group>
                  </f:facet>

	          <f:facet name="next">
                    <h:panel_group>
                      <h:output_text value="Next<br>"/>
                      <h:graphic_image url="/images/arrow-right.gif" />
                    </h:panel_group>
                  </f:facet>

	          <f:facet name="previous">
                    <h:panel_group>
                      <h:output_text value="Previous<br>"/>
                      <h:graphic_image url="/images/arrow-left.gif" />
                    </h:panel_group>
                  </f:facet>

	          <f:facet name="number">
                     <!-- You can put a panel here if you like -->
                  </f:facet>

	          <f:facet name="current">
                    <h:panel_group>
                      <h:graphic_image url="/images/duke.gif" />
                    </h:panel_group>
                  </f:facet>


                  <!-- List Data -->

                  <h:panel_data var="customer"
                       valueRef="list">
                    <h:output_text id="accountId1"
                       valueRef="customer.accountId"/>
                    <h:output_text id="name1"
                       valueRef="customer.name"/>
                    <h:output_text id="symbol1"
                       valueRef="customer.symbol"/>
                    <h:output_text id="totalSales1"
                       valueRef="customer.totalSales"/>
                  </h:panel_data>

                 <!-- d:panel_list may only have one child: d:panel_data -->

                 </d:panel_resultset>  

<hr>
</h:form>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.


<h1>How to Use this Component</h1>

<p>This component produces a search-engine style result set scroller
given a back end model object of type <code>java.util.List</code>.
</p>

<h2>JSP Attributes</h2>

<p>This component allows the user to define CSS classes via JSP
attributes that are output in the rendered markup.  This makes it
possible to produce highly customizable output.  You can compare the
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

<td><code>columnClasses</code>
</td>

<td>A comma separated list of style classes.  The number of entries in
the list must match the number of columns in your data.  The first entry
in the list is output with the first column, the second with the second
column, and so on.
</td>

</tr>

<tr>

<td><code>headerClass</code>
</td>

<td>This class is output with each column header.
</td>

</tr>

<tr>

<td><code>panelClass</code>
</td>

<td>This class is output as the class on the table enclosing the results.
</td>

</tr>

<tr>

<td><code>rowClasses</code>
</td>

<td>If this attribute has a value, it must by a comma separated list
with exactly two (2) entries.  The first entry is output as the class
for even rows, the second for odd rows.
</td>

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

<td><code>rowsPerPage</code>
</td>

<td>Controls the number of rows shown in a page of results.
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

<td><code>header</code>
</td>

<td>If present, this should be a <code>panel_group</code> that contains
a number of children equal to the number of columns in your data.
</td>

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

<h2>Model Data</h2>

<p>The <code>panel_resultset</code> tag must have a single child
component, a <code>panel_data</code> tag, whose model reference points
to a <code>java.util.List</code> reference.  The <code>panel_data</code>
tag must have a number of child components equal to the number of
columns you wish to display in your data.  See <a
href="ShowSource.jsp?filename=/result-set.jsp">the JSP source</A> for an
example of how to do this.</p>

<hr>

<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.



</body>
</html>
</f:use_faces>
