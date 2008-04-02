<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc" %>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup>
        <h:form id="countryForm" name="countryForm">
            <t:saveState value="#{countryForm.id}" />
            <h:panelGrid columns="2" styleClass="countryFormTable"
                         headerClass="countryFormHeader"
                         footerClass="countryFormFooter"
                         columnClasses="countryFormLabels, countryFormInputs" >
                <f:facet name="header">
                    <h:outputText id="cfH" value="(Country Form Header)"/>
                </f:facet>
                <f:facet name="footer">
                    <h:outputText value="(Country Form Footer)"/>
                </f:facet>

                <h:outputLabel for="name" value="#{example_messages['label_country_name']}"/>
                <h:panelGroup>
                    <h:inputText id="name" value="#{countryForm.name}" required="true" />
                    <t:message for="name" styleClass="error" showDetail="true" showSummary="false" />
                </h:panelGroup>

                <h:outputLabel for="isoCode" value="#{example_messages['label_country_iso']}"/>
                <h:panelGroup>
                    <h:inputText id="isoCode" value="#{countryForm.isoCode}" required="true">
                        <f:validateLength maximum="2" minimum="2"/>
                    </h:inputText>
                    <t:message for="isoCode" styleClass="error" showDetail="true" showSummary="false" />
                </h:panelGroup>

                <h:panelGroup/>
                <h:panelGroup>
                    <h:commandButton action="#{countryForm.save}" value="#{example_messages['button_save']}" />
                    <f:verbatim>&nbsp;</f:verbatim>
                    <h:commandButton action="cancel" immediate="true" value="#{example_messages['button_cancel']}" />
                    <f:verbatim>&nbsp;</f:verbatim>
                    <h:commandButton action="#{countryForm.delete}" immediate="true" value="#{example_messages['button_delete']}" />
                    <f:verbatim>&nbsp;</f:verbatim>
                    <h:commandButton action="#{countryForm.apply}" value="#{example_messages['button_apply']}" />
                </h:panelGroup>

            </h:panelGrid>
        </h:form>
    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
