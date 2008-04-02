<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test Core Tags VB Enabling</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
  </head>

  <body>
    <h1>Test Core Tags VB Enabling</h1>

<f:view>
<h:form id="validatorForm">

<table>

  <tr>

    <td>

                   <h:input_text id="doubleRange" value="1000.0">
                        <f:convert_number type="number" integerOnly="false" 
                           maxFractionDigits="2" maxIntegerDigits="5" locale="en"
                           groupingUsed="true" />
                       <f:validate_doublerange minimum="#{doubleMin}" 
                                             maximum="#{doubleMax}"/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>
         <h:input_text id="longRange" value="1000">
                     <f:convert_number pattern="####" 
                           minFractionDigits="0" minIntegerDigits="2" />
                     <f:validate_longrange minimum="#{longMin}" 
                                             maximum="#{longMax}"/>
         </h:input_text>

    </td>

     <h:output_text id="outputNumber2" value="$123.45">
                   <f:convert_number type="currency" currencySymbol="$" />
               </h:output_text>

    <h:output_text id="outputDatetime3" value="7/10/96 12:31:31 PM PDT">
       <f:convert_datetime type="both" timeStyle="full" dateStyle="short" 
            locale="en"/>
    </h:output_text>

      

  </tr>

  <tr>

    <td>
            <h:input_text id="intRange" value="NorthAmerica">
                  
                     <f:validate_length minimum="#{intMin}" 
                                             maximum="#{intMax}"/>
             </h:input_text>

    </td>


  </tr>

</table>

</h:form>
</f:view>

  </body>
</html>
