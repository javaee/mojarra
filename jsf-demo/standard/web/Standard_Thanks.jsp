<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Standard RenderKit Demo - Thank You Page</TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <H3> Standard RenderKit Thank You Page </H3>
    <hr>

       <f:use_faces>
        <h:form id="standardForm" formName="standardForm" >

            <table> 
            <tr> 
              <td> <h:output_text id="thanksLabel" value="Thanks for using the Standard RenderKit Demo..." /> </td>
            </tr>

          </TR>
            <td><h:command_hyperlink id="link" 
                href="/faces/index.html"
                label="Back To RenderKit Demo" /></td>
          </TR>

          </table>


        </h:form>
     </f:use_faces>
</HTML>
