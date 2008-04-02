<?xml version="1.0"?>

<!DOCTYPE window>

<?xml-stylesheet href="chrome://global/skin/" type="text/css"?>
<?xml-stylesheet href='<%= request.getContextPath() + "/xul.css" %>' type="text/css"?>

<%@ page contentType="application/vnd.mozilla.xul+xml"%>
<%@ taglib uri="http://java.sun.com/jsf/xul" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view renderKitId="XUL">
   <f:loadBundle basename="demo.model.Resources" var="phaseBundle"/>
   <x:form id="form">
      <x:outputLabel boxClass="header" pack="center" styleClass="header" value="#{phaseBundle.renderHeader}"/>

      <x:panelGroup styleClass="groupboxClass" captionLabel="#{phaseBundle.renderCaption}" 
          captionClass="captionClass" >
         <x:panelGroup styleClass="groupboxDetailClass">
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription1}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription2}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription3}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription4}" escape="false" />
            <x:outputText styleClass="info" value="#{phaseBundle.renderDescription5}" escape="false" />
         </x:panelGroup>
      </x:panelGroup>

      <x:panelGrid columns="2">
         <x:commandButton id="back" value="Back" action="xul-back" type="submit"/>
         <x:commandButton id="main" value="Main" action="xul-main" type="submit"/>
      </x:panelGrid>
      
   </x:form>
</f:view>
