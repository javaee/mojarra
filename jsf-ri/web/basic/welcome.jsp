<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
      <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />


      <f:use_faces>
        <h:form id="welcomeForm" formName="welcomeForm" >

             <p>


              <h:output_message id="userMsg" value="Welcome {0}.  Thanks for trying the {1} application." >
                  <f:parameter valueRef="LoginBean.userName"/>

                  <f:parameter value="Faces Basic"/>
              </h:output_message>

<P>
              <h:command_button id="back" commandName="back" type="submit"
                  label="Back">
             </h:command_button>
</P>
      </h:form>
     </f:use_faces>
</HTML>
