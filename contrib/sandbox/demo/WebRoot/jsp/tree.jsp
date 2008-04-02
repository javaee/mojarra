<%@ include file="header.inc" %>
<f:view> 
     <risb:tree id="foo">
        <risb:treeNode>
            <h:outputText value="Test 1"/>
            <risb:treeNode>
                <h:outputText value="Test 1-1"/>
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
</f:view>
<%@ include file="footer.inc" %>