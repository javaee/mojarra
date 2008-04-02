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
