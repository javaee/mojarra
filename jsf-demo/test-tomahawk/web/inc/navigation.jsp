<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<t:panelNavigation id="nav"
              styleClass="navigation"
              separatorClass="navseparator"
              itemClass="navitem"
              activeItemClass="navitem_active"
              openItemClass="navitem_open"   >
    <t:commandNavigation id="nav_1" value="#{example_messages['nav_Home']}" action="go_home">
        <f:param name="param" value="value"/>
    </t:commandNavigation>
    <t:commandNavigation id="nav_2" value="#{example_messages['nav_Examples']}" >
        <t:commandNavigation id="nav_2_1" value="#{example_messages['nav_Sample_1']}" action="go_sample1" />
        <t:commandNavigation id="nav_2_2" value="#{example_messages['nav_Sample_2']}" action="go_sample2" />
        <t:commandNavigation id="nav_2_3" value="#{example_messages['nav_Validate']}" action="go_validate" />
        <t:commandNavigation id="nav_2_4" value="#{example_messages['nav_Components']}" >
            <t:commandNavigation id="nav_2_4_1" value="#{example_messages['nav_aliasBean']}" action="go_aliasBean" />
            <t:commandNavigation id="nav_2_4_1_2" value="#{example_messages['nav_buffer']}" action="go_buffer" />
            <t:commandNavigation id="nav_2_4_2" value="#{example_messages['nav_dataTable']}" action="go_dataTable" />
            <t:commandNavigation id="nav_2_4_3" value="#{example_messages['nav_sortTable']}" action="go_sortTable" />
            <t:commandNavigation id="nav_2_4_4" value="#{example_messages['nav_Selectbox']}" action="go_selectbox" />
            <t:commandNavigation id="nav_2_4_5" value="#{example_messages['nav_FileUpload']}" action="go_fileupload" />
            <t:commandNavigation id="nav_2_4_6" value="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" />
            <t:commandNavigation id="nav_2_4_7" value="#{example_messages['nav_Calendar']}" action="go_calendar" />
            <t:commandNavigation id="nav_2_4_71" value="#{example_messages['nav_Popup']}" action="go_popup" />
            <t:commandNavigation id="nav_2_4_72" value="#{example_messages['nav_JsListener']}" action="go_jslistener" />
            <t:commandNavigation id="nav_2_4_73" value="#{example_messages['nav_panelnavigation']}" action="go_panelnavigation" />
            <t:commandNavigation id="nav_2_4_8" value="#{example_messages['nav_Date']}" action="go_date" />
            <t:commandNavigation id="nav_2_4_81" value="#{example_messages['nav_InputHtml']}" action="go_inputHtml" />
            <t:commandNavigation id="nav_2_4_9" value="#{example_messages['nav_dataList']}" action="go_dataList" />
            <t:commandNavigation id="nav_2_4_10" value="#{example_messages['nav_tree']}" action="go_tree" />
            <t:commandNavigation id="nav_2_4_100" value="#{example_messages['nav_tree2']}" action="go_tree2" />
            <t:commandNavigation id="nav_2_4_11" value="#{example_messages['nav_treeTable']}" action="go_treeTable"/>
            <t:commandNavigation id="nav_2_4_12" value="#{example_messages['nav_rssTicker']}" action="go_rssticker" />
            <t:commandNavigation id="nav_2_4_13" value="#{example_messages['nav_dataScroller']}" action="go_datascroller" />
            <t:commandNavigation id="nav_2_4_14" value="#{example_messages['nav_panelstack']}" action="go_panelstack" />
            <t:commandNavigation id="nav_2_4_15" value="#{example_messages['nav_css']}" action="go_css" />
            <t:commandNavigation id="nav_2_4_16" value="#{example_messages['nav_newspaperTable']}" action="go_newspaperTable" />
            <t:commandNavigation id="nav_2_4_17" value="#{example_messages['nav_swapimage']}" action="go_swapimage" />
            <t:commandNavigation id="nav_2_4_18" value="#{example_messages['nav_forceId']}" action="go_forceId" />
            <t:commandNavigation id="nav_2_4_19" value="#{example_messages['nav_selectOneCountry']}" action="go_selectOneCountry" />
        </t:commandNavigation>
    </t:commandNavigation>
    <t:commandNavigation id="nav_3" value="#{example_messages['nav_Documentation']}" >
        <t:commandNavigation id="nav_3_1" value="#{example_messages['nav_Features']}" action="go_features"/>
    </t:commandNavigation>
    <t:commandNavigation id="nav_4" value="#{example_messages['nav_Options']}" action="go_options" />
    <f:verbatim>&nbsp;</f:verbatim>
    <t:commandNavigation id="nav_5" value="#{example_messages['nav_Info']}" >
        <t:commandNavigation id="nav_5_1" value="#{example_messages['nav_Contact']}" action="go_contact" />
        <t:commandNavigation id="nav_5_2" value="#{example_messages['nav_Copyright']}" action="go_copyright" />
    </t:commandNavigation>
</t:panelNavigation>
