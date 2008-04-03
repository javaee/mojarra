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

<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<%@ page import="java.util.*" %>

<% ArrayList list = new ArrayList();
   list.add("output1"); list.add("output2"); list.add("output3");
   pageContext.setAttribute("output", list, pageContext.SESSION_SCOPE);

   ArrayList inputWithIdList = new ArrayList();
   inputWithIdList.add("inputid1"); inputWithIdList.add("inputid2"); inputWithIdList.add("inputid3");
   pageContext.setAttribute("inputWithIdList", inputWithIdList, pageContext.SESSION_SCOPE);

   
   HashMap map1 = new HashMap();
   map1.put("inputText1", "input1");
   map1.put("inputText2", "input2");
   map1.put("inputText3", "input3");
   pageContext.setAttribute("input", map1, pageContext.SESSION_SCOPE);

%>
<f:view>
<html>
<head>
<title>c:forEach Test</title>
</head>
<body>

<br>
<h:form id="myform" >
<br>
   <h:outputText value ="Test c:ForEach with outputText and no id" />
   <br> 
   <c:forEach var="item" items="#{output}">
       <h:outputText value="#{item}"/> <br>
   </c:forEach>
   <br> <br>
   
   <!-- inputText without "id" -->
   <h:outputText value ="Test c:ForEach with inputText and no id" />
   <br> 
   <c:forEach var="item" items="#{input}">
       <h:inputText value="#{item}" valueChangeListener="#{forEachBean1.valueChange1}"/> <br>
   </c:forEach>
   
   <c:forEach var="item" items="#{forEachBean1.newList1}">
       <h:inputText value="#{item}" /> <br>
   </c:forEach>
   
   
   <br> <br>
   <h:outputText value ="Test c:ForEach with inputText and with id" />
   <br> 
   <!-- inputText with "id" -->
   <c:forEach var="itemWithId" items="#{inputWithIdList}">
       <h:inputText id ="inputId1" value="#{itemWithId}" valueChangeListener="#{forEachBean1.valueChange2}"/>
       <br>
   </c:forEach>
   
   <c:forEach var="item" items="#{forEachBean1.newList2}">
       <h:inputText id="inputId2" value="#{item}" /> <br>
   </c:forEach>
       
   <br> <br>

<h:commandButton id="submit" value="Submit" />
</h:form>
</body>
</html>
</f:view>
