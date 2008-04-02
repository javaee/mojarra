
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>

<%@include file="inc/head.inc"%>

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
	<f:loadBundle
		basename="org.apache.myfaces.examples.resource.example_messages"
		var="example_messages" />

    <h:outputText value="test" />
    <t:div id="subnavigation_outer">
    <t:div id="subnavigation">
    <t:panelNavigation2 id="nav1" layout="list" itemClass="mypage" activeItemClass="selected" openItemClass="selected" >
        <t:navigationMenuItems value="#{navigationMenu.panelNavigationItems}" />
    </t:panelNavigation2>
    </t:div>
    </t:div>

</f:view>
<%@include file="inc/page_footer.jsp"%>

</body>

</html>
