<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/j2meDemo.tld" prefix="j2medemo" %>
<f:view>
   <f:loadBundle basename="j2meDemo.messages" var="msgs"/>
   <j2medemo:form id="won">
      <j2medemo:output id="result" value="#{msgs.won}"/>   
      <j2medemo:command id="newgame" action="#{game.initialize}"/>
   </j2medemo:form>
</f:view>
