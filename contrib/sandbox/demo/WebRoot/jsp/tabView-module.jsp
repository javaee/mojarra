<%@ include file="header.inc" %>
<f:view> 
    <risb:tabView tabStyle="module">
        <risb:tab label="Label 1">
            <h:outputText value="Some text!"/>
        </risb:tab>
        <risb:tab active="true">
            <f:facet name="label"><h:outputText value="Label 2"/></f:facet>
            <h:outputText value="Some more text!"/>
        </risb:tab>
    </risb:tabView>
</f:view>
<%@ include file="footer.inc" %>