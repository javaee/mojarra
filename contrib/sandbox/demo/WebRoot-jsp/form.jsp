<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<html>
<body>
<f:view>
<h:outputText value="#{true ? 'multipart/form-data' :
'application/x-www-form-urlencoded'}" />

<h:form id="frm1" enctype="#{true ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}"></h:form>
<h:form id="frm2" enctype="#{false ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}"></h:form>
<h:form id="frm3" enctype="hi"></h:form>
<h:form id="frm4" enctype="#{'multipart/form-data'}"></h:form>
<h:inputSecret value="#{somebean.somevalue}" />
</f:view>
</body>
</html>
