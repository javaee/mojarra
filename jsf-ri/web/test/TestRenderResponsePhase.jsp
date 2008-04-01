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

	      <faces:selectboolean_checkbox id="validUser" 
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
                              dateStyle="LONG" />
	</TD>

      </TR>


      <TR>

        <TD>Disabled Date: 
        </TD>

	<TD><faces:input_date id="date2" value="Thu, Jul 11, 1996"
                        dateStyle="LONG" disabled="true"
                        size="3" maxlength="20" tabindex="1" accesskey="D"/>
	</TD>

      </TR>

      <TR>

        <TD>DateTime: 
        </TD>

	<TD><faces:input_datetime id="date3" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
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
                   <faces:output_number id="testPercent" numberStyle="PERCENT"
                        value="45%"/>
              </td>
      </tr>

      <TR>

        <TD>OutputDate: 
        </TD>

	<TD><faces:output_date id="date4" value="Wed, Jul 10, 1996"
                              dateStyle="LONG" />
	</TD>

      </TR>

      <TR>

        <TD>OutputDateTime: 
        </TD>

	<TD><faces:output_datetime id="date5" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
	</TD>

      </TR>

      <TR>

        <TD>InputTime: 
        </TD>

	<TD><faces:input_time id="date6" 
                          value="12:31:31 PM"
                          timeStyle="MEDIUM" />
	</TD>

      </TR>

      <TR>

        <TD>OutputTime: 
        </TD>

	<TD><faces:output_time id="date7" 
                          value="12:31:31 PM"
                          timeStyle="MEDIUM" />
	</TD>

      </TR>

           <tr>

             <td>

               <faces:output_text id="input_date1_label" 
                     value="input_date MEDIUM readonly with PAGE_START label"/>

             </td>


             <td>

               <faces:input_date id="input_date1" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date MEDIUM readonly"
                                 accesskey="D" labelAlign="PAGE_START"
                               title="input_date MEDIUM readonly">

                                <faces:output_text 
                                      id="input_date1_label_page_start" 
                           value="input_date MEDIUM readonly"/>

               </faces:input_date>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date2_label" 
                     value="input_date MEDIUM with PAGE_END label"/>

             </td>


             <td>

               <faces:input_date id="input_date2" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 alt="input_date MEDIUM"
                                 labelAlign="PAGE_END"
                                  title="input_date MEDIUM">

                                <faces:output_text 
                                      id="input_date2_label_page_end" 
                           value="input_date MEDIUM"/>

               </faces:input_date>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date3_label" 
                     value="input_date SHORT with LINE_START label"/>

             </td>


             <td>

               <faces:input_date id="input_date3" dateStyle="SHORT"
                                 value="01/12/1952" 
                                 alt="input_date SHORT"
                                 labelAlign="LINE_START"
                                  title="input_date SHORT">

                                <faces:output_text 
                                      id="input_date3_label_line_start" 
                           value="input_date SHORT"/>

               </faces:input_date>


             </td>

            </tr>

           <tr>

             <td>

               <faces:output_text id="input_date4_label" 
                     value="input_date LONG with LINE_END label"/>

             </td>


             <td>

               <faces:input_date id="input_date4" dateStyle="LONG" 
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date LONG with LINE_END label"
                                 accesskey="d"
                               title="input_date LONG with LINE_END label"
                                 labelAlign="LINE_END">

                                <faces:output_text 
                                      id="input_date4_label_line_end" 
                           value="input_date LONG"/>

               </faces:input_date>


             </td>

            </tr>




  <TABLE>

</faces:form>
</faces:usefaces>
    </BODY>
</HTML>
