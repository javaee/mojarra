<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib.tld" %>

<f:view>
<html>
<head>
<title>jstl-if-02</title>
</head>
<body>
<h:output_text value="[First]"/>
<c:if test="${param.component}">
  <s:facets id="comp" value="Second">
    <c:if test="${param.header}">
      <f:facet name="header">
        <h:output_text id="head" value="Header"/>
      </f:facet>
    </c:if>
    <c:if test="${param.footer}">
      <f:facet name="footer">
        <h:output_text id="foot" value="Footer"/>
      </f:facet>
    </c:if>
  </s:facets>
</c:if>
<h:output_text value="[Third]"/>
</body>
</html>
</f:view>
