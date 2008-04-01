<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <faces:UseFaces>
        <faces:Form name='basicForm' model="LoginBean">
            <faces:Listener name="loginListener" scope="session" className="basic.EventHandler" />
            <faces:Command name="handleLogin" scope="session" className="basic.EventHandler" onCompletion="welcome.jsp" onError="error.jsp"/>

           <table> 
            <tr> 
              <td> <faces:Output_Text name='userLabel'  value='UserName' /> </td>
              <td> <faces:TextEntry_Input name="userName" size="20" maxlength="26" valueChangeListener="loginListener" />  </td>
            </tr>

             <tr>
                <td> <faces:Output_Text name='pwdLabel'  value='Password' /> </td>
                <td> <faces:TextEntry_Secret name="password" size="20" maxlength="26" valueChangeListener ="loginListener"/> </td>
             </tr>
       
             <tr>
                <td> <faces:Output_Text name='addrLabel'  value='Address' /> </td>
                <td> <faces:TextEntry_TextArea name="address" cols="150" rows="10" valueChangeListener="loginListener" /> </td>
             </tr>

              <tr>
             <td> <faces:SelectBoolean_Checkbox name="validUser" value="Checker" valueChangeListener="loginListener" label="Check this" />
                  </td>
             </tr>

             <tr> 
             <td><faces:Command_Button name="login" label="login" command="handleLogin" /></td>
             </tr>

          </table>

            <P></P>
            <faces:Command_Button name="Login" image="duke.gif"/>
            <P></P>
            <faces:SelectBoolean_Checkbox name="Checkbox" value="Checker" label="Check this"/>
            <P></P>
            <faces:Command_Hyperlink target="Faces_Basic.jsp" text="Hello World"/>
            <P></P>
            [A Hyperlink Image:]
            <faces:Command_Hyperlink target="Faces_Basic.jsp" image="duke.gif"/>
            <P></P>
            <faces:Command_Hyperlink target="Faces_Basic.jsp" image="duke.gif" text="Hello World`"/>
        </faces:Form>
    </faces:UseFaces>
</HTML>
