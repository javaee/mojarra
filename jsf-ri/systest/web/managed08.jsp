<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Managed Bean Lifecycle Annotations</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Managed Bean Lifecycle Annotations</h1>

<f:view>

  <h:form id="form">
  
  <h:outputText value="#{noneBean.appendRequestMarker}" />

  <p>requestBean PostConstruct: <h:outputText value="#{requestBean.postConstructCalled}" /></p>
  <p>requestBean PreDestroy: <h:outputText value="#{requestBean.preDestroyCalled}" /></p>

  <p>sessionBean PostConstruct: <h:outputText value="#{sessionBean.postConstructCalled}" /></p>
  <p>sessionBean PreDestroy: <h:outputText value="#{sessionBean.preDestroyCalled}" /></p>

  <p>applicationBean PostConstruct: <h:outputText value="#{applicationBean.postConstructCalled}" /></p>
  <p>applicationBean PreDestroy: <h:outputText value="#{applicationBean.preDestroyCalled}" /></p>
  
  <p>previous request status: <pre><h:outputText value="#{applicationScope.previousRequestStatus}" /></pre></p>
  
  <p><h:commandButton id="removenoneBean" value="remove request bean" action="#{noneBean.removeRequestBean}" /></p>
       
  <p><h:commandButton id="removeSessionBean" value="remove session bean" action="#{noneBean.removeSessionBean}" /></p>

  <p><h:commandButton id="removeApplicationBean" value="remove application bean" action="#{noneBean.removeApplicationBean}" /></p>
       
  <p><h:commandButton id="invalidateSession" value="invalidate session" action="#{noneBean.invalidateSession}" /></p>

  <p><h:commandButton id="clearRequestMap" value="clear request map" action="#{noneBean.clearRequestMap}" /></p>

  <p><h:commandButton id="clearRequestMapTwice" value="clear request map twice" action="#{noneBean.clearRequestMapTwice}" /></p>

  <p><h:commandButton id="clearSessionMap" value="clear session map" action="#{noneBean.clearSessionMap}" /></p>

  <p><h:commandButton id="clearSessionMapTwice" value="clear session map twice" action="#{noneBean.clearSessionMapTwice}" /></p>

  <p><h:commandButton id="clearStatusMessage" value="clear status message" action="#{noneBean.clearStatusMessage}" /></p>
  
  <p><h:commandButton id="reload" value="reload" />
  </h:form>

</f:view>
    <hr>
  </body>
</html>
