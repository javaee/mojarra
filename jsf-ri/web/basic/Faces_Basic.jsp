<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <hr>
    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
    <faces:useFaces>
        <faces:form name='basicForm' >
            <faces:Output_Text name='hello_label' value='Hello World' />
<P></P>
	    <faces:command_button name="Login" label="login"/>
<P></P>
            <faces:command_button name="Login" image="duke.gif"/>
        </faces:form>
    </faces:useFaces>
</HTML>
