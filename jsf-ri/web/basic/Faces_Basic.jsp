<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <faces:UseFaces>
        <faces:DeclareBean scope="session" name="LoginBean" 
                           className="basic.LoginBean"/>
        <faces:DeclareBean scope="session" name="ShipTypeBean" 
                           className="basic.ShipTypeBean"/>

        <faces:Form name="basicForm" model="LoginBean">
            <faces:Listener name="loginListener" scope="session" className="basic.EventHandler" />
            <faces:Listener name="radioListener" scope="session" className="basic.EventHandler" />
            <faces:Listener name="optionListener" scope="session" className="basic.EventHandler" />
            <faces:Command name="handleLogin" scope="session" className="basic.EventHandler" onCompletion="welcome.jsp" onError="error.jsp"/>

           <table> 
            <tr> 
              <td> <faces:Output_Text name="userLabel"  value="UserName" /> </td>
              <td> <faces:TextEntry_Input name="userName" size="20" maxlength="26" value="default" valueChangeListener="loginListener" />  </td>
            </tr>

             <tr>
                <td> <faces:Output_Text name="pwdLabel"  value="Password" /> </td>
                <td> <faces:TextEntry_Secret name="password" size="20" maxlength="26" valueChangeListener ="loginListener"/> </td>
             </tr>
       
             <tr>
                <td> <faces:Output_Text name="addrLabel"  value="Address" /> </td>
                <td> <faces:TextEntry_TextArea name="address" cols="150" rows="10" valueChangeListener="loginListener" /> </td>
             </tr>

              <tr>
             <td> <faces:SelectBoolean_Checkbox name="validUser" value="Checker" valueChangeListener="loginListener" label="Check this" />
                  </td>
             </tr>
<tr>
           <table> 
<TR>
<faces:RadioGroup name="shipType" 
                  model="$ShipTypeBean.shipType" 
                  selectedValueModel="$ShipTypeBean.currentShipType" 
                  valueChangeListener="radioListener">
  <TABLE border="2"><TR><TD>
  <TABLE>
  <TR>
  <TD>Select Shipping</TD>
  <TD><faces:SelectOne_Radio value="nextDay" label=" Next Day"/></TD>
  <TD><faces:SelectOne_Radio checked="true" value="nextWeek" label="Next Week"/> </TD>
  <TD><faces:SelectOne_Radio value="nextMonth" label="Next Month"/> </TD>
  </TABLE>
  </TD></TR></TABLE>
</faces:RadioGroup>

</TR>
<TR>Options: 
    <faces:SelectOne_OptionList name="appleQuantity"
                   valueChangeListener="optionListener">
        <faces:SelectOne_Option value="0" label="0.00"/>
        <faces:SelectOne_Option value="1" label="1.00"/>
        <faces:SelectOne_Option value="2" label="2.00"/>
        <faces:SelectOne_Option value="3" label="3.00"/>
        <faces:SelectOne_Option value="4" selected="true" label="4.00"/>
        <faces:SelectOne_Option value="5" label="5.00"/>
        <faces:SelectOne_Option value="6" label="6.00"/>
        <faces:SelectOne_Option value="7" label="7.00"/>
        <faces:SelectOne_Option value="8" label="8.00"/>
        <faces:SelectOne_Option value="9" label="9.00"/>
    </faces:SelectOne_OptionList>

</TR>
          </table>
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
