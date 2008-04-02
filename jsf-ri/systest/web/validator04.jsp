<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
</head>

<body>
<h1>Validators</h1>

<f:view>

    <h:form>

        literal required: <h:inputText id="textField" required="true"
                                       requiredMessage="Literal Message"/>

        <p/>

        expression required <h:inputText id="textField2" required="true"
                                         requiredMessage="#{test2.stringProperty}"/>

        <p/>

        literal converter <h:inputText id="textField3"
                                       value="#{test2.intProperty}"
                                       converterMessage="Converter Literal"/>

        <p/>

        expression converter <h:inputText id="textField4"
                                          value="#{test2.intProperty}"
                                          converterMessage="#{test2.converterMessage}"/>

        <p/>

        literal validator <h:inputText id="textField5"
                                       value="#{test2.intProperty}"
                                       validatorMessage="Validator Literal">
        <f:validateLongRange minimum="1" maximum="10"/>
    </h:inputText>

        <p/>

        expression validator <h:inputText id="textField6"
                                          value="#{test2.intProperty}"
                                          validatorMessage="#{test2.validatorMessage}">
        <f:validateLongRange minimum="1" maximum="10"/>
    </h:inputText>

        <p/>


        <h:messages/>

        <h:commandButton value="submit"/>

    </h:form>

</f:view>

<hr>
</body>
</html>
