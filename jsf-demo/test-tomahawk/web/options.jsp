<%@ page session="false" contentType="text/html;charset=utf-8" %>
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

<body>

<!--
managed beans used:
    optionsForm
    optionsCtrl
-->

<f:view>

    <t:saveState id="ss1" value="#{optionsForm.language}" />

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <t:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <f:facet name="header">
            <f:subview id="header">
                <jsp:include page="inc/page_header.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>


        <f:facet name="body">
            <h:panelGroup id="body">
                <h:messages id="messageList" />

<f:verbatim>
                <h4>Options</h4>
                <table border="1"><tr><td>
</f:verbatim>
                    <h:form id="form1" name="optionsForm">
                        <h:outputText value="#{example_messages['option_lang']}" />
<f:verbatim>:&nbsp;</f:verbatim>
                        <h:selectOneMenu id="locale" value="#{optionsForm.language}">
                            <f:selectItems id="available" value="#{optionsForm.availableLanguages}" />
                        </h:selectOneMenu>
<f:verbatim><br></f:verbatim>
                        <h:outputText value="#{example_messages['option_layout']}" />
<f:verbatim>:&nbsp;</f:verbatim>
                        <h:selectOneMenu id="layout" value="#{globalOptions.pageLayout}"  >
                            <f:selectItem id="item101" itemLabel="Classic" itemValue="classic" />
                            <f:selectItem id="item102" itemLabel="Navigation right" itemValue="navigationRight" />
                            <f:selectItem id="item103" itemLabel="Upside down" itemValue="upsideDown" />
                        </h:selectOneMenu>
<f:verbatim><br></f:verbatim>
                        <h:commandButton id="apply" value="#{example_messages['button_apply']}" action="#{optionsCtrl.changeLocale}"/>
                    </h:form>

<f:verbatim>
                </td></tr></table>
</f:verbatim>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </t:panelLayout>

</f:view>

</body>

</html>