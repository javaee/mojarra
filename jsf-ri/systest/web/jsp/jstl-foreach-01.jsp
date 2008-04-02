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

<h:form id="jstlForeach01_form">

  <table border="0" cellspacing="5" cellpadding="5">

    <jsp:useBean scope="session" 
                 class="com.sun.faces.systest.model.ForEachBean"
                 id="forEachBean" />

    <c:forEach items="${forEachBean.arrayProperty}" varStatus="s" var="curVal">
      <c:set var="val" scope="request" value="${curVal}" />
      <c:set var="s" scope="request" value="${s}"/>
      <c:set var="arrayLabel" scope="request" value="arrayLabel${s.index}"/>
      <c:set var="arrayLabelValue" scope="request" 
             value="arrayLabel_${s.index}"/>
      <c:set var="arrayProp" scope="request" value="arrayProp${s.index}"/>
      <tr>
        <td><h:output_text id="#{arrayLabel}" value="#{arrayLabelValue}"/></td>
        <td><h:input_text id="#{arrayProp}" value="#{val}"/></td>
      </tr>
    </c:forEach>

    <tr>
      <td><h:command_button id="submit" type="submit" value="Submit"/></td>
      <td><h:command_button id="reset"  type="reset"  value="Reset"/></td>
    </tr>

  </table>

</h:form>
</body>
</html>
</f:view>
