<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ page extends="com.sun.faces.Page" %>
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

        <faces:Form id="basicForm" model="LoginBean" navigationMapId="navMap" >
            <faces:Errors />
            <faces:Listener id="loginListener" scope="session" className="basic.EventHandler" />
            <faces:Listener id="radioListener" scope="session" className="basic.EventHandler" />
            <faces:Listener id="optionListener" scope="session" className="basic.EventHandler" />

            <faces:Listener id="basicListener" scope="session" className="basic.BasicListener" />
           
            <table> 
            <tr> 
              <td> <faces:Output_Text id="userLabel"  value="UserName" /> </td>
              <td> <faces:TextEntry_Input id="userName" 
                                          value="default" 
                                          valueChangeListener="loginListener"  
                                          required = "true"
                                          lengthMinimum="1"
                                          lengthMaximum="15" />
              </td>
            </tr>

            <tr>
              <td> <faces:Output_Text id="pinLabel"  value="Pin Number" /> </td>
              <td> <faces:TextEntry_Input id="pin"
                                          required = "true"
                                          modelType ="java.lang.Integer"
                                          rangeMinimum="1"
                                          rangeMaximum="1000" />
              </td>
            </tr>

            <tr>
              <td> <faces:Output_Text id="dobLabel"  value="Date of Birth" /> </td>

              <td> <faces:TextEntry_Input id="dob"
                                          required = "true"
                                          converter="DateValidator" />
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
             <td><faces:Command_Button id="login" label="login" commandListener="basicListener" /></td>
             </tr>

          </table>

        </faces:Form>
</HTML>
