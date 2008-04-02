<%@ include file="header.inc" %>
<f:view> 
     <h3>"Value" Binding</h3>
     <risb:tree id="foo">
        <risb:treeNode>
            <h:outputText value="Test 1"/>
            <risb:treeNode>
                <h:outputText value="Test 1-1"/>
                <risb:treeNode>
                    <h:outputText value="Test 1-1-1"/>
                </risb:treeNode>
                <risb:treeNode>
                    <h:outputText value="Test 1-1-2"/>
                </risb:treeNode>
            </risb:treeNode>
        </risb:treeNode>
        <risb:treeNode>
            <h:outputText id="test2" value="Test 2"/>
            <risb:treeNode>
                <h:outputLink value="http://blogs.steeplesoft.com">
                    <h:graphicImage id="image" url="/download.jpg" />
                </h:outputLink>
            </risb:treeNode>
        </risb:treeNode>
     </risb:tree>
     
     <h3>Component Binding</h3>
     <risb:tree binding="#{testBean.tree}"/>
</f:view>
<%@ include file="footer.inc" %>