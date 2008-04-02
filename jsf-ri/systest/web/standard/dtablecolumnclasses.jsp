<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>dtablecolumnclasses.jsp</title>
    <style type="text/css">
        .b1 {
            background-color: red;
        }

        .b2 {
            background-color: green;
        }

        .b3 {
            background-color: blue;
        }

        .b4 {
            background-color: burlywood;
        }
    </style>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>


    <%
        List<String[]> list = new ArrayList<String[]>(1);
        list.add(new String[]{"c1", "c2", "c3"});
        request.setAttribute("list", list);
    %>
</head>
<body>
<f:view>
    <h:dataTable value="#{requestScope.list}"
                 var="row"
                 columnClasses="b1,b2">
        <h:column>
            <h:outputText value="#{row[0]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[1]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[2]}"/>
        </h:column>
    </h:dataTable>
    <h:dataTable value="#{requestScope.list}"
                 var="row"
                 columnClasses="b1,b2,b3,b4">
        <h:column>
            <h:outputText value="#{row[0]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[1]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[2]}"/>
        </h:column>
    </h:dataTable>
    <h:dataTable value="#{requestScope.list}"
                 var="row"
                 columnClasses="b1,b2,b3">
        <h:column>
            <h:outputText value="#{row[0]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[1]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[2]}"/>
        </h:column>
    </h:dataTable>
    <h:dataTable value="#{requestScope.list}"
                 var="row"
                 columnClasses="b1">
        <h:column>
            <h:outputText value="#{row[0]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[1]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[2]}"/>
        </h:column>
    </h:dataTable>
    <h:dataTable value="#{requestScope.list}"
                 var="row">
        <h:column>
            <h:outputText value="#{row[0]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[1]}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row[2]}"/>
        </h:column>
    </h:dataTable>
</f:view>
</body>
</html>


