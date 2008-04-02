
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

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
	<t:jscookMenu layout="hbr" theme="ThemeOffice" styleLocation="css/jscookmenu">
		<%/* Availaible jscookMenu themes: ThemeIE, ThemeMiniBlack, ThemeOffice, ThemePanel
             Availaible jscookMenu layout: hbr, hbl, hur, hul, vbr, vbl, vur, vul
             respect to Heng Yuan http://www.cs.ucla.edu/~heng/JSCookMenu
        */%>
		<t:navigationMenuItem id="nav_1"
			itemLabel="#{example_messages['nav_Home']}" action="go_home" />
		<t:navigationMenuItem id="nav_2"
			itemLabel="#{example_messages['nav_Examples']}">
			<t:navigationMenuItem id="nav_2_1"
				itemLabel="#{example_messages['nav_Sample_1']}" action="go_sample1" />
			<t:navigationMenuItem split="true"> </t:navigationMenuItem> 
			<t:navigationMenuItem id="nav_2_2"
				itemLabel="#{example_messages['nav_Sample_2']}" action="go_sample2"
				icon="images/myfaces.gif" />
			<t:navigationMenuItem id="nav_2_3"
				itemLabel="#{example_messages['nav_Validate']}" action="go_validate"
				icon="images/myfaces.gif" />
			<t:navigationMenuItem id="nav_2_4"
				itemLabel="#{example_messages['nav_Components']}"
				icon="images/component.gif" split="true">
				<t:navigationMenuItem id="nav_2_4_1"
					itemLabel="#{example_messages['nav_sortTable']}"
					action="go_sortTable" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_2"
					itemLabel="#{example_messages['nav_Selectbox']}"
					action="go_selectbox" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_3"
					itemLabel="#{example_messages['nav_FileUpload']}"
					action="go_fileupload" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_4"
					itemLabel="#{example_messages['nav_TabbedPane']}"
					action="go_tabbedPane" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_5"
					itemLabel="#{example_messages['nav_Calendar']}"
					action="go_calendar" icon="images/myfaces.gif" split="true" />
				<t:navigationMenuItem id="nav_2_4_6"
					itemLabel="#{example_messages['nav_Popup']}" action="go_popup"
					icon="images/myfaces.gif" split="true" />
				<t:navigationMenuItem id="nav_2_4_7"
					itemLabel="#{example_messages['nav_Date']}" action="go_date"
					icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_8"
					itemLabel="#{example_messages['nav_InputHtml']}"
					action="go_inputHtml" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_9"
					itemLabel="#{example_messages['nav_tree2']}" action="go_tree2"
					icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_10"
					itemLabel="#{example_messages['nav_treeTable']}"
					action="go_treeTable" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_11"
					itemLabel="#{example_messages['nav_rssTicker']}"
					action="go_rssticker" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_12"
					itemLabel="#{example_messages['nav_dataScroller']}"
					action="go_datascroller" icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_13"
					itemLabel="#{example_messages['nav_css']}" action="go_css"
					icon="images/myfaces.gif" />
				<t:navigationMenuItem id="nav_2_4_14"
					itemLabel="#{example_messages['nav_newspaperTable']}"
					action="go_newspaperTable" icon="images/myfaces.gif" />
			</t:navigationMenuItem>
		</t:navigationMenuItem>
	</t:jscookMenu>
</f:view>
<%@include file="inc/page_footer.jsp"%>

</body>

</html>
