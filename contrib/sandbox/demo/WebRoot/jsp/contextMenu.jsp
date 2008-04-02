<%@ include file="header.inc" %>
<f:view> 
    <h:form>
    <h:outputText id="text" value="This should have a context menu!  Right click me!">
        <risb:contextMenu width="225px">
            <risb:menuItem value="Test!" url="http://www.yahoo.com"/>
            <risb:menuItem>
                <h:outputLink value="https://javaserverfaces.dev.java.net/">
                    <h:outputText value="JSF RI Homepage"/>
                </h:outputLink>
            </risb:menuItem>
            <risb:menuItem>
                <h:commandLink action="home" value="Home"/>
            </risb:menuItem>
        </risb:contextMenu>
    </h:outputText>
    </h:form>
</f:view>
<%@ include file="footer.inc" %>
