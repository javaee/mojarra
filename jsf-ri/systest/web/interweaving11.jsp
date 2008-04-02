<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    java.util.ArrayList list = new java.util.ArrayList();
    list.add("one");
    list.add("two");
    list.add("three");
    pageContext.setAttribute("list", list, PageContext.REQUEST_SCOPE);
%>
<html>
  <head>
      <title>interweaving11</title>   
  </head>
  <body>
    <f:view>
        <h:form>
            <c:forEach items="#{list}" var="item">
                <h:commandLink>
                   <f:param name="param" value="value"/>
                   <h:outputText value="#{item}"/>
                </h:commandLink>
                </br>
            </c:forEach>
        </h:form>
    </f:view>
  </body>
</html>

