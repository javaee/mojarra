<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:panelGrid id="header_group1" columns="2" styleClass="pageHeader1"  >
    <t:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="#{example_messages['alt_logo']}" />
    <f:verbatim>
        &nbsp;&nbsp;
        <font size="+1" color="#FFFFFF">MyFaces - The free JavaServer&#8482; Faces Implementation</font>
        <font size="-1" color="#FFFFFF">(Version 1.1.1)</font>
    </f:verbatim>
</h:panelGrid>

<h:panelGrid id="header_group2" columns="1" styleClass="pageHeader2" columnClasses="pageHeader2col1"  >
    <t:jscookMenu layout="hbr" theme="ThemeOffice" >
        <%/* Availaible jscookMenu themes: ThemeIE, ThemeMiniBlack, ThemeOffice, ThemePanel
             Availaible jscookMenu layout: hbr, hbl, hur, hul, vbr, vbl, vur, vul
             respect to Heng Yuan http://www.cs.ucla.edu/~heng/JSCookMenu
        */%>
        <t:navigationMenuItem id="nav_1" itemLabel="#{example_messages['nav_Home']}" action="go_home" />
        <t:navigationMenuItem id="nav_2" itemLabel="#{example_messages['nav_Examples']}" >
            <t:navigationMenuItem id="nav_2_1" itemLabel="#{example_messages['nav_Sample_1']}" action="go_sample1" icon="images/myfaces.gif" />
            <t:navigationMenuItem id="nav_2_2" itemLabel="#{example_messages['nav_Sample_2']}" action="go_sample2" icon="images/myfaces.gif" />
            <t:navigationMenuItem id="nav_2_3" itemLabel="#{example_messages['nav_Validate']}" action="go_validate" icon="images/myfaces.gif" />
            <t:navigationMenuItem id="nav_2_4" itemLabel="#{example_messages['nav_Components']}" icon="images/component.gif" split="true" >
	            <t:navigationMenuItem id="nav_2_4_1" itemLabel="#{example_messages['nav_aliasBean']}" action="go_aliasBean" icon="images/myfaces.gif" />
  	            <t:navigationMenuItem id="nav_2_4_1_2" itemLabel="#{example_messages['nav_buffer']}" action="go_buffer" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_2" itemLabel="#{example_messages['nav_dataTable']}" action="go_dataTable" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_3" itemLabel="#{example_messages['nav_sortTable']}" action="go_sortTable" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_4" itemLabel="#{example_messages['nav_Selectbox']}" action="go_selectbox" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_5" itemLabel="#{example_messages['nav_FileUpload']}" action="go_fileupload" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_6" itemLabel="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_7" itemLabel="#{example_messages['nav_Calendar']}" action="go_calendar" icon="images/myfaces.gif" split="true" />
                <t:navigationMenuItem id="nav_2_4_71" itemLabel="#{example_messages['nav_Popup']}" action="go_popup" icon="images/myfaces.gif" split="true" />
                <t:navigationMenuItem id="nav_2_4_72" itemLabel="#{example_messages['nav_JsListener']}" action="go_jslistener" icon="images/myfaces.gif" split="true" />                
            	<t:navigationMenuItem id="nav_2_4_8" itemLabel="#{example_messages['nav_Date']}" action="go_date" icon="images/myfaces.gif" />
	            <t:navigationMenuItem id="nav_2_4_81" itemLabel="#{example_messages['nav_InputHtml']}" action="go_inputHtml" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_9" itemLabel="#{example_messages['nav_dataList']}" action="go_dataList" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_10" itemLabel="#{example_messages['nav_tree']}" action="go_tree" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_100" itemLabel="#{example_messages['nav_tree2']}" action="go_tree2" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_11" itemLabel="#{example_messages['nav_treeTable']}" action="go_treeTable" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_12" itemLabel="#{example_messages['nav_rssTicker']}" action="go_rssticker" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_13" itemLabel="#{example_messages['nav_dataScroller']}" action="go_datascroller" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_14" itemLabel="#{example_messages['nav_panelstack']}" action="go_panelstack" icon="images/myfaces.gif" />
	            <t:navigationMenuItem id="nav_2_4_15" itemLabel="#{example_messages['nav_css']}" action="go_css" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_16" itemLabel="#{example_messages['nav_newspaperTable']}" action="go_newspaperTable" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_17" itemLabel="#{example_messages['nav_swapimage']}" action="go_swapimage" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_18" itemLabel="#{example_messages['nav_forceId']}" action="go_forceId" icon="images/myfaces.gif" />
                <t:navigationMenuItem id="nav_2_4_19" itemLabel="#{example_messages['nav_selectOneCountry']}" action="go_selectOneCountry" icon="images/myfaces.gif" />
            </t:navigationMenuItem>
        </t:navigationMenuItem>
        <t:navigationMenuItem id="nav_3" itemLabel="#{example_messages['nav_Documentation']}" >
            <t:navigationMenuItem id="nav_3_1" itemLabel="#{example_messages['nav_Features']}" action="go_features" icon="images/myfaces.gif" />
        </t:navigationMenuItem>
        <t:navigationMenuItem id="nav_4" itemLabel="#{example_messages['nav_Options']}" action="go_options" />
        <t:navigationMenuItems id="nav_5" value="#{navigationMenu.infoItems}" />

    </t:jscookMenu>
</h:panelGrid>

