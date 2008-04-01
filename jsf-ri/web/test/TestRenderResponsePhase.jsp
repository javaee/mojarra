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

            <faces:command_button id="pushButton" type="button"
                     commandName="push" disabled = "true" >
                 <faces:output_text id="buttonLabel" value="This is a push button " />
                 <faces:graphic_image id="buttonImage" url="/duke.gif" />
             </faces:command_button>
	</TD>

      </TR>


      <TR>

	<TD>

            <faces:command_button id="imageOnlyButton" type="submit"
                     commandName="login"  >
                 <faces:graphic_image id="buttonImage1" url="/duke.gif" />
             </faces:command_button>
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

            <faces:selectboolean_checkbox id="validUser" />
	</TD>

      </TR>

      <TR>

	<TD>

	     <faces:selectone_optionlist id="appleQuantity" size="6"
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <faces:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <faces:selectitem  itemValue="1" itemLabel="1" title="One"/>
                <faces:selectitem  itemValue="2" itemLabel="2" title="Two" />
                <faces:selectitem  itemValue="3" itemLabel="3" title="Three" />
                <faces:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <faces:selectitem  itemValue="5" itemLabel="5" title="Five" />
                <faces:selectitem  itemValue="6" itemLabel="6" title="Six" />
                <faces:selectitem  itemValue="7" itemLabel="7" title="Seven" />
                <faces:selectitem  itemValue="8" itemLabel="8" title="Eight" />
                <faces:selectitem  itemValue="9" itemLabel="9" title="nine" />

              </faces:selectone_optionlist>

	</TD>

      </TR>

      <TR>

	<TD>

	    <faces:selectone_radiogroup id="shipType" layout="LINE_DIRECTION">

                <faces:selectitem itemValue="nextDay" itemLabel="Next Day"
                      tabindex="30" title="Next day shipment"/>
                <faces:selectitem itemValue="nextWeek" itemLabel="Next Week" title="Next week shipment"
                                  tabindex="40" selected="true" />
                <faces:selectitem itemValue="nextMonth" itemLabel="Next Month"
                        tabindex="50" title="Next month shipment"/>

              </faces:selectone_radiogroup>

	</TD>

      </TR>

      <TR>

	<TD>
            <faces:selectone_radiogroup id="verticalRadio" 
                                            layout="PAGE_DIRECTION" border="1" >

                <faces:selectitem itemValue="nextDay" itemLabel="Next Day"
                                  selected="true" />
                <faces:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
                <faces:selectitem itemValue="nextMonth" itemLabel="Next Month" />

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
          <td>
                   <faces:input_number id="testPattern" formatPattern="####"
                        value="9999.98765" size="3" maxlength="20" 
                        tabindex="2" accesskey="D"/>
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

               <faces:input_date id="input_date1" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date MEDIUM readonly"
                                 accesskey="D" 
                               title="input_date MEDIUM readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:input_date id="input_date2" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 alt="input_date MEDIUM"
                                  title="input_date MEDIUM"/>

             </td>

            </tr>

           <tr>

             <td>

               <faces:input_date id="input_date3" dateStyle="SHORT"
                                 value="01/12/1952" 
                                 alt="input_date SHORT"
                                  title="input_date SHORT"/>


             </td>

            </tr>

           <tr>

             <td>

               <faces:input_date id="input_date4" dateStyle="LONG" 
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date LONG"
                                 accesskey="d"
                               title="input_date LONG"/>


             </td>

            </tr>

<tr>
					<TD>Multi-select menu:</TD>
					<TD><faces:selectmany_menu id="ManyApples">
						<faces:selectitem itemValue="0" itemLabel="zero" />
						<faces:selectitem itemValue="1" itemLabel="one" />
						<faces:selectitem itemValue="2" itemLabel="two" />
						<faces:selectitem itemValue="3" itemLabel="three" />
						<faces:selectitem itemValue="4" itemLabel="four" selected="true" />
						<faces:selectitem itemValue="5" itemLabel="five" />
						<faces:selectitem itemValue="6" itemLabel="six" />
						<faces:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<faces:selectitem itemValue="8" itemLabel="eight" />
						<faces:selectitem itemValue="9" itemLabel="nine" />
					</faces:selectmany_menu></TD>

</tr>

<tr>
					<TD>Multi-select listbox:</TD>
					<TD><faces:selectmany_listbox id="ManyApples2">
						<faces:selectitem itemValue="0" itemLabel="zero" />
						<faces:selectitem itemValue="1" itemLabel="one" />
						<faces:selectitem itemValue="2" itemLabel="two" />
						<faces:selectitem itemValue="3" itemLabel="three" />
						<faces:selectitem itemValue="4" itemLabel="four" selected="true" />
						<faces:selectitem itemValue="5" itemLabel="five" />
						<faces:selectitem itemValue="6" itemLabel="six" />
						<faces:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<faces:selectitem itemValue="8" itemLabel="eight" />
						<faces:selectitem itemValue="9" itemLabel="nine" />
					</faces:selectmany_listbox></TD>
</tr>

<tr>
					<TD><faces:selectmany_checkboxlist id="ManyApples3">
						<faces:selectitem itemValue="0" itemLabel="zero" />
						<faces:selectitem itemValue="1" itemLabel="one" />
						<faces:selectitem itemValue="2" itemLabel="two" />
						<faces:selectitem itemValue="3" itemLabel="three" />
						<faces:selectitem itemValue="4" itemLabel="four" selected="true" />
						<faces:selectitem itemValue="5" itemLabel="five" />
						<faces:selectitem itemValue="6" itemLabel="six" />
						<faces:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<faces:selectitem itemValue="8" itemLabel="eight" />
						<faces:selectitem itemValue="9" itemLabel="nine" />
					</faces:selectmany_checkboxlist></TD>
</tr>


  <TABLE>

</faces:form>
</faces:usefaces>
    </BODY>
</HTML>
