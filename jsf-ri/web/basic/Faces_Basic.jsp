<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>

        <faces:form id="/basicForm" >

            <table> 
            <tr> 
              <td> <faces:output_text id="userLabel" /> </td>
              <td> <faces:textentry_input id="userName" />
              </td>
            </tr>

            <tr>
               <td> <faces:output_text id="pwdLabel" /> </td>
               <td> <faces:textentry_secret id="password" /> </td>
             </tr>
       
             <tr>
                <td> <faces:output_text id="addrLabel" /> </td>
                <td> <faces:textentry_textarea id="address" /> </td>
             </tr>

              <tr>
             <td> <faces:selectboolean_checkbox id="validUser" />
                  </td>
             </tr>
<tr>

</tr>
             <tr> 
             <td><faces:command_button id="login" /></td>
             </tr>

          </table>

        </faces:form>
</HTML>
