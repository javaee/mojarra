<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <faces:useFaces>
        <faces:form name='basicForm' >
            <faces:Output_Text name='hello_label' value='Hello World' />
             <P></P>
	    <faces:command_button name="Login" label="login"/>
            <P></P>
            <faces:command_button name="Login" image="duke.gif"/>
            <P></P>
            <faces:selectboolean_checkbox name="Checkbox" value="Checker" label="Check this"/>
            <P></P>
            <faces:TextEntry_Input name="textField" value="Hello World" size="20" maxlength="26"/>
            <P></P>
            <faces:TextEntry_Secret name="password" value="Hello World" size="20" maxlength="26"/>
            <P></P>
            <faces:TextEntry_TextArea name="textarea" cols="150" rows="10" wrap="OFF"> Hello World </faces:TextEntry_TextArea>
        </faces:form>
    </faces:useFaces>
</HTML>
