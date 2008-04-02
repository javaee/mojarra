<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <H3> JavaServer Faces custom components test page </H3>
    <hr>

       <f:use_faces>
        <h:form id="demo-testForm" formName="demo-testForm" >

            <table> 
            <tr> 
              <td> <h:output_text id="testLabel" value="JavaServer Faces custom components test page..." /> </td>
            </tr>

          </TR>
           <td> <a href='<%= request.getContextPath() + "/faces/menu.jsp" %>'>Back</a> </td>
          </TR>

          </table>


        </h:form>
     </f:use_faces>
</HTML>
