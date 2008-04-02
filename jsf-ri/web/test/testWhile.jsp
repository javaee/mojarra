<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <H3> JSF Tree Test </H3>
    <hr>

<P>This page shouldn't be actually loaded into faces.  It's just to test
the tree navigation mechanism.  Test using getNextStart() inside while
loop.</P>

  <f:view>
  <h:form id="1" >

            <h:outputText id="5"  text="5" />
  </h:form>
</f:view>

</HTML>
