<%@ include file="header.inc" %>
<f:view> 
    <h:outputText id="text" value="This should have a context menu!  Right click me!">
        <risb:contextMenu width="225px">
            <risb:menuItem value="Test!" url="http://www.yahoo.com"/>
            <risb:menuItem>
                <h:outputLink value="https://javaserverfaces.dev.java.net/">JSF RI Homepage</h:outputLink>
            </risb:menuItem>
        </risb:contextMenu>
    </h:outputText>
</f:view>
<%@ include file="footer.inc" %>