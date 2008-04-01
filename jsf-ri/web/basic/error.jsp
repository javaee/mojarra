<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
      <faces:usefaces>
        <faces:form id='basicForm' formName='basicForm'>

            <faces:output_text id='hello_label' text='Login Failed' />
             <P></P>
        </faces:form>
       </faces:usefaces>
</HTML>
