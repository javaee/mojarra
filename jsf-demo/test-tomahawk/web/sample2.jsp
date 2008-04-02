<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<html>

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

<!--
managed beans used:
    q_form
-->

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup id="body">
                                                   
        <h:messages id="messageList" />

        <h:form id="q_form" name="q_form">
            <h:inputTextarea id="text"
                              rows="5"
                              value="#{q_form.text}" required="true" />
            <f:verbatim><br><br></f:verbatim>
            <h:selectOneMenu id="oneoption" value="#{q_form.quoteChar}" >
                <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_quote']}" />
                <f:selectItem itemValue="\"" itemLabel="Double" />
                <f:selectItem itemValue="'" itemLabel="Single" />
                <f:selectItems value="#{q_form.selectOneItems}" />
            </h:selectOneMenu>
            <h:commandButton id="button1" value="#{example_messages['sample2_add_quote']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example2.QuotationController" ></f:actionListener>
            </h:commandButton>

            <f:verbatim><br><br></f:verbatim>
            <h:selectManyListbox id="manyoptions" value="#{q_form.selectManyValues}" >
                <f:selectItem itemValue="" itemLabel="#{example_messages['sample2_select_unquote']}" />
                <f:selectItems value="#{q_form.selectManyItems}" />
            </h:selectManyListbox>
            <h:commandButton id="button2" value="#{example_messages['sample2_remove_quote']}" action="none"><f:verbatim><br></f:verbatim>
                <f:actionListener type="org.apache.myfaces.examples.example2.QuotationController" ></f:actionListener>
            </h:commandButton>

        </h:form>

    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
