<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <H3> Basic Components Thank You Page </H3>
    <hr>

       <f:use_faces>
        <h:form id="basicForm" formName="basicForm" >

            <table> 
            <tr> 
              <td> <h:output_text id="thanksLabel" value="Thanks for using the Basic Application..." /> </td>
            </tr>

          </TR>
            <td><h:command_hyperlink id="link" 
                target="/faces/Faces_Basic.jsp" 
                commandName="basiclink" 
                label="Back To Components Page" /></td>
          </TR>

          </table>


        </h:form>
     </f:use_faces>
</HTML>
