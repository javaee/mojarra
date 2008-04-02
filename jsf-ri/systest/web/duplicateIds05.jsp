<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
<%@ taglib uri="/WEB-INF/taglib.tld"           prefix="s" %>

<f:view>
  <s:children_body>
    <h:outputText id="id1" value="output"/>
    <h:outputText id="id2" value="output2"/>
    <h:outputText value="output3"/>
  </s:children_body>
</f:view>
