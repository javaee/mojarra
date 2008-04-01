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

              <td> <faces:validation_message componentId="userName"/> </td>

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

              <td> <faces:validation_message componentId="password"/> </td>


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

              <td> <faces:validation_message componentId="double"/> </td>


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

              <td> <faces:validation_message componentId="integer"/> </td>


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

              <td> <faces:validation_message componentId="string"/> </td>


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
<<<<<<< variant A
>>>>>>> variant B

      <TR>

	<TD>

	      <faces:selectone_radiogroup id="radioFromModel" 
                       modelReference="${LoginBean.currentOption}"
                       align="horizontal">

		<faces:selectitems id="optionListOptions"
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
