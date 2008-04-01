<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <%@ taglib uri="WEB-INF/lib/basic.tld" prefix="basic" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
       <fmt:setBundle
	    basename="basic.Resources"
	    scope="session" var="basicBundle"/>

	<jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />

       <faces:usefaces>  
        <faces:form id="basicForm" formName="basicForm" >

            <table> 

      <TR>

        <TD>OutputDateTime: 
        </TD>

	<TD><faces:output_datetime id="date3" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
	</TD>

      </TR>

            <tr>

             <td><FONT COLOR="RED">Custom</FONT></TD>

             <td><basic:textentry_input_valuechange id="custom">

                   <faces:eventhandler className="basic.EventHandler"/>

                 </basic:textentry_input_valuechange>
                 

            </tr>

            <tr> 
              <td> <faces:output_text id="userLabel" key="usernameLabel" 
                                      bundle="${basicBundle}"/> </td>
              <td> 

                   <faces:input_text id="userName" modelReference="${LoginBean.userName}"
                       value="joebob" >

		     <faces:validator className="javax.faces.validator.LengthValidator"/>
		     <faces:validator className="javax.faces.validator.RequiredValidator"/>
		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MINIMUM"
                         value="6"/>

		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MAXIMUM"
                         value="10"/>
                   </faces:input_text>

              </td>

            <td> <faces:output_errors id="err1" compoundId="/basicForm/userName" /> </td>

            </tr>

            <tr>
               <td> <faces:output_text id="pwdLabel" key="passwordLabel" 
                                      bundle="${basicBundle}" /> </td>

               <td> 

                    <faces:input_secret id="password"> 

		     <faces:validator className="javax.faces.validator.LengthValidator"/>
		     <faces:validator className="javax.faces.validator.RequiredValidator"/>
		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MINIMUM"
                         value="6"/>

		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MAXIMUM"
                         value="10"/>

                    </faces:input_secret>

               </td>

            <td> <faces:output_errors id="err2" compoundId="/basicForm/password"/> </td>

             </tr>

            <tr>
               <td> <faces:output_text id="doubleLabel" 
                            key="doubleLabel" bundle="${basicBundle}" /> </td>

               <td> 

                    <faces:input_number id="double">

		     <faces:validator 
                       className="javax.faces.validator.DoubleRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.DoubleRangeValidator.MINIMUM"
                         value="3.2"/>

		     <faces:attribute 
                         name="javax.faces.validator.DoubleRangeValidator.MAXIMUM"
                         value="3.9"/>

                    </faces:input_number>

               </td>

