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
      <td>
                    <h:input_text id="userName" 
                                     value="JavaServerFaces" >
		     <f:validate_length minimum="6" maximum="10"/>
		     <f:validate_required/>
                   </h:input_text>

              </td>


	<TD>

	      <h:command_link id="link" href="hello.html"
                           styleClass="hyperlinkClass"
				       value="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

            <h:selectboolean_checkbox id="validUser" 
                   styleClass="selectbooleanClass"/>
	</TD>

      </TR>

      <TR>

	<TD>

	     <h:selectone_listbox id="appleQuantity" 
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <h:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <h:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <h:selectitem  itemValue="9" itemLabel="9" title="nine" />

              </h:selectone_listbox>

	</TD>

      </TR>

					<TD><h:selectmany_menu id="ManyApples">
						<h:selectitem itemValue="4" itemLabel="four" selected="true" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" selected="true" />
					</h:selectmany_menu></TD>

</tr>

  </TABLE>

</h:form>
</f:view>

    </BODY>
</HTML>
