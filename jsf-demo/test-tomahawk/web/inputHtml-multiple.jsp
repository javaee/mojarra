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

<body>

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup id="body">

        <h:form>
            <f:verbatim>
                <h1>Html Editor - multiple instances</h1>
                <b>Warning, this is for TEST, and doesn't work yet :-(</b><br/>
                Powered by the <a href="http://kupu.oscom.org">Kupu library</a>
            </f:verbatim>

			<f:verbatim><h2>Editor #1</h2></f:verbatim>
            <t:inputHtml value="#{editor1.text}" style="height: 40ex;"/>

			<f:verbatim><h2>Editor #2</h2></f:verbatim>
            <t:inputHtml value="#{editor2.text}" style="height: 40ex;"/>

			<f:verbatim><br/><br/></f:verbatim>
            <h:commandButton value="Submit"/>
        </h:form>

    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
