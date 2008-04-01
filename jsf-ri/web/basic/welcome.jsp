<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
      <faces:usefaces>
        <faces:form id="welcomeForm" formName="welcomeForm" >

             <p>
              <faces:output_text id="userLabel" value="Welcome!" />  </P>

<P>
	      <faces:command_button id="back" label="Back" 
				    commandName="back"/>
</P>

        </faces:form>
     </faces:usefaces>
</HTML>
