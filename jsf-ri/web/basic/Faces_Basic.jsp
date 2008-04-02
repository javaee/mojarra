<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <%@ taglib uri="WEB-INF/lib/basic.tld" prefix="basic" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
       <fmt:setBundle
	    basename="basic.Resources"
	    scope="session" var="basicBundle"/>

	<jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />


       <f:use_faces>  
        <h:form formName="basicForm" bundle="basicBundle">

            <table> 

      <TR>

        <TD>OutputDateTime: 
        </TD>

	<TD><h:output_datetime id="date3" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
	</TD>

      </TR>

            <tr>

             <td><FONT COLOR="RED">Custom</FONT></TD>

             <td><h:input_text id="custom">
                     <f:valuechanged_listener type="basic.ValueChange"/>
                 </h:input_text></td>
               
            </tr>

            <tr> 
              <td> <h:output_text id="userLabel" key="usernameLabel" /> </td>
              <td> 
                   <h:input_text id="userName" 
                                     modelReference="LoginBean.userName">
		     <f:validate_length minimum="6" maximum="10"/>
		     <f:validate_required/>
                   </h:input_text>

              </td>

            <td> <h:output_errors id="err1" clientId="userName" /> </td>

            </tr>

            <tr>
               <td> <h:output_text id="pwdLabel" key="passwordLabel" /> </td>

               <td> 
                    <h:input_secret id="password" > 
		     <f:validate_length maximum="10" minimum="6"/>
		     <f:validate_required/>
                    </h:input_secret>

               </td>

            <td> <h:output_errors id="err2" clientId="password"/> </td>

             </tr>

            <tr>
               <td> <h:output_text id="doubleLabel" 
                            key="doubleLabel" /> </td>

               <td> 

                    <h:input_number id="double">
		     <f:validate_doublerange minimum="3.2" maximum="3.9"/>
                    </h:input_number>


               </td>

