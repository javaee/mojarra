<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>

<f:view>
<html>
<head>
  <title>
    Combat Results Page
  </title>
  <link rel="stylesheet" type="text/css"
    href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<body>
  <h2>Combat Results Page</h2>

  <p>The results page determines the winner of the two selections
     based on a magically simple algorithm</p>

  <p>If 
     <i><h:outputText value="#{modelBean.firstSelection}" /></i>
     and 
     <i><h:outputText value="#{modelBean.secondSelection}" /></i>
     were to wage a magical war, the winner would be: 
     <b><h:outputText value="#{modelBean.combatWinner}" /></b>
     !
  </p>

  <h:form>
    <h:commandButton value="Start Over" action="startOver" />
    <jsp:include page="wizard-buttons.jsp"/>
  </h:form>


</body>
</html>
</f:view>
