<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test prependId feature</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Test prependId feature</h1>

<h2>Literal ids with prependId literal</h2>

<f:view>

  <h:form id="form1" prependId="false">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdFalse" value="prependIdFalse" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form id="form2" prependId="true">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdTrue" value="prependIdTrue" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form id="form3">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdUnspecified" value="prependIdUnspecified" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>


<h2>Literal ids with prependId from expression</h2>

  <h:form id="form4" prependId="#{prependIdBean.booleanProperty2}">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdFalse" value="prependIdFalse" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form id="form5" prependId="#{prependIdBean.booleanProperty}">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdTrue" value="prependIdTrue" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form id="form6">

    <h:panelGrid id="grid1" columns="1">

      <h:outputText id="prependIdUnspecified" value="prependIdUnspecified" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>


<h2>Auto-generated ids with prependId literal</h2>

  <h:form prependId="false">

    <h:panelGrid columns="1">

      <h:outputText value="prependIdFalse" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form prependId="true">

    <h:panelGrid columns="1">

      <h:outputText value="prependIdTrue" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form>

    <h:panelGrid columns="1">

      <h:outputText value="prependIdUnspecified" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

<h2>Auto-generated ids with prependId from expression</h2>

  <h:form prependId="#{prependIdBean.booleanProperty2}">

    <h:panelGrid columns="1">

      <h:outputText value="prependIdFalse" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form prependId="#{prependIdBean.booleanProperty}">

    <h:panelGrid columns="1">

      <h:outputText value="prependIdTrue" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

  <h:form >

    <h:panelGrid columns="1">

      <h:outputText value="prependIdUnspecified" />

    </h:panelGrid>

    <h:commandButton value="submit" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
