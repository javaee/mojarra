<%@ include file="header.inc" %>
<f:view> 
    <h3>TabView - Module Style Demo</h3>
    This page shows a three tab tabView in the "module" style 
    with "auto" <code>maxHeight</code>.
    <br /><br />
    <risb:tabView tabStyle="module" maxHeight="auto">
        <risb:tab label="Label 1">
            <h:outputText value="Some text!"/>
        </risb:tab>
        <risb:tab>
            <f:facet name="label"><h:outputText value="Label 2"/></f:facet>
            <h:outputText>
                Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi 
                gravida. Etiam nibh metus, tincidunt eget, egestas eu, dictum at, 
                purus. Integer vehicula eros sit amet magna. Nulla dignissim. 
                Donec lobortis libero ac lacus. Nulla fermentum enim ac turpis 
                suscipit aliquet. Maecenas posuere erat nec justo. Cum sociis 
                natoque penatibus et magnis dis parturient montes, nascetur 
                ridiculus mus. In suscipit. Nullam ornare velit non felis. 
                Suspendisse potenti. Mauris orci dui, facilisis fringilla, 
                facilisis eget, molestie ut, enim. Nullam quam. Quisque aliquet. 
                Mauris arcu dui, vestibulum eget, commodo et, suscipit in, nunc. 
                Ut blandit felis ullamcorper magna. Sed nunc neque, tincidunt 
                ultricies, vehicula ut, aliquam quis, ipsum. Vivamus lorem urna, 
                volutpat quis, interdum nec, faucibus in, est. Etiam lobortis mi 
                ac libero.
            </h:outputText>
        </risb:tab>
        <risb:tab active="true">
            <f:facet name="label"><h:outputText value="Label 3"/></f:facet>
            <risb:download mimeType="image/png" fileName="sample.png" data="#{testBean.image}" urlVar="foo">
                <h:graphicImage url="#{foo}" width="250px" />
            </risb:download>
        </risb:tab>
    </risb:tabView>
</f:view>
<%@ include file="footer.inc" %>