<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc" %>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<body>

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup id="body">

        <t:saveState id="save1" value="#{calcForm.number1}" />
        <t:saveState id="save2" value="#{calcForm.number2}" />
        <t:saveState id="save3" value="#{ucaseForm.text}" />

        <t:messages id="messageList" styleClass="error" summaryFormat="{0} in {1}" />

        <f:verbatim>
            <h4>
        </f:verbatim>
                <h:outputText value="#{example_messages['sample1_form']}"/>
        <f:verbatim>
            </h4>
            <table border="1"><tr><td>
        </f:verbatim>

        <h:form id="form1" name="calcForm">
            <h:outputLabel for="form1:number1" value="#{example_messages['sample1_number']} 1" />
            <h:outputText value="#{validationController.number1ValidationLabel}"/>
            <f:verbatim>: </f:verbatim>
            <h:inputText id="number1" value="#{calcForm.number1}" maxlength="10" size="25" required="true" >
               <f:validateLongRange minimum="1" maximum="10" />
            </h:inputText>
            <h:message id="number1Error" for="form1:number1" styleClass="error" /><f:verbatim><br></f:verbatim>

            <h:outputLabel for="form1:number2" value="#{example_messages['sample1_form']} 2" />
            <h:outputText value="#{validationController.number2ValidationLabel}"/>
            <f:verbatim>: </f:verbatim>
            <h:inputText id="number2" value="#{calcForm.number2}" maxlength="10" size="25" required="true" >
               <f:validateLongRange minimum="20" maximum="50" />
            </h:inputText>
            <h:message id="number2Error" for="form1:number2" styleClass="error" /><f:verbatim><br></f:verbatim>

            <h:outputLabel for="form1:result" value="#{example_messages['sample1_result']}" /><f:verbatim>: </f:verbatim>
            <h:outputText id="result" value="#{calcForm.result}" /><f:verbatim><br></f:verbatim>

            <h:commandButton id="addButton" value="#{example_messages['sample1_add']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandButton>
            <h:commandButton id="subtractButton" value="#{example_messages['sample1_sub']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandButton>
            <f:verbatim><br></f:verbatim>

            <h:commandLink id="href1" action="none">
                <h:outputText value="#{example_messages['sample1_add_link']}"/>
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandLink><f:verbatim><br></f:verbatim>
            <h:commandLink id="href2" action="none">
                <h:outputText value="#{example_messages['sample1_sub_link']}"/>
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandLink>
        </h:form>

        <f:verbatim>
            </td></tr></table>
            <h4>
        </f:verbatim>
            <h:outputText value="#{example_messages['sample1_another_form']}"/>
        <f:verbatim>
            </h4>
            <table border="1"><tr><td>
        </f:verbatim>

        <h:form id="form2" name="ucaseForm">
            <h:outputLabel for="form2:text" value="#{example_messages['sample1_text']}" />
            <h:outputText value="#{validationController.textValidationLabel}"/>
            <f:verbatim>: </f:verbatim>
            <h:inputText id="text" value="#{ucaseForm.text}">
                <f:validateLength minimum="3" maximum="7"/>
            </h:inputText>
            <h:message id="textError" for="form2:text" styleClass="error" /><f:verbatim><br></f:verbatim>
            <h:commandButton id="ucaseButton" value="#{example_messages['sample1_uppercase']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.UCaseActionListener" />
            </h:commandButton>
            <h:commandButton id="lcaseButton" value="#{example_messages['sample1_lowercase']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.UCaseActionListener" />
            </h:commandButton>
            <f:verbatim><br></f:verbatim>
        </h:form>

        <f:verbatim>
            </td></tr></table>
            <h4>
        </f:verbatim>
            <h:outputText value="#{example_messages['sample1_validation']}"/>
        <f:verbatim>
            </h4>
            <table border="1"><tr><td>
        </f:verbatim>

        <h:form id="form3" name="valForm">
            <h:commandButton id="valDisable" value="#{example_messages['sample1_disable_validation']}" action="#{validationController.disableValidation}" />
            <h:commandButton id="valEnable" value="#{example_messages['sample1_enable_validation']}" action="#{validationController.enableValidation}" />
        </h:form>

        <f:verbatim>
            </td></tr></table>
        </f:verbatim>

        <f:verbatim><br></f:verbatim>
        <h:commandLink id="jump_home" action="#{ucaseForm.jumpHome}" ><f:verbatim>Go Home</f:verbatim></h:commandLink>

    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
