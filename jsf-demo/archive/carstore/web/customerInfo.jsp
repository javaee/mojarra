<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

--%>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <title>Customer Details</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/stylesheet.css">

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="https://javaserverfaces.dev.java.net/demo/carstore"
               prefix="cs" %>
</head>

<body>

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>
<h:form>

<h:panelGrid id="mainPanel" columns="1" footerClass="subtitle"
             headerClass="subtitlebig" styleClass="medium"
             columnClasses="medium">

<h:graphicImage url="/images/cardemo.jpg"/>

<h:outputText value="#{bundle.customerTitle}"/>

<h:panelGrid id="subPanel" columns="3" footerClass="medium"
             headerClass="subtitlebig" styleClass="medium"
             columnClasses="medium">

<h:outputText value="#{bundle.titleLabel}"/>
<h:selectOneMenu id="title" value="#{customer.currentTitle}">
    <f:selectItems value="#{customer.titleOptions}"/>
</h:selectOneMenu>
<h:outputText value=""/>

<h:outputText value="#{bundle.firstLabel}"/>
<h:inputText id="firstName" value="#{customer.firstName}" required="true">
    <f:valueChangeListener type="carstore.FirstNameChanged"/>
</h:inputText>
<h:message styleClass="validationMessage" for="firstName"/>

<h:outputText value="#{bundle.middleLabel}"/>
<h:inputText id="middleInitial" size="1" maxlength="1"
             value="#{customer.middleInitial}">
</h:inputText>
<h:message styleClass="validationMessage" for="middleInitial"/>

<h:outputText value="#{bundle.lastLabel}"/>
<h:inputText value="#{customer.lastName}"/>
<h:outputText value=""/>

<h:outputText value="#{bundle.mailingLabel}"/>
<h:inputText value="#{customer.mailingAddress}"/>
<h:outputText value=""/>

<h:outputText value="#{bundle.cityLabel}"/>
<h:inputText value="#{customer.city}"/>
<h:outputText value=""/>

<h:outputText value="#{bundle.stateLabel}"/>
<h:selectOneMenu value="#{customer.state}">

    <f:selectItem itemValue="AL" itemLabel="AL"/>
    <f:selectItem itemValue="AK" itemLabel="AK"/>
    <f:selectItem itemValue="AZ" itemLabel="AZ"/>
    <f:selectItem itemValue="AR" itemLabel="AR"/>
    <f:selectItem itemValue="CA" itemLabel="CA"/>
    <f:selectItem itemValue="CO" itemLabel="CO"/>
    <f:selectItem itemValue="CT" itemLabel="CT"/>
    <f:selectItem itemValue="DE" itemLabel="DE"/>
    <f:selectItem itemValue="FL" itemLabel="FL"/>
    <f:selectItem itemValue="GA" itemLabel="GA"/>

    <f:selectItem itemValue="HI" itemLabel="HI"/>
    <f:selectItem itemValue="ID" itemLabel="ID"/>
    <f:selectItem itemValue="IL" itemLabel="IL"/>
    <f:selectItem itemValue="IN" itemLabel="IN"/>
    <f:selectItem itemValue="IA" itemLabel="IA"/>
    <f:selectItem itemValue="KS" itemLabel="KS"/>
    <f:selectItem itemValue="KY" itemLabel="KY"/>
    <f:selectItem itemValue="LA" itemLabel="LA"/>
    <f:selectItem itemValue="ME" itemLabel="ME"/>
    <f:selectItem itemValue="MD" itemLabel="MD"/>

    <f:selectItem itemValue="MA" itemLabel="MA"/>
    <f:selectItem itemValue="MI" itemLabel="MI"/>
    <f:selectItem itemValue="MN" itemLabel="MN"/>
    <f:selectItem itemValue="MO" itemLabel="MO"/>
    <f:selectItem itemValue="MT" itemLabel="MT"/>
    <f:selectItem itemValue="NE" itemLabel="NE"/>
    <f:selectItem itemValue="NV" itemLabel="NV"/>
    <f:selectItem itemValue="NH" itemLabel="NH"/>
    <f:selectItem itemValue="NJ" itemLabel="NJ"/>
    <f:selectItem itemValue="NM" itemLabel="NM"/>

    <f:selectItem itemValue="MY" itemLabel="MY"/>
    <f:selectItem itemValue="NC" itemLabel="NC"/>
    <f:selectItem itemValue="ND" itemLabel="ND"/>
    <f:selectItem itemValue="OH" itemLabel="OH"/>
    <f:selectItem itemValue="OK" itemLabel="OK"/>
    <f:selectItem itemValue="OR" itemLabel="OR"/>
    <f:selectItem itemValue="PA" itemLabel="PA"/>
    <f:selectItem itemValue="RI" itemLabel="RI"/>
    <f:selectItem itemValue="SC" itemLabel="SC"/>
    <f:selectItem itemValue="SD" itemLabel="SD"/>

    <f:selectItem itemValue="TN" itemLabel="TN"/>
    <f:selectItem itemValue="TX" itemLabel="TX"/>
    <f:selectItem itemValue="UT" itemLabel="UT"/>
    <f:selectItem itemValue="VT" itemLabel="VT"/>
    <f:selectItem itemValue="VA" itemLabel="VA"/>
    <f:selectItem itemValue="WA" itemLabel="WA"/>
    <f:selectItem itemValue="WV" itemLabel="WV"/>
    <f:selectItem itemValue="WI" itemLabel="WI"/>
    <f:selectItem itemValue="WY" itemLabel="WY"/>
