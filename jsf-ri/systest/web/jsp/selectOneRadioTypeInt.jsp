<html>
<body>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<jsp:useBean id="myBean" class="com.sun.faces.systest.model.TestBean"
             scope="session"/>

<f:view>
    <h:form id="form">
        <h:selectOneRadio value="#{myBean.int}">
            <f:selectItem itemLabel="first" itemValue="1"/>
            <f:selectItem itemLabel="second" itemValue="2"/>
            <f:selectItem itemLabel="three" itemValue="3"/>
        </h:selectOneRadio>
        <h:outputText value="Model Selection:"/>
        <h:outputText value="#{myBean.int}"/>
        <br>
        <h:commandButton id="nonImmediate" value="Submit immedate false"/>
        <h:commandButton id="immediate" value="Submit immediate true"
                         immediate="true"/>
    </h:form>
    <h:messages/>
</f:view>

</body>
</html>
