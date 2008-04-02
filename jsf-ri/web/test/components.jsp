<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>

       <f:view>
       <h:form id="basicForm">

  <TABLE BORDER="1">


      <TR>

	<TD>

	      <h:textentry_input id="userName" text="Default_username" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:textentry_secret id="password" text="Default_password" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandButton id="login" value="Login" 
				    commandName="login"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <h:commandButton id="imageButton" image="duke.gif"
				    commandName="login"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandLink id="link" href="hello.html"
				       value="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:commandLink id="imageLink" href="hello.html"
				       image="duke.gif"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:outputText id="userLabel" text="Output Text" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectManyCheckbox id="validUser" label="Valid User"
					    selected="true" />

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectOneListbox id="appleQuantity">

		<f:selectItem  value="0" label="0"/>
		<f:selectItem  value="1" label="1"/>
		<f:selectItem  value="2" label="2"/>
		<f:selectItem  value="3" label="3"/>
		<f:selectItem  value="4" label="4" selected="true"/>
		<f:selectItem  value="5" label="5"/>
		<f:selectItem  value="6" label="6"/>
		<f:selectItem  value="7" label="7"/>
		<f:selectItem  value="8" label="8"/>
		<f:selectItem  value="9" label="9"/>

	      </h:selectOneListbox>

              Option List

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectOneRadio id="shipType" align="horizontal">

		<f:selectItem value="nextDay" label="Next Day" />
		<f:selectItem value="nextWeek" label="Next Week" 
                                  selected="true" />
		<f:selectItem value="nextMonth" label="Next Month" />

              </h:selectOneRadio>

	</TD>

      </TR>

      <TR>

	<TD>
		<h:selectOneRadio id="verticalRadio" 
                                            align="vertical" border="1" >

  		<f:selectItem value="nextDay" label="Next Day" 
                                  selected="true" />
		<f:selectItem value="nextWeek" label="Next Week"  />
		<f:selectItem value="nextMonth" label="Next Month" />

                </h:selectOneRadio>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:textentry_textarea id="address" text="Hi There" 
                                        rows="10" cols="10"/>

	</TD>

      </TR>

  <TABLE>

</h:form>
</f:view>

    </BODY>
</HTML>
