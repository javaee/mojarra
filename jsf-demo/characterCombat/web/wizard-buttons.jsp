<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:subview id="wizard-buttons">

  <h:panelGrid columns="2">

    <h:commandButton value="< Back" action="back" 
                     disabled="#{wizardButtons.hasBack}" />

    <h:commandButton value="Next >" action="next" 
                     disabled="#{wizardButtons.hasNext}"/>

  </h:panelGrid>

</f:subview>
