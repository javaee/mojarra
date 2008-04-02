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

<h:form id="jstlForeach01_form" formName="formName">

  <table border="0" cellspacing="5" cellpadding="5">

    <c:forEach var="i" begin="0" end="4">
      <tr>
        <td><h:output_text id="arrayLabel${i}" value="arrayLabel_${i}"/></td>
        <td><h:input_text id="arrayProp${i}"
                    valueRef="forEachBean.arrayProperty[${i}]"/></td>
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
