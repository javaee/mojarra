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
              <td> <faces:textentry_input id="userName" />
              </td>
            </tr>

            <tr>
               <td> <faces:output_text id="pwdLabel" text="Password" /> </td>
               <td> <faces:textentry_secret id="password" /> </td>
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
