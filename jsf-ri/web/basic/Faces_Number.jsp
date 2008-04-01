<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <%@ taglib uri="WEB-INF/lib/basic.tld" prefix="basic" %>

    <H3> JSF NumberFormat Renderer Test Page </H3>
    <hr>
       <fmt:setBundle
	    basename="basic.Resources"
	    scope="session" var="basicBundle"/>

       <faces:usefaces>  
        <faces:form id="basicForm" formName="basicForm" >

            <table>

            <tr>
              <td> <faces:output_text id="outputLabel" value="DISPLAY-ONLY" /> </td>
              <td> <faces:output_number id="outputNumber" formatPattern="####.##"
                       value="9989.456987"/> </td>
            </tr> 

            <tr>
              <td> <faces:output_text id="numberLabel" value="NUMBER" /> </td>
              <td> <faces:input_number id="testNumber" formatStyle="NUMBER"
                       value="1239989.6079"/> </td>
            </tr>

            <tr> 
              <td> <faces:output_text id="currLabel" value="CURRENCY" /> </td>
              <td> <faces:input_number id="testcurrency" formatStyle="CURRENCY"  
                       value="$1234789.60"/> </td>
            </tr>

             <tr>
              <td> <faces:output_text id="percentLabel" value="PERCENT" /> </td>
              <td> 
                   <faces:input_number id="testPercent" formatStyle="PERCENT" 
                        value="45%"/>
              </td>

            </tr>

            <tr>
              <td> <faces:output_text id="patternLabel" value="PATTERN" /> </td>
              <td>
                   <faces:input_number id="testPattern" formatPattern="####"
                        value="9999.98765"/>
              </td>

            </tr>

            <tr>
                <td>
                    <faces:command_button id="login" label="Login"
                                 commandName="login"/>

                </td>
            </tr>
          </table>
        </faces:form>
       </faces:usefaces>
</HTML>
