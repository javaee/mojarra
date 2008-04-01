<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
        <faces:DeclareBean scope="session" id="LoginBean" 
                           className="basic.LoginBean"/>
        <faces:DeclareBean scope="session" id="ShipTypeBean" 
                           className="basic.ShipTypeBean"/>

         <faces:DeclareBean scope="session" id="DateValidator"
                           className="basic.DateValidator"/>

        <faces:NavigationMap id="navMap" scope="session" >
            <faces:outcome commandName="login" outcome="success" targetAction="forward"
                    targetPath = "welcome.jsp" />

            <faces:outcome commandName="login" outcome="failure" targetAction="forward"
                    targetPath = "error.jsp" />
        </faces:NavigationMap>

        <faces:Form id="basicForm" modelReference="${LoginBean}" navigationMapId="navMap" >
            <faces:Listener id="loginListener" scope="session" className="basic.EventHandler" />
            <faces:Listener id="radioListener" scope="session" className="basic.EventHandler" />
            <faces:Listener id="optionListener" scope="session" className="basic.EventHandler" />
           
            <table> 
            <tr> 
              <td> <faces:Output_Text id="userLabel"  value="UserName" /> </td>
              <td> <faces:TextEntry_Input id="userName" 
                                          value="default" 
                                          valueChangeListener="loginListener" />
              </td>
            </tr>

             <tr>
                <td> <faces:Output_Text id="pwdLabel"  value="Password" /> </td>
                <td> <faces:TextEntry_Secret id="password" size="20" maxlength="26" valueChangeListener ="loginListener"/> </td>
             </tr>
       
             <tr>
                <td> <faces:Output_Text id="addrLabel"  value="Address" /> </td>
                <td> <faces:TextEntry_TextArea id="address" cols="150" rows="10" valueChangeListener="loginListener" /> </td>
             </tr>

              <tr>
             <td> <faces:SelectBoolean_Checkbox id="validUser" value="Checker" valueChangeListener="loginListener" label="Check this" />
                  </td>
             </tr>
<tr>
           <table> 
<TR>
<faces:RadioGroup id="shipType" 
                  modelReference="${ShipTypeBean.shipType}" 
                  selectedModelReference="${ShipTypeBean.currentShipType}" 
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
    <faces:SelectOne_OptionList id="appleQuantity"
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
             <td><faces:Command_Button id="login" label="login" commandListener="loginListener" /></td>
             </tr>

          </table>

            <P></P>
            <faces:Command_Button id="Login" image="duke.gif"/>
            <P></P>
            <faces:SelectBoolean_Checkbox id="Checkbox" value="Checker" label="Check this"/>
            <P></P>
            <faces:Command_Hyperlink target="Faces_Basic.jsp" text="Hello World"/>
            <P></P>
            [A Hyperlink Image:]
            <faces:Command_Hyperlink target="Faces_Basic.jsp" image="duke.gif"/>
            <P></P>
            <faces:Command_Hyperlink target="Faces_Basic.jsp" image="duke.gif" text="Hello World`"/>
        </faces:Form>
</HTML>
