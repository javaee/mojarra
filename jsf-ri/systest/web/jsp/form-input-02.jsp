<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>form-input-02</title>
</head>
<body>

<h:form id="formInput02_form" formName="formName">

  <h:panel_grid columns="3">

    <h:output_text value="booleanProperty"/>
    <h:input_text id="booleanProperty" valueRef="formInput02.booleanProperty"/>
    <h:output_errors for="booleanProperty"/>

    <h:output_text value="byteProperty"/>
    <h:input_text id="byteProperty" valueRef="formInput02.byteProperty"/>
    <h:output_errors for="byteProperty"/>

    <h:output_text value="doubleProperty"/>
    <h:input_text id="doubleProperty" valueRef="formInput02.doubleProperty"/>
    <h:output_errors for="doubleProperty"/>

    <h:output_text value="floatProperty"/>
    <h:input_text id="floatProperty" valueRef="formInput02.floatProperty"/>
    <h:output_errors for="floatProperty"/>

    <h:output_text value="intProperty"/>
    <h:input_text id="intProperty" valueRef="formInput02.intProperty"/>
    <h:output_errors for="intProperty"/>

    <h:output_text value="longProperty"/>
    <h:input_text id="longProperty" valueRef="formInput02.longProperty"/>
    <h:output_errors for="longProperty"/>

    <h:output_text value="shortProperty"/>
    <h:input_text id="shortProperty" valueRef="formInput02.shortProperty"/>
    <h:output_errors for="shortProperty"/>

    <h:output_text value="stringProperty"/>
    <h:input_text id="stringProperty" valueRef="formInput02.stringProperty"/>
    <h:output_errors for="stringProperty"/>

    <h:command_button id="submit" type="submit" value="Submit"/>
    <h:command_button id="reset"  type="reset"  value="Reset"/>
    <h:output_text value=""/>

  </h:panel_grid>

</h:form>

</body>
</html>
</f:view>
