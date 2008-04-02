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
  <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/result-set.css" %>'>
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
                  navFacetOrientation="SOUTH"
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
                      <h:output_text value="Go<br>Next"/>
                  </f:facet>

	          <f:facet name="previous">
                      <h:output_text value="Go<br>Previous"/>
                  </f:facet>

	          <f:facet name="number">
                      <h:output_text value="X"/>
                  </f:facet>

	          <f:facet name="current">
                      <h:output_text value="_"/>
                  </f:facet>


                  <!-- List Data -->

                  <h:panel_data var="customer"
                       modelReference="list">
                    <h:output_text id="accountId1"
                       modelReference="customer.accountId"/>
                    <h:output_text id="name1"
                       modelReference="customer.name"/>
                    <h:output_text id="symbol1"
                       modelReference="customer.symbol"/>
                    <h:output_text id="totalSales1"
                       modelReference="customer.totalSales"/>
                  </h:panel_data>

                 <!-- d:panel_list may only have one child: d:panel_data -->

                 </d:panel_resultset>  

<hr>
</h:form>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

</body>
</html>
</f:use_faces>
