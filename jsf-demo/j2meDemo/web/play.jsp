<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/j2meDemo.tld" prefix="j2medemo" %>
<f:view>
   <j2medemo:form id="play">
      <j2medemo:board id="board" value="#{game.board}" />
      <j2medemo:command id="submit" action="#{setupform.play}"/>
   </j2medemo:form>
</f:view>
