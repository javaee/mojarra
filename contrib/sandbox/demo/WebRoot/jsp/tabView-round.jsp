<%@ include file="header.inc" %>
<f:view> 
    <risb:tabView tabStyle="round">
        <risb:tab label="Label 1">
            <h:outputText value="Some text!"/>
        </risb:tab>
        <risb:tab active="true">
            <f:facet name="label"><h:outputText value="Label 2"/></f:facet>
            <h:outputText value="Some more text!"/>
        </risb:tab>
        <risb:tab>
            <f:facet name="label"><h:outputText value="Label 3"/></f:facet>
            <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}" urlVar="foo">
                <h:graphicImage url="#{foo}" width="250px" />
            </risb:download>
        </risb:tab>
    </risb:tabView>
</f:view>
<%@ include file="footer.inc" %>