-              <td> <faces:output_errors id="err3" compoundId="/basicForm/double"/> </td>


             </tr>

            <tr>
               <td> <faces:output_text id="intLabel" 
                        key="intLabel" bundle="${basicBundle}" /> </td>

               <td> 

                    <faces:input_number id="integer">

		     <faces:validator 
                       className="javax.faces.validator.LongRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.LongRangeValidator.MINIMUM"
                         value="1"/>

		     <faces:attribute 
                         name="javax.faces.validator.LongRangeValidator.MAXIMUM"
                         value="10"/>

                    </faces:input_number>

               </td>

              <td> <faces:output_errors id="err4" compoundId="/basicForm/integer"/> </td>


             </tr>

            <tr>
               <td> <faces:output_text id="stringLength" key="characterLabel" 
                                      bundle="${basicBundle}" /> </td>

               <td> 

                    <faces:input_text id="string" size="1"
                                  modelReference="${LoginBean.string}"> 

		     <faces:validator 
                       className="javax.faces.validator.StringRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.StringRangeValidator.MINIMUM"
                         value="a"/>

		     <faces:attribute 
                         name="javax.faces.validator.StringRangeValidator.MAXIMUM"
                         value="f"/>

                    </faces:input_text>

               </td>

              <td> <faces:output_errors id="err5" compoundId="/basicForm/string"/> </td>


             </tr>

       
             <tr>
                <td> <faces:output_text id="addrLabel" key="addressLabel" 
                                      bundle="${basicBundle}" /> </td>
                <td> <faces:input_textarea rows="10" cols="10" 
                                               id="address" /> </td>
             </tr>

              <tr>
             <td> <faces:selectboolean_checkbox id="validUser"
                       modelReference="${LoginBean.validUser}"/> 
                  <faces:output_text id="checkLabel" 
                                     key="validUserLabel"
                                                bundle="${basicBundle}"/>

                  </td>
             </tr>


          </table>

	  <TABLE>

      <TR>

	<TD>

	      <faces:command_hyperlink id="link" 
                  target="/faces/Basic_Thanks.jsp"
                  commandName="thankyoulink" label="Link to Thank You page"
                                                key="linkLabel" 
                                                bundle="${basicBundle}"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <faces:command_hyperlink id="imageLink" 
                      target="/basic/index.html" image="/basic/duke.gif"/>

	</TD>

      </TR>

      <TR>

         <TD>

	      <faces:selectone_optionlist id="OptionList" 
                             modelReference="${LoginBean.currentOption}">

		<faces:selectitems id="optionListOptions"
                                   modelReference="${LoginBean.options}"/>

	      </faces:selectone_optionlist>
                <faces:output_text id="optionLabel" 
                   value="OptionList with Kinds of Beans from Model Object" />

	</TD>

      </TR>

      <TR>

      
	<TD>

	      <faces:selectone_optionlist id="appleQuantity" size="6"
                     title="Select Quantity" 
                     accesskey="N" tabindex="20" >

		<faces:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
		<faces:selectitem  itemValue="1" itemLabel="1" title="One" />
		<faces:selectitem  itemValue="2" itemLabel="2" title="Two" />
		<faces:selectitem  itemValue="3" itemLabel="3" title="Three" />
		<faces:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
		<faces:selectitem  itemValue="5" itemLabel="5" title="Five" />
		<faces:selectitem  itemValue="6" itemLabel="6" title="Six" />
		<faces:selectitem  itemValue="7" itemLabel="7" title="Seven" />
		<faces:selectitem  itemValue="8" itemLabel="8" title="Eight" />
		<faces:selectitem  itemValue="9" itemLabel="9" title="nine" />

	      </faces:selectone_optionlist>

               <faces:output_text id="quantityLabel" value="Option list from JSP" />
	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="shipType" layout="LINE_DIRECTION" >

		<faces:selectitem itemValue="nextDay" itemLabel="Next Day" 
                      tabindex="30" title="Next day shipment"/>
		<faces:selectitem itemValue="nextWeek" itemLabel="Next Week" title="Next week shipment"
                                  tabindex="40" selected="true" />
		<faces:selectitem itemValue="nextMonth" itemLabel="Next Month" 
                        tabindex="50" title="Next month shipment"/>
 
              </faces:selectone_radiogroup>
                <faces:output_text id="shipmentLabel" value="Radio laid out horizontally" />


	</TD>

      </TR>

      <TR>

	<TD>
		<faces:selectone_radiogroup id="verticalRadio" layout="PAGE_DIRECTION" border="1" >

  		<faces:selectitem itemValue="nextDay" itemLabel="Next Day" 
                                  selected="true" />
		<faces:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
		<faces:selectitem itemValue="nextMonth" itemLabel="Next Month" />

                </faces:selectone_radiogroup>

                <faces:output_text id="verticalLabel" value="Radio laid out vertically" />
	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="radioFromModel" 
                       modelReference="${LoginBean.currentOption}"
                       layout="LINE_DIRECTION" >

		<faces:selectitems id="optionListOptions"
                                   title="options come from model" 
                                   modelReference="${LoginBean.options}"/>

              </faces:selectone_radiogroup>
              <faces:output_text id="modelLabel" value="Above options come from model " />

	</TD>

      </TR>

      <TR>
        <TD><faces:output_text id="graphicLabel" value="Graphic Image: " /></TD>
        <TD><faces:graphic_image id="graphicImage" url="/duke.gif" /></TD>
      </TR>
      <TR>
        <TD><faces:output_text id="graphicLabel1" value="Graphic Image (url From Resource Bundle): " /></TD>
        <TD><faces:graphic_image id="graphicImage1" key="imageurl"
                bundle="${basicBundle}" /></TD>
      </TR>

      <TR>

        <TD>

             <faces:command_button id="pushButton" type="button" 
                     commandName="push" disabled = "true" >
                 <faces:output_text id="buttonLabel" value="This is a push button " />       
                 <faces:graphic_image id="buttonImage" url="/duke.gif" />
             </faces:command_button>
        </TD>

        <TD>

             <faces:command_button id="resetButton"  title="Click to reset form"
                                   commandName="reset" type="reset" >
                <faces:output_text id="resetLabel" key="resetButton" 
                                bundle="${basicBundle}" />
             </faces:command_button>

        </TD>

      </TR>
      
      <TR>

	<TD>

            <faces:command_button id="login" type="submit" 
                     commandName="login" >
                 <faces:output_text id="submitLabel" key="loginButton" bundle="${basicBundle}" />       
             </faces:command_button>

	</TD>

      </TR>


      <TR>

	<TD>
           <faces:command_button id="imageOnlyButton" type="submit" 
                     commandName="login"  >
                 <faces:graphic_image id="buttonImage1" url="/duke.gif" />
             </faces:command_button>

               Image button that does the same thing

	</TD>

      </TR>
				<TR>
					<TD>Multi-select menu:</TD>
					<TD><faces:selectmany_menu id="ManyApples" size="3">
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
				</TR>
				<TR>
					<TD>Multi-select menu with model:</TD>
					<TD><faces:selectmany_menu id="menumodel" size="3">
						<faces:selectitems id="menumodelitems"
							modelReference="${LoginBean.options}" />
					</faces:selectmany_menu></TD>
				</TR>
				<TR>
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
				</TR>
				<TR>
					<TD>Multi-select listbox with model:</TD>
					<TD><faces:selectmany_listbox id="listmodel"
						modelReference="${LoginBean.currentOption}">
						<faces:selectitems id="listmodelitems"
							modelReference="${LoginBean.options}" />
					</faces:selectmany_listbox></TD>
				</TR>
				<TR>
					<TD>Multi-select checkbox:</TD>
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
				</TR>
				<TR>
					<TD>Multi-select checkbox with model:</TD>
					<TD><faces:selectmany_checkboxlist id="checklistmodel"
						modelReference="${LoginBean.currentOption}">
						<faces:selectitems id="checklistmodelitems"
							modelReference="${LoginBean.options}" />
					</faces:selectmany_checkboxlist></TD>
				</TR>
                                <TR>
                                    <TD>Single-select menu: </TD>
                                    <TD><faces:selectone_menu id="players" size="3">
                                            <faces:selectitem itemValue="99"
                                                itemLabel="Wayne Gretzky" /> 
                                            <faces:selectitem itemValue="4"
                                                itemLabel="Bobby Orr" /> 
                                            <faces:selectitem itemValue="9"
                                                itemLabel="Gordie Howe" /> 
                                            <faces:selectitem itemValue="2"
                                                itemLabel="Brad Park" /> 
                                        </faces:selectone_menu> </TD>
                                </TR>
                                   



	  </TABLE>

        </faces:form>
       </faces:usefaces>
</HTML>
