<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <H3> JSF Tree Test </H3>
    <hr>

<P>This page shouldn't be actually loaded into faces.  It's just to test
the tree navigation mechanism.  Test using getNextStart() inside while
loop.</P>

<faces:UseFaces>

  <faces:Form id="1" model="1" navigationMapId="navMap" >

            <faces:Output_Text id="5"  value="5" />

  </faces:Form>

</faces:UseFaces>

</HTML>
