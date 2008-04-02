<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Test prependId feature</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>Test prependId feature</h1>

<h2>Literal ids with prependId literal</h2>

<f:view>

    <h:form id="form1" prependId="false">

        <h:outputText id="case1prependIdFalse" value="prependIdFalse"/>

    </h:form>

    <h:form id="form2" prependId="true">

        <h:outputText id="case1prependIdTrue" value="prependIdTrue"/>

    </h:form>

    <h:form id="form3">

        <h:outputText id="case1prependIdUnspecified"
                      value="prependIdUnspecified"/>

    </h:form>


    <h2>Literal ids with prependId from expression</h2>

    <h:form id="form4" prependId="#{prependIdBean.booleanProperty2}">

        <h:outputText id="case2prependIdFalse" value="prependIdFalse"/>

    </h:form>

    <h:form id="form5" prependId="#{prependIdBean.booleanProperty}">

        <h:outputText id="case2prependIdTrue" value="prependIdTrue"/>

    </h:form>

    <h:form id="form6">

        <h:outputText id="case2prependIdUnspecified"
                      value="prependIdUnspecified"/>

    </h:form>


    <h2>Auto-generated ids with prependId literal</h2>

    <h:form prependId="false">

        <h:inputText value="prependIdFalse"/>

    </h:form>

    <h:form prependId="true">

        <h:inputText value="prependIdTrue"/>

    </h:form>

    <h:form>

        <h:inputText value="prependIdUnspecified"/>

    </h:form>

    <h2>Auto-generated ids with prependId from expression</h2>

    <h:form prependId="#{prependIdBean.booleanProperty2}">

        <h:inputText value="prependIdFalse"/>

    </h:form>

    <h:form prependId="#{prependIdBean.booleanProperty}">

        <h:inputText value="prependIdTrue"/>

    </h:form>

    <h:form>

        <h:inputText value="prependIdUnspecified"/>

    </h:form>

</f:view>

<hr>
</body>
</html>
