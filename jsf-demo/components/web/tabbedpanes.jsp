<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsf/core"   prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"   prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<%

    // Construct a preconfigured Date in session scope
    Date date = (Date)
      pageContext.getAttribute("date", PageContext.SESSION_SCOPE);
    if (date == null) {
      date = new Date();
      pageContext.setAttribute("date", date,
                               PageContext.SESSION_SCOPE);
    }

%>


<f:use_faces>
<html>
<head>
  <title>Demonstration Components - Tabbed Panes</title>
  <d:stylesheet path="/stylesheet.css"/>
</head>
<body bgcolor="white">

<h:form formName="tabbedForm" bundle="demoBundle">

Powered by Faces components:

<d:pane_tabbed id="tabcontrol"
        paneClass="tabbed-pane"
     contentClass="tabbed-content"
    selectedClass="tabbed-selected"
  unselectedClass="tabbed-unselected">

  <d:pane_tab id="first">

    <f:facet name="label">
      <d:pane_tablabel label="T a b 1" commandName="first" />
    </f:facet>

    <h:panel_group>
      <h:output_text value="This is the first pane with the date set to:"/>
      <h:output_date dateStyle="MEDIUM" modelReference="date"/>
    </h:panel_group>

  </d:pane_tab>

  <d:pane_tab id="second" selected="true">

    <f:facet name="label">
      <d:pane_tablabel image="images/duke.gif" commandName="second"/>
    </f:facet>

    <h:panel_group>
      <h:output_text value="Hi folks!  My name is 'Duke'.  Here's a sample of some of the components you can build:<p>"/>
    </h:panel_group>
    <h:panel_group>
      <h:command_button label="button" commandName="button"/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      <h:selectboolean_checkbox checked="true" alt="checkbox"/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      <h:selectone_radio layout="PAGE_DIRECTION" border="1">
        <h:selectitem itemValue="nextDay" itemLabel="Next Day" selected="true"/>
        <h:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
        <h:selectitem itemValue="nextMonth" itemLabel="Next Month" />
      </h:selectone_radio>
      <h:selectone_listbox id="appleQuantity" title="Select Quantity"
        accesskey="N" tabindex="20" >
        <h:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
        <h:selectitem  itemValue="1" itemLabel="1" title="One" />
        <h:selectitem  itemValue="2" itemLabel="2" title="Two" />
        <h:selectitem  itemValue="3" itemLabel="3" title="Three" />
        <h:selectitem  itemValue="4" itemLabel="4" title="Four" 
           selected="true"/>
      </h:selectone_listbox>
    </h:panel_group>

  </d:pane_tab>

  <d:pane_tab id="third">

    <f:facet name="label">
      <d:pane_tablabel label="T a b 3" commandName="third"/>
    </f:facet>

    <h:panel_group>
      <jsp:include page="tabbedpanes3.jsp" flush="false"/>
    </h:panel_group>

  </d:pane_tab>

</d:pane_tabbed>

<hr>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

</body>
</html>

</h:form>
</f:use_faces>
