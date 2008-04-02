<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='h' %>

    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>


    <H3> JSF Basic Components Test Page </H3>
    <hr>

      <f:use_faces>
        <h:form id='basicForm' formName='basicForm'>

            <h:output_text id='hello_label' text='Login Failed' />
             <P></P>

        </h:form>
       </f:use_faces>

</HTML>
