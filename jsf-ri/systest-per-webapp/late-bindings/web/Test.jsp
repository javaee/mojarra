
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
  <head><title>Simple jsp page</title></head>
  <body>
  <f:view>
      <h:messages />
      <h:form>
          <h:inputText value="hello">
              <f:converter binding="#{bean.converter}"/>
          </h:inputText>
          <h:inputText value="hello2">
              <f:validator binding="#{bean.validator}"/>
          </h:inputText>          

          <h:commandButton value="#{fn:toUpperCase('click me')}"
                           type="submit"/>

      </h:form>
  </f:view>
  </body>
</html>