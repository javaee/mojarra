<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<!--
 Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>Customer Details</title>
   <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
    
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="/WEB-INF/carstore.tld" prefix="cs" %>
</head>
<body>

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>
<h:form >

<h:panel_grid id="mainPanel" columns="1" footerClass="subtitle"
   headerClass="subtitlebig" styleClass="medium" columnClasses="medium">

  <h:graphic_image  url="/images/cardemo.jpg" />

  <h:output_text value="#{bundle.customerTitle}" />

  <h:panel_grid id="subPanel" columns="3" footerClass="medium"
     headerClass="subtitlebig" styleClass="medium" columnClasses="medium">

    <h:output_text value="#{bundle.titleLabel}" />
    <h:selectone_menu id="title" value="#{customer.currentTitle}">
        <f:selectitems value="#{customer.titleOptions}" />
    </h:selectone_menu>
    <h:output_text value=""/>

    <h:output_text value="#{bundle.firstLabel}" />
    <h:input_text  id="firstName" value="#{customer.firstName}" required="true"> 
        <f:valuechange_listener type="carstore.FirstNameChanged" />
    </h:input_text>
    <h:messages styleClass="validationMessage"  for="firstName"/>

    <h:output_text value="#{bundle.middleLabel}" />
    <h:input_text id="middleInitial" size="1" maxlength="1" 
            value="#{customer.middleInitial}" > 
        </h:input_text>
    <h:messages  styleClass="validationMessage" for="middleInitial"/>

    <h:output_text value="#{bundle.lastLabel}" />
    <h:input_text  value="#{customer.lastName}" />
    <h:output_text value=""/>

    <h:output_text value="#{bundle.mailingLabel}"/>
    <h:input_text  value="#{customer.mailingAddress}" />
    <h:output_text value=""/>

    <h:output_text value="#{bundle.cityLabel}" />
    <h:input_text  value="#{customer.city}" />
    <h:output_text value=""/>

    <h:output_text value="#{bundle.stateLabel}" />
    <h:selectone_menu  value="#{customer.state}" >

		<f:selectitem  itemValue="AL" itemLabel="AL" />
		<f:selectitem  itemValue="AK" itemLabel="AK"/>
		<f:selectitem  itemValue="AZ" itemLabel="AZ"/>
		<f:selectitem  itemValue="AR" itemLabel="AR"/>
		<f:selectitem  itemValue="CA" itemLabel="CA"/>
		<f:selectitem  itemValue="CO" itemLabel="CO"/>
		<f:selectitem  itemValue="CT" itemLabel="CT"/>
		<f:selectitem  itemValue="DE" itemLabel="DE"/>
		<f:selectitem  itemValue="FL" itemLabel="FL"/>
		<f:selectitem  itemValue="GA" itemLabel="GA"/>

		<f:selectitem  itemValue="HI" itemLabel="HI"/>
		<f:selectitem  itemValue="ID" itemLabel="ID"/>
		<f:selectitem  itemValue="IL" itemLabel="IL"/>
		<f:selectitem  itemValue="IN" itemLabel="IN"/>
		<f:selectitem  itemValue="IA" itemLabel="IA"/>
		<f:selectitem  itemValue="KS" itemLabel="KS"/>
		<f:selectitem  itemValue="KY" itemLabel="KY"/>
		<f:selectitem  itemValue="LA" itemLabel="LA"/>
		<f:selectitem  itemValue="ME" itemLabel="ME"/>
		<f:selectitem  itemValue="MD" itemLabel="MD"/>

		<f:selectitem  itemValue="MA" itemLabel="MA"/>
		<f:selectitem  itemValue="MI" itemLabel="MI"/>
		<f:selectitem  itemValue="MN" itemLabel="MN"/>
		<f:selectitem  itemValue="MO" itemLabel="MO"/>
		<f:selectitem  itemValue="MT" itemLabel="MT"/>
		<f:selectitem  itemValue="NE" itemLabel="NE"/>
		<f:selectitem  itemValue="NV" itemLabel="NV"/>
		<f:selectitem  itemValue="NH" itemLabel="NH"/>
		<f:selectitem  itemValue="NJ" itemLabel="NJ"/>
		<f:selectitem  itemValue="NM" itemLabel="NM"/>

		<f:selectitem  itemValue="MY" itemLabel="MY"/>
		<f:selectitem  itemValue="NC" itemLabel="NC"/>
		<f:selectitem  itemValue="ND" itemLabel="ND"/>
		<f:selectitem  itemValue="OH" itemLabel="OH"/>
		<f:selectitem  itemValue="OK" itemLabel="OK"/>
		<f:selectitem  itemValue="OR" itemLabel="OR"/>
		<f:selectitem  itemValue="PA" itemLabel="PA"/>
		<f:selectitem  itemValue="RI" itemLabel="RI"/>
		<f:selectitem  itemValue="SC" itemLabel="SC"/>
		<f:selectitem  itemValue="SD" itemLabel="SD"/>

		<f:selectitem  itemValue="TN" itemLabel="TN"/>
		<f:selectitem  itemValue="TX" itemLabel="TX"/>
		<f:selectitem  itemValue="UT" itemLabel="UT"/>
		<f:selectitem  itemValue="VT" itemLabel="VT"/>
		<f:selectitem  itemValue="VA" itemLabel="VA"/>
		<f:selectitem  itemValue="WA" itemLabel="WA"/>
		<f:selectitem  itemValue="WV" itemLabel="WV"/>
		<f:selectitem  itemValue="WI" itemLabel="WI"/>
		<f:selectitem  itemValue="WY" itemLabel="WY"/>
    </h:selectone_menu>
    <h:output_text value=""/>

    <h:output_text  value="#{bundle.zipLabel}" />
    <h:input_text id="zip"  
			value="#{customer.zip}"
                        size="10" required="true">
          <cs:format_validator formatPatterns="99999|99999-9999|### ###"/> 
    </h:input_text>
    <h:messages  styleClass="validationMessage" for="zip" />

    <h:output_text  value="#{bundle.ccNumberLabel}" />
    <h:input_text id="ccno" size="16"
           converter="#{creditCardConverter}" required="true">
          <cs:format_validator 
          formatPatterns="9999999999999999|9999 9999 9999 9999|9999-9999-9999-9999"/>
     </h:input_text>
     <h:messages styleClass="validationMessage"  for="ccno"/>  
 
    <h:output_text  value="#{bundle.monthLabel}" />
    <h:panel_grid id="monthYearPanel" columns="2" footerClass="medium"
            headerClass="medium" styleClass="medium" columnClasses="medium">
      <h:selectone_menu  value="#{customer.month}">
        <f:selectitem itemValue="01" itemLabel="01"/>
        <f:selectitem itemValue="02" itemLabel="02"/>
        <f:selectitem itemValue="03" itemLabel="03"/>
        <f:selectitem itemValue="04" itemLabel="04"/>
        <f:selectitem itemValue="05" itemLabel="05"/>
        <f:selectitem itemValue="06" itemLabel="06"/>
        <f:selectitem itemValue="07" itemLabel="07"/>
        <f:selectitem itemValue="08" itemLabel="08"/>
        <f:selectitem itemValue="09" itemLabel="09"/>
        <f:selectitem itemValue="10" itemLabel="10"/>
        <f:selectitem itemValue="11" itemLabel="11"/>
        <f:selectitem itemValue="12" itemLabel="12"/>
    </h:selectone_menu>

      <h:selectone_menu  value="#{customer.year}" >
        <f:selectitem itemValue="2002" itemLabel="2002"/>
        <f:selectitem itemValue="2003" itemLabel="2003"/>
        <f:selectitem itemValue="2004" itemLabel="2004"/>
        <f:selectitem itemValue="2005" itemLabel="2005"/>
        <f:selectitem itemValue="2006" itemLabel="2006"/>
        <f:selectitem itemValue="2007" itemLabel="2007"/>
        <f:selectitem itemValue="2008" itemLabel="2008"/>
    </h:selectone_menu>
    </h:panel_grid>
    <h:output_text value=""/>

 </h:panel_grid>

<h:command_button  value="#{bundle.finishButton}" action="finish" />

<h:graphic_image id="duke" url="/images/duke.gif" />

<h:output_text  value="#{bundle.buyLabel}" />

</h:panel_grid>

</h:form>
</f:view>
</body>
</html>

