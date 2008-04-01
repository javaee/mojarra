<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <BODY>
        <H3> JSF Basic Components Test Page </H3>
<faces:usefaces>
<faces:form formName="basicForm" id="basicForm">
  <TABLE BORDER="1">


      <TR>

	<TD>

	      <faces:textentry_input id="userName" text="Default_username" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:textentry_secret id="password" text="Default_password" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:command_button id="login" label="Login" 
				    commandName="login"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <faces:command_hyperlink id="link" target="hello.html"
				       label="link text"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <faces:output_text id="userLabel" text="Output Text" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:selectboolean_checkbox id="validUser" label="Valid User"
					    selected="true" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:textentry_textarea id="address" text="Hi There" 
                                        rows="10" cols="10"/>

	</TD>

      </TR>

  <TABLE>

</faces:form>
</faces:usefaces>
    </BODY>
</HTML>
