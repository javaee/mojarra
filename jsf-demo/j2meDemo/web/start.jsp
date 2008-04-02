<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/j2meDemo.tld" prefix="j2medemo" %>
<f:view>
   <j2medemo:form id="start">
      <j2medemo:board id="board" value="#{game.board}" />
      <j2medemo:selectOne id="boardsize"
            binding="#{setupform.boardSize}"
            value="#{setupform.size}">
         <f:selectItems value="#{game.board.boardSizes}"/>
      </j2medemo:selectOne>
      <j2medemo:command id="submit" action="#{setupform.submit}"/>
   </j2medemo:form>
</f:view>
