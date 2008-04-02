<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>

<f:view>

  <f:loadBundle basename="com.sun.faces.TestMessages" var="testMessages" />
<h:form id="basicForm" title="basicForm" styleClass="formClass"
      accept="html,wml" >

  <TABLE BORDER="1">

      <tr>
         <td>
                 <h:panel_grid id="logonPanel1" columns="2"
                    border="1" cellpadding="3" cellspacing="3"
                     summary="Grid with hardcoded data"
                     title="Grid with hardcoded data" >

                     <h:panel_group id="formHeader2">
                        <h:output_text id="A2" value="Logon&nbsp;"/>
                        <h:output_text id="B2" value="Form"/>
                     </h:panel_group>

                   
                    <h:output_text id="text1" value="Username:"/>
                   
                    <h:input_text id="username1" styleClass="inputClass" value="JavaServerFaces" />

                    <h:input_text id="username2" styleClass="inputClass" value="JavaServerFaces" disabled="true" />

                    <h:output_text id="text2" styleClass="outputClass" value="Password:"/>

                    <h:input_secret styleClass="secretClass" id="password1" />

                    <h:command_button id="submit1" type="SUBMIT"
                        styleClass="commandClass" 
                        value="Login" >
                    </h:command_button>

                    <h:command_button id="reset1" type="RESET" 
                        value="Reset">
                    </h:command_button>

                </h:panel_grid>
             </td>
           </tr>

      <TR>

	<TD>

            <h:command_button id="pushButton" type="button"
                 disabled = "true" image="duke.gif">
            </h:command_button>
	</TD>

      </TR>


      <TR>

	<TD>

            <h:command_button id="imageOnlyButton" type="submit"
                 image="duke.gif" rendered="true"> 
             </h:command_button>
	</TD>

      </TR>

      <TR>

	<TD>

	      <h:command_link id="link" styleClass="hyperlinkClass">
                <f:verbatim>link text with localized output</f:verbatim> 
                <h:output_text value="#{testMessages.euroMessage}" />
              </h:command_link>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:command_link id="imageLink">
                <h:graphic_image url="duke.gif"/>
              </h:command_link>

	</TD>

        <TD> 
            <h:graphic_image id="graphicImage" url="/duke.gif" ismap="true" usemap="#map1" /> 
        </TD>

      </TR>

      <TR>
        <TD>
            <h:command_link id="commandLink" styleClass="hyperlinkClass"><f:verbatim>link text</f:verbatim>
            </h:command_link>
       </TD>
      </TR>

      <TR>
        <TD>
            <h:command_link id="commandParamLink" styleClass="hyperlinkClass">
              <f:verbatim>link text</f:verbatim>
              <f:parameter id="hlParam1" name="name" value="horwat"/>
              <f:parameter id="hlParam2" name="value" value="password"/>
            </h:command_link>
        </TD>
      </TR>

      <TR>
        <TD>
            <h:command_link id="hrefLink"><f:verbatim escape="false"><img src="duke.gif"></f:verbatim></h:command_link>
        </TD>
      </TR>

      <TR>
        <TD>
            <h:command_link id="hrefParamLink">
              <h:graphic_image url="duke.gif"/>
              <f:parameter id="hlParam3" name="name" value="horwat"/>
              <f:parameter id="hlParam4" name="value" value="password"/>
            </h:command_link>
        </TD>
      </TR>

      <TR>

	<TD>

	      <h:output_link value="test.html" id="output_link" styleClass="hyperlinkClass"><f:verbatim>output link text</f:verbatim></h:output_link>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:output_link value="test.html"id="output_imageLink">
                <h:graphic_image url="duke.gif"/>
              </h:output_link>

	</TD>

        <TD> 
            <h:graphic_image id="output_graphicImage" url="/duke.gif" ismap="true" usemap="#map1" /> 
        </TD>

      </TR>

      <TR>
        <TD>
            <h:output_link value="test.html" id="output_commandLink" styleClass="hyperlinkClass"><f:verbatim>link text</f:verbatim>
            </h:output_link>
       </TD>
      </TR>

      <TR>
        <TD>
            <h:output_link value="test.html" id="output_commandParamLink" styleClass="hyperlinkClass">
              <f:verbatim>link text</f:verbatim>
              <f:parameter id="hlParam7" name="name" value="horwat"/>
              <f:parameter id="hlParam8" name="value" value="password"/>
            </h:output_link>
        </TD>
      </TR>

      <TR>
        <TD>
            <h:output_link value="test.html" id="output_hrefLink"><f:verbatim escape="false"><img src="duke.gif"></f:verbatim></h:output_link>
        </TD>
      </TR>

      <TR>
        <TD>
            <h:output_link value="test.html" id="output_hrefParamLink">
              <h:graphic_image url="duke.gif"/>
              <f:parameter id="hlParam5" name="name" value="horwat"/>
              <f:parameter id="hlParam6" name="value" value="password"/>
            </h:output_link>
        </TD>
      </TR>



      <TR>

	<TD>
              
             <h:output_text id="outputText" value="Output Text"/>
            
              <h:output_text id="testvisible1" value="This should not be visible"
                               rendered="false" />
              <h:graphic_image id="testvisible2" url="/duke.gif" rendered="false"  />

	</TD>

      </TR>

      <TR>

	<TD>
            
            <h:selectboolean_checkbox  id="checkbox1" rendered="true"
                   styleClass="selectbooleanClass"/>
             <h:output_label id="labe11" for="checkbox1">
                        <h:output_text id="outputlabel" value="Checkbox"/>
              </h:output_label>
	</TD>

      </TR>

      <TR>

	<TD>

	     <h:selectone_listbox styleClass="selectoneClass"
                     title="Select Quantity"
                     tabindex="20" >

                <f:selectItem  disabled="true" itemValue="0" itemLabel="0"/>
                <f:selectItem  itemValue="1" itemLabel="1" />
                <f:selectItem  itemValue="2" itemLabel="2" />
                <f:selectItem  itemValue="3" itemLabel="3" />
                <f:selectItem  itemValue="4" itemLabel="4" />
                <f:selectItem  itemValue="5" itemLabel="5" />
                <f:selectItem  itemValue="6" itemLabel="6" />
                <f:selectItem  itemValue="7" itemLabel="7" />
                <f:selectItem  itemValue="8" itemLabel="8" />
                <f:selectItem  itemValue="9" itemLabel="9" />

              </h:selectone_listbox>

	</TD>

      </TR>

      <TR>

	<TD>

	    <h:selectone_radio id="shipType" layout="LINE_DIRECTION" 
                tabindex="3" disabledClass="disabledClass" 
                enabledClass="enabledClass" accesskey="A" 
                styleClass = "styleClass">

                <f:selectItem itemValue="nextDay" itemLabel="Next Day" disabled="true"/>
                <f:selectItem itemValue="nextWeek" itemLabel="Next Week" />
                <f:selectItem itemValue="nextMonth" itemLabel="Next Month" />
                 
              </h:selectone_radio>

	</TD>

      </TR>

      <TR>

	<TD>
            <h:selectone_radio id="verticalRadio" 
                                            layout="PAGE_DIRECTION" border="1" >

                <f:selectItem itemValue="nextDay" itemLabel="Next Day"
                                   disabled="true" />
                <f:selectItem itemValue="nextWeek" itemLabel="Next Week"  
                        disabled="false"/>
                <f:selectItem itemValue="nextMonth" itemLabel="Next Month" />

           </h:selectone_radio>

	</TD>

      </TR>

      <TR>

        <TD>Float: 
        </TD>

	<TD><h:input_text value="3.1415">
                <f:converter converterId="Float"/>
            </h:input_text>
	</TD>

      </TR>



      <TR>

        <TD>Date: 
        </TD>

	<TD><h:input_text value="July 10, 1996"
                              styleClass="inputClass">
                <f:convertDateTime dateStyle="long"/>
            </h:input_text>
	</TD>

      </TR>


      <TR>

        <TD>Disabled Date: 
        </TD>

	<TD><h:input_text id="date2" value="July 11, 1996"
                         readonly="true"
                        size="3" maxlength="20" tabindex="1" accesskey="D">
                <f:convertDateTime dateStyle="long"/>
            </h:input_text>
	</TD>

      </TR>

      <TR>

        <TD>DateTime: 
        </TD>

	<TD><h:input_text id="date3" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM">
                <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
            </h:input_text>
	</TD>

      </TR>


      <tr>
          <td>
            <h:input_text id="testPattern" 
                        value="9999.98765" size="3" maxlength="20" 
                        styleClass="inputClass" 
                        tabindex="2" accesskey="D">
               <f:convertNumber pattern="####"/>
            </h:input_text>
              </td>

      </tr>

      <tr>
          <td> <h:output_text styleClass="outputClass" id="percentLabel" value="OUTPUT-PERCENT" /> </td>
              <td>
                   <h:output_text id="testPercent" value="45%">
                       <f:convertNumber type="number" pattern="#%"/>
                   </h:output_text>
              </td>
      </tr>

      <TR>

        <TD>OutputDate: 
        </TD>

	<TD><h:output_text id="date4" value="July 10, 1996"
                              styleClass="outputClass" >
                <f:convertDateTime dateStyle="long"/>
            </h:output_text>
	</TD>

      </TR>

      <TR>

        <TD>OutputDateTime: 
        </TD>

	<TD><h:output_text id="date5" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM">
                <f:convertDateTime pattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a"/>
            </h:output_text>
	</TD>

      </TR>

      <TR>

        <TD>InputTime: 
        </TD>

	<TD><h:input_text id="date6" 
                          value="12:31:31 PM">
                <f:convertDateTime timeStyle="medium"/>
            </h:input_text>
	</TD>

      </TR>

      <TR>

        <TD>OutputTime: 
        </TD>

	<TD><h:output_text id="date7" 
                          value="12:31:31 PM">
                <f:convertDateTime timeStyle="medium"/>
            </h:output_text>
	</TD>

      </TR>

           <tr>

             <td>

               <h:input_text id="inputDate1" 
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date medium readonly"
                                 accesskey="D" 
                               title="input_date medium readonly">
                   <f:convertDateTime dateStyle="medium"/>
                </h:input_text>


             </td>

            </tr>

           <tr>

             <td>

               <h:input_text id="inputDate2" 
                                 value="Jan 12, 1952" 
                                 alt="input_date medium"
                                  title="input_date medium">
                   <f:convertDateTime dateStyle="medium"/>
                </h:input_text>

             </td>

            </tr>

           <tr>

             <td>

               <h:input_text id="inputDate3" 
                                 value="01/12/1952" 
                                 alt="input_date short"
                                  title="input_date short">
                   
                   <f:convertDateTime dateStyle="short"/>
                </h:input_text>

             </td>

            </tr>

           <tr>

             <td>

               <h:input_text id="inputDate4"  
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date long"
                                 accesskey="d"
                               title="input_date long">
                   <f:convertDateTime dateStyle="long"/>
                </h:input_text>


             </td>

            </tr>

