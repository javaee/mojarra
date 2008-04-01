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

                   <faces:input_text id="userName">

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

            <TD> OptionList with Kinds of Beans from Model Object

            </TD>	

            <TD>

	      <faces:selectone_optionlist id="OptionList" 
                             modelReference="${LoginBean.currentOption}">

		<faces:selectitems id="optionListOptions"
                                   modelReference="${LoginBean.options}"/>

	      </faces:selectone_optionlist>

	</TD>

      </TR>

      <TR>

        <TD> Option list from JSP
        </TD>

	<TD>

	      <faces:selectone_optionlist id="appleQuantity" size="6"
                      title="Select Quantity" accesskey="N" tabindex="20" >

		<faces:selectitem  disabled="true" value="0" label="0"/>
		<faces:selectitem  value="1" label="1" title="One"/>
		<faces:selectitem  value="2" label="2" title="Two" />
		<faces:selectitem  value="3" label="3" title="Three" />
		<faces:selectitem  value="4" label="4" title="Four" selected="true"/>
		<faces:selectitem  value="5" label="5" title="Five" />
		<faces:selectitem  value="6" label="6" title="Six" />
		<faces:selectitem  value="7" label="7" title="Seven" />
		<faces:selectitem  value="8" label="8" title="Eight" />
		<faces:selectitem  value="9" label="9" title="nine" />

	      </faces:selectone_optionlist>

	</TD>

      </TR>

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="shipType" layout="horizontal">

		<faces:selectitem value="nextDay" label="Next Day" 
                      tabindex="30" title="Next day shipment"/>
		<faces:selectitem value="nextWeek" label="Next Week" title="Next week shipment"
                                  tabindex="40" selected="true" />
		<faces:selectitem value="nextMonth" label="Next Month" 
                        tabindex="50" title="Next month shipment"/>

              </faces:selectone_radiogroup>

	</TD>

      </TR>

      <TR>

	<TD>
		<faces:selectone_radiogroup id="verticalRadio" 
                                            layout="vertical" border="1" >

  		<faces:selectitem value="nextDay" label="Next Day" 
                                  selected="true" />
		<faces:selectitem value="nextWeek" label="Next Week"  />
		<faces:selectitem value="nextMonth" label="Next Month" />

                </faces:selectone_radiogroup>

	</TD>

      </TR>
<<<<<<< variant A
>>>>>>> variant B

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="radioFromModel" 
                       modelReference="${LoginBean.currentOption}"
                       layout="horizontal">

		<faces:selectitems id="optionListOptions"
                                   title="options come from model" 
                                   modelReference="${LoginBean.options}"/>

              </faces:selectone_radiogroup>

	</TD>

      </TR>

======= end

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

             <faces:command_button id="pushButton" label="This is a push button"
                                 type="button" commandName="push" disabled = "true" />

        </TD>

        <TD>

             <faces:command_button id="resetButton"  title="Click to reset form"
                                key="resetButton" type="reset"
                                bundle="${basicBundle}" commandName="reset"/>

        </TD>

      </TR>
      
      <TR>

	<TD>

             <faces:command_button id="login" label="Login" 
                                key="loginButton"
                                bundle="${basicBundle}" commandName="login"/>

	</TD>

      </TR>


      <TR>

	<TD>

             <faces:command_button id="imageButton" image="/basic/duke.gif"
                                   commandName="login"/>

               Image button that does the same thing

	</TD>

      </TR>



	  </TABLE>

        </faces:form>
       </faces:usefaces>
</HTML>
