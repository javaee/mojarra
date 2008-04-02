<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test Validator Tags</title>
    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>
  </head>

  <body>
    <h1>Test Validator Tags</h1>

<f:usefaces>
<h:form formName="validatorForm" id="validatorForm">
<table>

  <tr>

    <td>

                   <h:input_number id="outOfBounds1" formatPattern="####"
                        value="3.1415">
                     <f:validate_doublerange minimum="10.0" 
                                             maximum="10.5"/>
                   </h:input_number>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_number id="inBounds1" formatPattern="####"
                        value="10.25">
                     <f:validate_doublerange minimum="10.0" 
                                             maximum="10.5"/>
                   </h:input_number>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="outOfBounds2" value="fox">
                     <f:validate_length minimum="10" maximum="11"/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="inBounds2" value="alligator22">
                     <f:validate_length minimum="10"  maximum="12"/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_number id="outOfBounds3" value="30000">
                     <f:validate_longrange minimum="100000" maximum="110000"/>
                   </h:input_number>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_number id="inBounds3" value="1100">
                     <f:validate_longrange minimum="1000"  maximum="1200"/>
                   </h:input_number>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="required1" value="required">
                     <f:validate_required/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="outOfBounds4" value="aaa">
                     <f:validate_stringrange minimum="ggg" maximum="zzz"/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="inBounds4" value="ccc">
                     <f:validate_stringrange minimum="aaa"  maximum="zzz"/>
                   </h:input_text>

    </td>


  </tr>

  <tr>

    <td>

                   <h:input_text id="required2" value="required">
                     <f:validator 
	                type="javax.faces.validator.RequiredValidator"/>
                   </h:input_text>

    </td>


  </tr>

</table>
</h:form>
</f:usefaces>

  </body>
</html>
