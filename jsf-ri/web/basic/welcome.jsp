<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
      <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />

      <f:usefaces>
        <faces:form id="welcomeForm" formName="welcomeForm" >

             <p>
              <faces:output_message id="userMsg" value="Welcome {0}.  Thanks for trying the {1} application." >
                  <f:parameter id="param1" 
                      modelReference="LoginBean.userName"/>
                  <f:parameter id="param2" 
                      value="Faces Basic"/>
              </faces:output_message>

<P>
              <faces:command_button id="back" commandName="back" type="submit">
                 <faces:output_text id="backlabel" value="Back"/>

             </faces:command_button>
</P>

        </faces:form>
     </f:usefaces>
</HTML>
