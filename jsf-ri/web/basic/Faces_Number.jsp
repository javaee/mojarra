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

       <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />
       <faces:usefaces>  
        <faces:form id="numberForm" formName="numberForm" >

            <table>

            <tr>
              <td> <faces:output_text id="outputLabel" value="DISPLAY-ONLY" /> </td>
              <td> <faces:output_number id="outputNumber" formatPattern="####.##"
                       value="9989.456987"/> </td>
            </tr> 

            <tr>
              <td> <faces:output_text id="numberLabel" value="NUMBER" /> </td>
              <td> <faces:input_number id="testNumber" numberStyle="NUMBER"
                       value="1239989.6079"/> </td>
            </tr>

            <tr> 
              <td> <faces:output_text id="currLabel" value="CURRENCY" /> </td>
              <td> <faces:input_number id="testcurrency" numberStyle="CURRENCY"  
                       value="$1234789.60"/> </td>
            </tr>

             <tr>
              <td> <faces:output_text id="percentLabel" value="PERCENT" /> </td>
              <td> 
                   <faces:input_number id="testPercent" numberStyle="PERCENT" 
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
              <td> <faces:output_text id="byteLabel" value="BYTE" /> </td>
              <td>
                   <faces:input_number id="byteInput" numberStyle="INTEGER"
                        modelReference="${LoginBean.byte}"/>
              </td>
             </tr>

             <tr>
              <td> <faces:output_text id="doubleLabel" value="DOUBLE" /> </td>
              <td>
                   <faces:input_number id="doubleInput" numberStyle="NUMBER"
                        modelReference="${LoginBean.double}"/>
              </td>
             </tr>

             <tr>
              <td> <faces:output_text id="floatLabel" value="FLOAT" /> </td>
              <td>
                   <faces:input_number id="floatInput" numberStyle="NUMBER"
                        modelReference="${LoginBean.float}"/>
              </td>
             </tr>

             <tr>
              <td> <faces:output_text id="intLabel" value="INTEGER" /> </td>
              <td>
                   <faces:input_number id="intInput" numberStyle="INTEGER"
                        modelReference="${LoginBean.int}"/>
              </td>
             </tr>

             <tr>
              <td> <faces:output_text id="longLabel" value="LONG" /> </td>
              <td>
                   <faces:input_number id="longInput" numberStyle="NUMBER"
                        modelReference="${LoginBean.long}"/>
              </td>
             </tr>

              <tr>
              <td> <faces:output_text id="shortLabel" value="SHORT" /> </td>
              <td>
                   <faces:input_number id="shortInput" numberStyle="NUMBER"
                        modelReference="${LoginBean.short}"/>
              </td>
             </tr>

             <tr>
              <td> <faces:output_text id="charLabel" value="CHARACTER" /> </td>
              <td>
                   <faces:input_number id="charInput" numberStyle="INTEGER"
                        modelReference="${LoginBean.char}"/>
              </td>
             </tr>

            <tr>
                <td>
                <faces:command_button id="numberlogin" label="Login"
                                key="loginButton"
                                bundle="${basicBundle}" commandName="login"/>

                </td>
            </tr>
          </table>
        </faces:form>
       </faces:usefaces>
</HTML>
