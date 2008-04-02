<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Standard RenderKit Demo - Thank You Page</TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <H3> Standard RenderKit Thank You Page </H3>
    <hr>

       <f:view>
        <h:form id="standardForm" formName="standardForm" >
 
            <table> 

           <c:if test="${requestScope.standard_chosen != null}">

            <tr> 
              <td>The actuated component 
                  was <h:output_text valueRef="requestScope.standard_chosen"/>. </td>
            </tr>

        </c:if>

          </TR>
            <td><a href="index.html">Back To RenderKit Demo</a></td>
          </TR>

          </table>

        </h:form>
     </f:view>
</HTML>
