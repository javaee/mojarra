<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Standard RenderKit Demo - Thank You Page</TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

    <H3> Standard RenderKit Thank You Page </H3>
    <hr>

       <f:view>
        <h:form id="standardForm" >
 
            <table> 

           <c:if test="${model.hasComponent == 'true'}">

            <tr> 
              <td>The actuated component 
                  was <h:output_text id="actuated"
                               value="model.whichComponent"/>. </td>
            </tr>

        </c:if>

          </TR>
            <td><a href='<%= request.getContextPath() + "/" %>'>
                Back To RenderKit Demo</a></td>
          </TR>

          </table>

        </h:form>
     </f:view>
</HTML>
