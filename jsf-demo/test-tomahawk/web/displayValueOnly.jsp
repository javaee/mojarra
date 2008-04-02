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

    <f:verbatim>
        <h1>Usage of the displayValueOnly attribute</h1>
    </f:verbatim>
    <h:form id="form" >
	    <t:saveState id="ss1" value="#{dvoFace.attribute}"/>

    	<h:selectBooleanCheckbox
	    	id="displayValueOnlyCheckBox"
	    	title="displayValueOnly"
			value="#{dvoFace.attribute}"/>
		<h:outputLabel id="displayValueOnlyCheckBoxL" for="displayValueOnlyCheckBox" value="displayValueOnly attribute"/>

		<f:verbatim><br/></f:verbatim>

		<h:panelGrid id="firstGrid" columns="2">
	        <h:outputLabel id="inputTextL" for="inputText" value="inputText"/>
	        <t:inputText id="inputText" value="#{dvoFace.map['inputText']}" displayValueOnly="#{dvoFace.attribute}"/>

	        <h:outputLabel id="inputTextareaL" for="inputTextarea" value="inputTextarea"/>
	        <t:inputTextarea id="inputTextarea" value="#{dvoFace.map['inputTextarea']}" displayValueOnly="#{dvoFace.attribute}"/>

	        <h:outputLabel for="selectManyCheckbox" value="selectManyCheckbox"/>
	        <t:selectManyCheckbox id="selectManyCheckbox" value="#{dvoFace.map['selectManyCheckboxList']}" displayValueOnly="#{dvoFace.attribute}">
			  	<f:selectItem itemLabel="Value 1" itemValue="1"/>
			  	<f:selectItem itemLabel="Value 2" itemValue="2"/>
			  	<f:selectItem itemLabel="Value 3" itemValue="3"/>
			</t:selectManyCheckbox>

	        <%--h:outputLabel for="selectOneMenu" value="selectOneMenu"/>
			<t:selectOneMenu id="selectOneMenu" value="#{dvoFace.map['selectOneMenu']}" displayValueOnly="#{dvoFace.attribute}">
			  	<f:selectItem itemLabel="Value 1" itemValue="1"/>
			  	<f:selectItem itemLabel="Value 2" itemValue="2"/>
			  	<f:selectItem itemLabel="Value 3" itemValue="3"/>
			</t:selectOneMenu--%>

	        <h:outputLabel id="selectManyMenuL" for="selectManyMenu" value="selectManyMenu"/>
			<t:selectManyMenu id="selectManyMenu" value="#{dvoFace.map['selectManyMenuList']}" displayValueOnly="#{dvoFace.attribute}">
			  	<f:selectItem itemLabel="Value 1" itemValue="1"/>
			  	<f:selectItem itemLabel="Value 2" itemValue="2"/>
			  	<f:selectItem itemLabel="Value 3" itemValue="3"/>
			</t:selectManyMenu>

			<%--h:outputLabel for="selectOneListbox" value="selectOneListbox"/>
			<t:selectOneListbox id="selectOneListbox" value="#{dvoFace.map['selectOneListbox']}" displayValueOnly="#{dvoFace.attribute}">
			  	<f:selectItem itemLabel="Value 1" itemValue="1"/>
			  	<f:selectItem itemLabel="Value 2" itemValue="2"/>
			  	<f:selectItem itemLabel="Value 3" itemValue="3"/>
			</t:selectOneListbox--%>

			<h:outputLabel id="inputHtmlL" for="inputHtml" value="inputHtml"/>
	        <t:inputHtml id="inputHtml" value="#{dvoFace.map['inputHtml']}" displayValueOnly="#{dvoFace.attribute}"/>

		</h:panelGrid>
        <f:verbatim><br/><br/>

        <h2>The attribute can also be set for a whole section</h2>

        </f:verbatim>
        <t:panelGrid id="secondGrid" columns="2" displayValueOnly="#{dvoFace.attribute}">
	        <h:outputLabel id="inputText2L" for="inputText2" value="inputText"/>
	        <t:inputText id="inputText2" value="#{dvoFace.map['inputText2']}"/>
	        
	        <h:outputLabel id="inputSecret2L" for="inputSecret2" value="inputSecret"/>
	        <t:inputSecret id="inputSecret2" value="secret" />

	        <h:outputLabel id="inputTextArea2L" for="inputTextarea2" value="inputTextarea"/>
	        <t:inputTextarea id="inputTextarea2" value="#{dvoFace.map['inputTextarea2']}"/>
	    </t:panelGrid>
        <h:commandButton id="submitBtn" />
    </h:form>

    <%@include file="inc/page_footer.jsp" %>

</f:view>

</body>

</html>
