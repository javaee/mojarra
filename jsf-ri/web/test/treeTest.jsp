<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <H3> JSF Tree Test </H3>
    <hr>

<P>This page shouldn't be actually loaded into faces.  It's just to test
the tree navigation mechanism.  These aren't really valid nestings.</P>

<faces:UseFaces>
  <faces:DeclareBean scope="session" id="LoginBean" 
                     className="basic.LoginBean"/>
  <faces:DeclareBean scope="session" id="ShipTypeBean" 
                     className="basic.ShipTypeBean"/>

  <faces:NavigationMap id="navMap" scope="session" >
    <faces:outcome commandName="login" outcome="success" 
                    targetAction="forward" targetPath = "welcome.jsp" />
    <faces:outcome commandName="login" outcome="failure" targetAction="forward"
                    targetPath = "error.jsp" />
  </faces:NavigationMap>

  <faces:Form id="1" model="1" navigationMapId="navMap" >

    <table> 

      <tr> 

        <td> 
          
          <faces:Output_Text id="2"  value="2">

            <faces:Output_Text id="5"  value="5" />

            <faces:Output_Text id="6"  value="6" />

          </faces:Output_Text>

        </td>

      </tr>

      <tr> 

        <td> 
          
          <faces:Output_Text id="3"  value="3">

            <faces:RadioGroup id="7" model="$ShipTypeBean.shipType" 
                            selectedValueModel="$ShipTypeBean.currentShipType" 
                            valueChangeListener="radioListener">
              <TABLE border="2">
 
                <TR><TD>
                  <TABLE>

                    <TR>
                      <TD>Select Shipping</TD>

                      <TD><faces:SelectOne_Radio value="nextDay" 
                                                 label=" Next Day"/>
                      </TD>

                      <TD><faces:SelectOne_Radio checked="true" 
                                 value="nextWeek" label="Next Week"/> 
                      </TD>

                      <TD><faces:SelectOne_Radio value="nextMonth" 
                                                 label="Next Month"/> 
                      </TD>

                    </TR>

                  </TABLE>
				 
                </TD></TR>

              </TABLE>

            </faces:RadioGroup>

	  </faces:Output_Text>
        
        </td>

      </tr>

      <tr>

        <td> 

          <faces:SelectBoolean_Checkbox id="4" value="4" 
                               valueChangeListener="loginListener" label="4">

            <TABLE border="2"><TR><TD><TABLE>

              <TR>
                <td> <faces:TextEntry_Input id="8" 
                      size="20" maxlength="26" 
                      value="8" 
                      valueChangeListener="loginListener" /> 
                </td>
              </TR>

              <TR>
                <td> <faces:TextEntry_Secret id="9" 
                      size="20" maxlength="26" 
                      value="9" 
                      valueChangeListener="loginListener" /> 
                </td>
              </TR>

              <TR>

                <td> 

                  <faces:TextEntry_TextArea id="10" cols="150" rows="10" 
                                    valueChangeListener="loginListener">

                    <TABLE><TR><TD>
  
                      <faces:Command_Button id="11" label="11" 
                                    commandListener="loginListener"/>
  						  
                    </TD></TR></TABLE>
                  </faces:TextEntry_TextArea>

                </td>
              </TR>

            </TABLE></TD></TR></TABLE>

          </faces:SelectBoolean_Checkbox>

        </td>

      </tr>

    </table> 

  </faces:Form>

</faces:UseFaces>

</HTML>