</h:selectOneMenu>
<h:outputText value=""/>

<h:outputText value="#{bundle.zipLabel}"/>
<h:inputText id="zip"
             value="#{customer.zip}"
             size="10" required="true">
    <cs:format_validator formatPatterns="99999|99999-9999|### ###"/>
</h:inputText>
<h:message styleClass="validationMessage" for="zip"/>

<h:outputText value="#{bundle.ccNumberLabel}"/>
<h:inputText id="ccno" size="16"
             converter="creditCardConverter" required="true">
    <cs:format_validator
            formatPatterns="9999999999999999|9999 9999 9999 9999|9999-9999-9999-9999"/>
</h:inputText>
<h:message styleClass="validationMessage" for="ccno"/>

<h:outputText value="#{bundle.monthLabel}"/>
<h:panelGrid id="monthYearPanel" columns="2" footerClass="medium"
             headerClass="medium" styleClass="medium" columnClasses="medium">
    <h:selectOneMenu value="#{customer.month}">
        <f:selectItem itemValue="01" itemLabel="01"/>
        <f:selectItem itemValue="02" itemLabel="02"/>
        <f:selectItem itemValue="03" itemLabel="03"/>
        <f:selectItem itemValue="04" itemLabel="04"/>
        <f:selectItem itemValue="05" itemLabel="05"/>
        <f:selectItem itemValue="06" itemLabel="06"/>
        <f:selectItem itemValue="07" itemLabel="07"/>
        <f:selectItem itemValue="08" itemLabel="08"/>
        <f:selectItem itemValue="09" itemLabel="09"/>
        <f:selectItem itemValue="10" itemLabel="10"/>
        <f:selectItem itemValue="11" itemLabel="11"/>
        <f:selectItem itemValue="12" itemLabel="12"/>
    </h:selectOneMenu>

    <h:selectOneMenu value="#{customer.year}">
        <f:selectItem itemValue="2002" itemLabel="2002"/>
        <f:selectItem itemValue="2003" itemLabel="2003"/>
        <f:selectItem itemValue="2004" itemLabel="2004"/>
        <f:selectItem itemValue="2005" itemLabel="2005"/>
        <f:selectItem itemValue="2006" itemLabel="2006"/>
        <f:selectItem itemValue="2007" itemLabel="2007"/>
        <f:selectItem itemValue="2008" itemLabel="2008"/>
    </h:selectOneMenu>
</h:panelGrid>
<h:outputText value=""/>

</h:panelGrid>

<h:commandButton value="#{bundle.finishButton}" action="finish"/>

<h:graphicImage id="duke" url="/images/duke.gif"/>

<h:outputText value="#{bundle.buyLabel}"/>

</h:panelGrid>

</h:form>
</f:view>
</body>
</html>

