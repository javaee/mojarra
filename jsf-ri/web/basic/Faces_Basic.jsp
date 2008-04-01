<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

    <H3> JSF Basic Components Test Page </H3>
    <hr>
       <faces:usefaces>  
        <faces:form id="basicForm" formName="basicForm" >

            <table> 
            <tr> 
              <td> <faces:output_text id="userLabel" text="Username" /> </td>
              <td> 

                   <faces:textentry_input id="userName">

		     <faces:validator className="javax.faces.validator.LengthValidator"/>
		     <faces:validator className="javax.faces.validator.RequiredValidator"/>
		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MINIMUM"
                         value="6"/>

		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MAXIMUM"
                         value="10"/>
                   </faces:textentry_input>

              </td>

              <td> <faces:validation_message componentId="userName"/> </td>

            </tr>

            <tr>
               <td> <faces:output_text id="pwdLabel" text="Password" /> </td>

               <td> 

                    <faces:textentry_secret id="password"> 

		     <faces:validator className="javax.faces.validator.LengthValidator"/>
		     <faces:validator className="javax.faces.validator.RequiredValidator"/>
		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MINIMUM"
                         value="6"/>

		     <faces:attribute 
                         name="javax.faces.validator.LengthValidator.MAXIMUM"
                         value="10"/>

                    </faces:textentry_secret>

               </td>

              <td> <faces:validation_message componentId="password"/> </td>


             </tr>

            <tr>
               <td> <faces:output_text id="doubleLabel" 
                                       text="Double (3.2 - 3.9)" /> </td>

               <td> 

                    <faces:textentry_input id="double"
                                  modelReference="${LoginBean.double}"> 

		     <faces:validator 
                       className="javax.faces.validator.DoubleRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.DoubleRangeValidator.MINIMUM"
                         value="3.2"/>

		     <faces:attribute 
                         name="javax.faces.validator.DoubleRangeValidator.MAXIMUM"
                         value="3.9"/>

                    </faces:textentry_input>

               </td>

              <td> <faces:validation_message componentId="double"/> </td>


             </tr>

            <tr>
               <td> <faces:output_text id="intLabel" 
                                       text="Integer (1-10)" /> </td>

               <td> 

                    <faces:textentry_input id="integer"
                                  modelReference="${LoginBean.int}"> 

		     <faces:validator 
                       className="javax.faces.validator.IntegerRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.IntegerRangeValidator.MINIMUM"
                         value="1"/>

		     <faces:attribute 
                         name="javax.faces.validator.IntegerRangeValidator.MAXIMUM"
                         value="10"/>

                    </faces:textentry_input>

               </td>

              <td> <faces:validation_message componentId="integer"/> </td>


             </tr>

            <tr>
               <td> <faces:output_text id="stringLength" 
                                       text="Letter (a-f)" /> </td>

               <td> 

                    <faces:textentry_input id="string" size="1"
                                  modelReference="${LoginBean.string}"> 

		     <faces:validator 
                       className="javax.faces.validator.StringRangeValidator"/>

		     <faces:attribute 
                         name="javax.faces.validator.StringRangeValidator.MINIMUM"
                         value="a"/>

		     <faces:attribute 
                         name="javax.faces.validator.StringRangeValidator.MAXIMUM"
                         value="f"/>

                    </faces:textentry_input>

               </td>

              <td> <faces:validation_message componentId="string"/> </td>


             </tr>

       
             <tr>
                <td> <faces:output_text id="addrLabel" text="Address" /> </td>
                <td> <faces:textentry_textarea rows="10" cols="10" 
                                               id="address" /> </td>
             </tr>

              <tr>
             <td> <faces:selectboolean_checkbox id="validUser" 
                                                label="Valid User"/>
                  </td>
             </tr>


          </table>

	  <TABLE>

      <TR>

	<TD>

	      <faces:command_hyperlink id="link" 
                  target="/faces/Basic_Thanks.jsp"
                  commandName="thankyoulink" label="Link to Thank You page"/>

	</TD>

      </TR>


      <TR>

	<TD>

	      <faces:command_hyperlink id="imageLink" target="/basic/index.html"
                  image="/basic/duke.gif"/>
              Image Link to index

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

	<TD>

             <faces:command_button id="login" label="Login"
                                   commandName="login"/>

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