<tr>
					<TD>Multi-select menu:</TD>
					<TD><h:selectmany_menu id="ManyApples" styleClass="selectmanyClass">
						<f:selectItem itemValue="0" itemLabel="zero" />
						<f:selectItem itemValue="1" itemLabel="one" />
						<f:selectItem itemValue="2" itemLabel="two" />
						<f:selectItem itemValue="3" itemLabel="three" />
						<f:selectItem itemValue="4" itemLabel="four" disabled="true"/>
						<f:selectItem itemValue="5" itemLabel="five" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" />
						<f:selectItem itemValue="8" itemLabel="eight" />
						<f:selectItem itemValue="9" itemLabel="nine" />
					</h:selectmany_menu></TD>

</tr>

<tr>
					<TD>Multi-select listbox:</TD>
					<TD><h:selectmany_listbox >
						<f:selectItem itemValue="0" itemLabel="zero" />
						<f:selectItem itemValue="1" itemLabel="one" />
						<f:selectItem itemValue="2" itemLabel="two" />
						<f:selectItem itemValue="3" itemLabel="three" />
						<f:selectItem itemValue="4" itemLabel="four" />
						<f:selectItem itemValue="5" itemLabel="five" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" />
						<f:selectItem itemValue="8" itemLabel="eight" />
						<f:selectItem itemValue="9" itemLabel="nine" />
					</h:selectmany_listbox></TD>