-              <td> <h:output_errors id="err3" clientId="double"/> </td>


             </tr>

            <tr>
               <td> <h:output_text id="intLabel" 
                        key="intLabel" /> </td>

               <td> 

                    <h:input_number id="integer">

		     <f:validate_longrange minimum="1" maximum="10"/>

                    </h:input_number>

               </td>

              <td> <h:output_errors id="err4" clientId="integer"/> </td>


             </tr>

            <tr>
               <td> <h:output_text id="stringLength" key="characterLabel" /> </td>

               <td> 

                    <h:input_text size="1" id="string" 
                                  modelReference="LoginBean.string"> 

		     <f:validate_stringrange maximum="f" minimum="a"/>
                    </h:input_text>

               </td>

              <td> <h:output_errors id="err5" clientId="string"/> </td>


             </tr>

       
             <tr>
                <td> <h:output_text id="addrLabel" key="addressLabel" /> </td>
                <td> <h:input_textarea rows="10" cols="10" 
                                               id="address" /> </td>
             </tr>

              <tr>
             <td> <h:selectboolean_checkbox rendered="true"
                       modelReference="LoginBean.validUser"/> 
                  <h:output_text id="checkLabel" 
                                     key="validUserLabel" />

                  </td>
                  <td> <h:output_text id="testvisible" value="This should not be visible"
                               rendered="false" /> </td>
             </tr>


          </table>

	  <TABLE>

      <TR>

	<TD>

	      <h:command_hyperlink id="link" 
                  target="/faces/Basic_Thanks.jsp"
                  commandName="thankyoulink" label="Link to Thank You page"
                                                key="linkLabel" />

	</TD>

      </TR>


      <TR>

	<TD>

	      <h:command_hyperlink id="imageLink" 
                      target="/basic/index.html" image="/basic/duke.gif"/>

	</TD>

      </TR>

      <TR>

         <TD>

	      <h:selectone_listbox id="Listbox" 
                             modelReference="LoginBean.currentOption">

		<h:selectitems id="listboxOptions"
                                   modelReference="LoginBean.options"/>

                <f:valuechanged_listener type="basic.ValueChange"/>

	      </h:selectone_listbox>
                <h:output_text id="optionLabel" 
                   value="Listbox with Kinds of Beans from Model Object" />

	</TD>

      </TR>

      <TR>

      
	<TD>

	      <h:selectone_listbox id="appleQuantity" 
                     title="Select Quantity" 
                     accesskey="N" tabindex="20" >

		<h:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
		<h:selectitem  itemValue="1" itemLabel="1" title="One" />
		<h:selectitem  itemValue="2" itemLabel="2" title="Two" />
		<h:selectitem  itemValue="3" itemLabel="3" title="Three" />
		<h:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
		<h:selectitem  itemValue="5" itemLabel="5" title="Five" />
		<h:selectitem  itemValue="6" itemLabel="6" title="Six" />
		<h:selectitem  itemValue="7" itemLabel="7" title="Seven" />
		<h:selectitem  itemValue="8" itemLabel="8" title="Eight" />
		<h:selectitem  itemValue="9" itemLabel="9" title="nine" />

	      </h:selectone_listbox>

               <h:output_text id="quantityLabel" value="Option list from JSP" />
	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectone_radio id="shipType" layout="LINE_DIRECTION" >

		<h:selectitem itemValue="nextDay" itemLabel="Next Day" 
                      tabindex="30" title="Next day shipment"/>
		<h:selectitem itemValue="nextWeek" itemLabel="Next Week" title="Next week shipment"
                                  tabindex="40" selected="true" />
		<h:selectitem itemValue="nextMonth" itemLabel="Next Month" 
                        tabindex="50" title="Next month shipment"/>
 
              </h:selectone_radio>
                <h:output_text id="shipmentLabel" value="Radio laid out horizontally" />


	</TD>

      </TR>

      <TR>

	<TD>
            <h:selectone_radio id="verticalRadio" 
             modelReference="LoginBean.currentShipment" 
             layout="PAGE_DIRECTION" border="1" >

                <h:selectitem modelReference="LoginBean.nextDay" />
                <h:selectitem modelReference="LoginBean.nextWeek" />
                <h:selectitem modelReference="LoginBean.nextMonth" />

                </h:selectone_radio>
                <h:output_text id="verticalLabel" value="Radio laid out vertically" />
	</TD>

      </TR>

      <TR>

	<TD>

	      <h:selectone_radio id="radioFromModel" 
                       modelReference="LoginBean.currentOption"
                       layout="LINE_DIRECTION" >

		<h:selectitems id="listboxOptions"
                                   title="options come from model" 
                                   modelReference="LoginBean.options"/>

              </h:selectone_radio>
              <h:output_text id="modelLabel" value="Above options come from model " />

	</TD>

      </TR>

      <TR>
        <TD><h:output_text id="graphicLabel" value="Graphic Image: " /></TD>
        <TD><h:graphic_image id="graphicImage" url="/duke.gif" /></TD>
      </TR>
      <TR>
        <TD><h:output_text id="graphicLabel1" value="Graphic Image (url From Resource Bundle): " /></TD>
        <TD><h:graphic_image key="imageurl" /></TD>
      </TR>

      <TR><TD><HR></HR></TD></TR>
      <TR><TD><h:output_text id="buttontitle" 
          value="B U T T O N------S T Y L E S "/> </TD></TR>
      <TR><TD><HR></HR></TD></TR>
      <TR>
        <TD><h:output_text id="buttonlabel1" value="Button rendered with 'label' attribute:" />
            <h:command_button id="mybutton" label="Login"
                commandName="login">
                <f:action_listener type="basic.Action"/>
            </h:command_button>
        </TD>
      </TR>
      <TR>
        <TD><h:output_text id="buttonlabel2" value="Button rendered with 'image'attribute:" /> 
            <h:command_button id="pushButton" type="button" 
                commandName="push" image="duke.gif">
            </h:command_button>
        </TD>
      </TR>
      <TR>
        <TD><h:output_text  value="Reset Button rendered with label from resource:" /> 
            <h:command_button id="resetButton"  title="Click to reset form"
                commandName="reset" type="reset" 
                key="resetButton" bundle="basicBundle">
            </h:command_button>
        </TD>
      </TR>
      <TR>
        <TD><h:output_text id="buttonlabel4" value="Submit Button rendered with label from resource:" /> 
            <h:command_button type="submit" 
                commandName="login" key="loginButton" >
            </h:command_button>
        </TD>
      </TR>
      <TR><TD><HR></HR></TD></TR>
      <TR><TD><h:output_text id="selecttitle" 
          value="S E L E C T------S T Y L E S "/> </TD></TR>
      <TR><TD><HR></HR></TD></TR>
      <TR>
        <TD>Multi-select menu:</TD>
        <TD><h:selectmany_menu id="ManyApples" size="3">
            <h:selectitem itemValue="0" itemLabel="zero" />
                <h:selectitem itemValue="1" itemLabel="one" />
                    <h:selectitem itemValue="2" itemLabel="two" />
                    <h:selectitem itemValue="3" itemLabel="three" />
                    <h:selectitem itemValue="4" itemLabel="four" selected="true" />
                    <h:selectitem itemValue="5" itemLabel="five" />
                    <h:selectitem itemValue="6" itemLabel="six" />
                    <h:selectitem itemValue="7" itemLabel="seven" selected="true" />
                    <h:selectitem itemValue="8" itemLabel="eight" />
                    <h:selectitem itemValue="9" itemLabel="nine" />
                </h:selectmany_menu></TD>
      </TR>
      <TR>
        <TD>Multi-select menu with model:</TD>
        <TD><h:selectmany_menu id="menumodel" size="3">
                <h:selectitems id="menumodelitems"
                    modelReference="LoginBean.options" />
            </h:selectmany_menu></TD>
      </TR>
      <TR>
        <TD>Multi-select listbox:</TD>
        <TD><h:selectmany_listbox id="ManyApples2">
                <h:selectitem itemValue="0" itemLabel="zero" />
                <h:selectitem itemValue="1" itemLabel="one" />
                <h:selectitem itemValue="2" itemLabel="two" />
                <h:selectitem itemValue="3" itemLabel="three" />
                <h:selectitem itemValue="4" itemLabel="four" selected="true" />
                <h:selectitem itemValue="5" itemLabel="five" />
                <h:selectitem itemValue="6" itemLabel="six" />
                <h:selectitem itemValue="7" itemLabel="seven" selected="true" />
                <h:selectitem itemValue="8" itemLabel="eight" />
                <h:selectitem itemValue="9" itemLabel="nine" />
           </h:selectmany_listbox></TD>
      </TR>
      <TR>
        <TD>Multi-select listbox with model:</TD>
        <TD><h:selectmany_listbox id="listmodel"
                modelReference="LoginBean.currentOptions">
                <h:selectitems id="listmodelitems"
                    modelReference="LoginBean.options" />
            </h:selectmany_listbox></TD>
      </TR>
      <TR>
        <TD>Multi-select checkbox:</TD>
        <TD><h:selectmany_checkboxlist >
                <h:selectitem itemValue="0" itemLabel="zero" />
                <h:selectitem itemValue="1" itemLabel="one" />
                <h:selectitem itemValue="2" itemLabel="two" />
                <h:selectitem itemValue="3" itemLabel="three" />
                <h:selectitem itemValue="4" itemLabel="four" selected="true" />
                <h:selectitem itemValue="5" itemLabel="five" />
                <h:selectitem itemValue="6" itemLabel="six" />
                <h:selectitem itemValue="7" itemLabel="seven" selected="true" />
                <h:selectitem itemValue="8" itemLabel="eight" />
                <h:selectitem itemValue="9" itemLabel="nine" />
           </h:selectmany_checkboxlist></TD>
      </TR>
      <TR>
        <TD>Multi-select checkbox with model:</TD>
        <TD><h:selectmany_checkboxlist id="checklistmodel"
                modelReference="LoginBean.currentOptions">
                <h:selectitems id="checklistmodelitems"
                    modelReference="LoginBean.options" />
            </h:selectmany_checkboxlist></TD>
      </TR>
      <TR>
        <TD>Single-select menu: </TD>
        <TD><h:selectone_menu size="3">
                <h:selectitem itemValue="99" itemLabel="Wayne Gretzky" /> 
                <h:selectitem itemValue="4" itemLabel="Bobby Orr" /> 
                <h:selectitem itemValue="9" itemLabel="Gordie Howe" /> 
                <h:selectitem itemValue="2" itemLabel="Brad Park" /> 
            </h:selectone_menu> </TD>
        <TD> <h:input_hidden id="basicHidden" converter="number" value="48%">
                     <f:attribute name="numberStyle" value="percent" />
              </h:input_hidden> </TD>
      </TR>

    </TABLE>
   </h:form>
   </f:use_faces>

</HTML>
