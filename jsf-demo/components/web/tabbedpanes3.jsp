<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>
<%@ taglib uri="http://java.sun.com/jsf/html"   prefix="h" %>

    <h:panel_group>
      <h:output_text value="This is the third pane with the date set to:"/>
      <h:output_date dateStyle="FULL" modelReference="sessionScope.date"/>
    </h:panel_group>

