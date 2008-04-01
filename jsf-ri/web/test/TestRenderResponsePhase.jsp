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

	      <faces:command_button id="login" label="Login" 
				    commandName="login"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <faces:command_button id="imageButton" image="duke.gif"
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

	      <faces:command_hyperlink id="imageLink" target="hello.html"
				       image="duke.gif"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:output_text id="userLabel" value="Output Text" />

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

	      <faces:selectone_optionlist id="appleQuantity">

		<faces:selectitem  value="0" label="0"/>
		<faces:selectitem  value="1" label="1"/>
		<faces:selectitem  value="2" label="2"/>
		<faces:selectitem  value="3" label="3"/>
		<faces:selectitem  value="4" label="4" selected="true"/>
		<faces:selectitem  value="5" label="5"/>
		<faces:selectitem  value="6" label="6"/>
		<faces:selectitem  value="7" label="7"/>
		<faces:selectitem  value="8" label="8"/>
		<faces:selectitem  value="9" label="9"/>

	      </faces:selectone_optionlist>

              Option List

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="shipType" align="horizontal">

		<faces:selectitem value="nextDay" label="Next Day" />
		<faces:selectitem value="nextWeek" label="Next Week" 
                                  selected="true" />
		<faces:selectitem value="nextMonth" label="Next Month" />

              </faces:selectone_radiogroup>

	</TD>

      </TR>

      <TR>

	<TD>
		<faces:selectone_radiogroup id="verticalRadio" 
                                            align="vertical" border="1" >

  		<faces:selectitem value="nextDay" label="Next Day" 
                                  selected="true" />
		<faces:selectitem value="nextWeek" label="Next Week"  />
		<faces:selectitem value="nextMonth" label="Next Month" />

                </faces:selectone_radiogroup>

	</TD>

      </TR>

      <TR>

        <TD>Date: 
        </TD>

	<TD><faces:input_date id="date1" value="Wed, Jul 10, 1996"
                              formatPattern="EEE, MMM d, yyyy" />
	</TD>

      </TR>


      <TR>

        <TD>Disabled Date: 
        </TD>

	<TD><faces:input_date id="date2" value="Thu, Jul 11, 1996"
                        formatPattern="EEE, MMM d, yyyy" disabled="true"
                        size="3" maxlength="20" tabindex="1" accesskey="D"/>
	</TD>

      </TR>

      <tr>
         <td> <faces:output_text id="patternLabel" value="NUMBER-INPUT-PATTERN" /> </td>
             <td>
                   <faces:input_number id="testPattern" formatPattern="####"
                        value="9999.98765" size="3" maxlength="20" tabindex="2" accesskey="D"/>
              </td>

      </tr>

      <tr>
          <td> <faces:output_text id="percentLabel" value="OUTPUT-PERCENT" /> </td>
              <td>
                   <faces:output_number id="testPercent" formatStyle="PERCENT"
                        value="45%"/>
              </td>
      </tr>


  <TABLE>

</faces:form>
</faces:usefaces>
    </BODY>
</HTML>
