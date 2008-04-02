<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>


<html>
<body>
<f:view>
    <h:form id="form01">
        <h:commandLink id="Link1" action="redirect">
            <h:outputText value="Link1"/>
            <f:param id="hlParam1" name="param1" value="value1"/>
            <f:param id="hlParam2" name="param2" value="value2"/>
        </h:commandLink>
        <h:commandLink id="Link2" action="redirect">
            <h:outputText value="Link2"/>
            <f:param id="hlParam3" name="param1" value="value1"/>
            <f:param id="hlParam4" name="param2" value="value2"/>
        </h:commandLink>
    </h:form>
    <h:form id="form02">
        <h:commandLink id="Link3" action="redirect">
            <h:outputText value="Link3"/>
            <f:param id="hlParam1" name="param3" value="value3"/>
            <f:param id="hlParam2" name="param4" value="value4"/>
        </h:commandLink>
        <h:commandLink id="Link4" action="forward">
            <h:outputText value="Link4"/>
        </h:commandLink>
    </h:form>
</f:view>
</body>
</html>
