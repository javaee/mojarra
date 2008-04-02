<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<HTML>
    <HEAD> <TITLE> JSF Standard RenderKit Demo - Thank You Page</TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <H3> Standard RenderKit Thank You Page </H3>
    <hr>

       <f:view>
        <h:form id="standardForm" >
 
            <table> 

           <c:if test="${model.hasComponent == 'true'}">

            <tr> 
              <td>The actuated component 
                  was <h:outputText id="actuated"
                               value="#{model.whichComponent}"/>. </td>
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
