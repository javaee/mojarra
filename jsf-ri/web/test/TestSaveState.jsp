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

	      <faces:command_hyperlink id="link" target="hello.html"
                           commandClass="hyperlinkClass"
				       label="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

            <faces:selectboolean_checkbox id="validUser" 
                   selectbooleanClass="selectbooleanClass"/>
	</TD>

      </TR>

      <TR>

	<TD>

	     <faces:selectone_listbox id="appleQuantity" size="6"
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <faces:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <faces:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <faces:selectitem  itemValue="9" itemLabel="9" title="nine" />

              </faces:selectone_listbox>

	</TD>

      </TR>

					<TD><faces:selectmany_menu id="ManyApples">
						<faces:selectitem itemValue="4" itemLabel="four" selected="true" />
						<faces:selectitem itemValue="6" itemLabel="six" />
						<faces:selectitem itemValue="7" itemLabel="seven" selected="true" />
					</faces:selectmany_menu></TD>

</tr>

  </TABLE>

</faces:form>
</faces:usefaces>
    </BODY>
</HTML>
