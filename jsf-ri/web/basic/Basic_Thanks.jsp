<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> Basic Components Thank You Page </H3>
    <hr>

        <faces:form id="/basicForm" >

            <table> 
            <tr> 
              <td> <faces:output_text id="thanksLabel" /> </td>
            </tr>

          </TR>
            <td><faces:command_hyperlink id="mylink" /></td>
          </TR>

          </table>

        </faces:form>
</HTML>
