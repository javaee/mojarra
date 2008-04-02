<html>
<body>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<jsp:useBean id="myBean" class="com.sun.faces.systest.model.TestBean"
             scope="session"/>

<f:view>
    <h:form id="form">
        <h:selectOneRadio converter="javax.faces.Integer"
                          value="#{myBean.selectedValue}">
            <f:selectItems value="#{myBean.mySelectItems}"/>
        </h:selectOneRadio>
        <h:outputText value="Model Selection:"/>
        <h:outputText value="#{myBean.selectedValue}"/>
        <br>
        <h:commandButton id="nonImmediate" value="Submit immedate false"
                         action="success"/>
        <h:commandButton id="immediate" value="Submit immediate true"
                         immediate="true" action="success"/>
    </h:form>
    <h:messages/>
</f:view>

</body>
</html>
