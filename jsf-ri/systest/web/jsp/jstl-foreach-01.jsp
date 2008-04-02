<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-foreach-01</title>
</head>
<body>

<%-- Appears we do not need this (good!!!)
<jsp:useBean id="forEachBean" scope="request"
          class="com.sun.faces.systest.model.ForEachBean"/>
--%>

<h:form id="jstlForeach01_form" formName="formName">

  <table border="0" cellspacing="5" cellpadding="5">

    <c:forEach var="i" begin="0" end="4">
      <tr>
        <td><h:output_text id="arrayLabel${i}" value="arrayLabel_${i}"/></td>
<%--
     PENDING(craigmcc) - The valueRef expression below works
     but is not intuitive
--%>
        <td><h:input_text id="arrayProp${i}"
                    valueRef="forEachBean.arrayProperty[${i}]"/></td>
<%--
     PENDING(craigmcc) - The valueRef expression below returns
     an error:  "String is empty" but would be really nice if
     this actually worked:
--%>
<%--
                    valueRef="${forEachBean.arrayProperty[i]}"/></td>
--%>
      </tr>
    </c:forEach>

  </table>

</h:form>
</body>
</html>
</f:view>
