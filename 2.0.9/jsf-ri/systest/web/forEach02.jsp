<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title></title>
  </head>

  <body>
    <h1></h1>

<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<f:view>

<h:messages/>
<br>

<h:form               id="form">


   <table border="1" style="list-background"
        summary="Add books from the catalog to your shopping cart.">
       <tr><th>header</th>
       </tr>

       <c:forEach items="#{BooksBean.books}" var="book" varStatus="stat" >
          <tr styleClass="${(stat.index % 2) == 0 ? "list-row-event" : "list-row-odd"}">
               <td style="list-column-left">
               <h:commandLink action="null">
                       
                          <h:outputText id="bookTitle" value="#{book.title}"/>
                       
               </h:commandLink>
               </td>
          </tr>
       </c:forEach>
  </table>

</h:form>

</f:view>



    <hr>
    <address><a href="mailto:ed.burns@sun.com">Edward Burns</a></address>
<!-- Created: Tue Oct  4 13:39:02 EDT 2005 -->
<!-- hhmts start -->
Last modified: Tue Oct  4 14:02:27 EDT 2005
<!-- hhmts end -->
  </body>
</html>
