<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>


<f:view>

  <p>actionListener must have either a type or binding attribute.</p>
  <h:commandButton value="submit" >
      <f:actionListener /> 
  </h:commandButton>

  <p>valueChangeListener must have either a type or binding attribute.</p>
  <h:inputText >
      <f:valueChangeListener /> 
  </h:inputText>

  <p>validator must have either a validatorId or binding attribute.</p>
  <h:inputText >
      <f:validator />
  </h:inputText>

  <p>converter must have either a converterId or binding attribute.</p>
  <h:inputText >
      <f:converter />
  </h:inputText>

</f:view>

</body>
</head>
</html>