</tr>

<tr>
					<TD><h:selectmany_checkboxlist id="ManyApples3" 
                                                tabindex="3" disabledClass="disabledClass" 
                                                styleClass="styleClass" enabledClass="enabledClass" accesskey="A">>
						<f:selectItem itemValue="0" itemLabel="zero" disabled="true" />
						<f:selectItem itemValue="1" itemLabel="one" />
						<f:selectItem itemValue="2" itemLabel="two" />
						<f:selectItem itemValue="3" itemLabel="three" disabled="false"/>
						<f:selectItem itemValue="4" itemLabel="four" />
						<f:selectItem itemValue="5" itemLabel="five" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" />
						<f:selectItem itemValue="8" itemLabel="eight" />
						<f:selectItem itemValue="9" itemLabel="nine" />
					</h:selectmany_checkboxlist></TD>
</tr>

<tr>
<td>
<h:panel_group style="color:red" styleClass="walleye">
  <f:verbatim>style this text like a red walleye</f:verbatim>
</h:panel_group>
</td>
</tr>

<h:input_hidden value="48%" >
    <f:convertNumber type="number" pattern="#%"/>
</h:input_hidden>

<tr><td>

<f:verbatim escape="true">1. You should see the <i>angle brackets</i> on this
text</f:verbatim>

</td>
</tr>

<tr><td>

<f:verbatim>2. You should see the <i>angle brackets</i> on this
text</f:verbatim>

</td>
</tr>

<tr><td>

<f:verbatim escape="false">3. You should not see the <i>angle
brackets</i> on this text</f:verbatim>

</td>
</tr>

<tr><td>

<f:verbatim escape="#{SimpleBean.trueValue}">4. You should see the <i>angle
brackets</i> on this text</f:verbatim>

</td>
</tr>
<
<tr><td>

<f:verbatim escape="#{SimpleBean.falseValue}">5. You should not see the
<i>angle brackets</i> on this text</f:verbatim>

</td>
</tr>

<tr><td>

<h:output_text escape="true" value="6. You should see the <i>angle
brackets</i> on this text"></h:output_text>

</td>
</tr>

<tr><td>

<h:output_text id="value7" value="7. You should see the <i>angle brackets</i> on
this text"></h:output_text>

</td>
</tr>

<tr><td>

<h:output_text escape="false" value="8. You should not see the <i>angle
brackets</i> on this text"></h:output_text>

</td>
</tr>

<tr><td>

<h:output_text escape="#{SimpleBean.trueValue}" value="9. You should see
the <i>angle brackets</i> on this text"></h:output_text>

</td>
</tr>

<tr><td>

<h:output_text escape="#{SimpleBean.falseValue}" value="10. You should not
see the <i>angle brackets</i> on this text"></h:output_text>

</td>
</tr>

<h:panel_grid>
<f:subview id="subview1" rendered="false">
   <h:output_text value="This should not be rendered" />
</f:subview>
</h:panel_grid>

<h:panel_grid>
<h:panel_group rendered="false">
   <h:output_text value="This should not be rendered" />
</h:panel_group>
</h:panel_grid>

<h:data_table>
<f:subview id="subview2" rendered="false">
   <h:output_text value="This should not be rendered" />
</f:subview>
</h:data_table>


<h:output_link value="test.html" rendered="false">
    <h:graphic_image url="duke.gif"/>
</h:output_link>

<h:command_link rendered="false">
      <h:output_text value="This should not be rendered" />
</h:command_link>
  <TABLE>

</h:form>
</f:view>

    </BODY>
</HTML>
