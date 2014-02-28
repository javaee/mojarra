<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>


<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Converters</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Converters</h1>

<f:loadBundle basename="com.sun.faces.CustomMessages" var="customBundle"/>

<f:view>

  <h:form id="form">

    <h:panelGrid id="panelGrid" columns="3">

<%-- Case 1: Big Decimal Converter with no "label" attribute --%>

      <h:outputText value="BigDecimal1:" />
      <h:inputText id="bd1"> 
        <f:converter converterId="javax.faces.BigDecimal" />
      </h:inputText>
      <h:message for="bd1" showSummary="true"/>

<%-- Case 2: Big Decimal Converter with "label" attribute --%>

      <h:outputText value="BigDecimal2:" />
      <h:inputText id="bd2" label="#{customBundle.BigDecimalLabel}"> 
        <f:converter converterId="javax.faces.BigDecimal" />
      </h:inputText>
      <h:message for="bd2" showSummary="true" />

<%-- Case 3: Big Integer Converter with no "label" attribute --%>
                                                                                
      <h:outputText value="BigInteger1:" />
      <h:inputText id="bi1">
        <f:converter converterId="javax.faces.BigInteger" />
      </h:inputText>
      <h:message for="bi1" showSummary="true"/>
                                                                                
<%-- Case 4: Big Integer Converter with "label" attribute --%>
                                                                                
      <h:outputText value="BigInteger2:" />
      <h:inputText id="bi2" label="BigInteger2">
        <f:converter converterId="javax.faces.BigInteger" />
      </h:inputText>
      <h:message for="bi2" showSummary="true" />

<%-- Case 5: Boolean Converter with no "label" attribute --%>
                                                                                
      <h:outputText value="Boolean1:" />
      <h:selectBooleanCheckbox id="b1" value="idontknow">
        <f:converter converterId="javax.faces.Boolean" />
      </h:selectBooleanCheckbox>
      <h:message for="b1" showSummary="true" />

                                                                                
<%-- Case 6: Boolean Converter with "label" attribute --%>
                                                                                
      <h:outputText value="Boolean2:" />
      <h:selectBooleanCheckbox id="b2" label="Boolean2" value="true">
        <f:converter converterId="javax.faces.Boolean" />
      </h:selectBooleanCheckbox>
      <h:message for="b2" showSummary="true" />

<%-- Case 7: Byte Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Byte1:" />
      <h:inputText id="byte1">
        <f:converter converterId="javax.faces.Byte" />
      </h:inputText>
      <h:message for="byte1"  showSummary="true"/>
                                                                                         
<%-- Case 8: Byte Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Byte2:" />
      <h:inputText id="byte2" label="Byte2">
        <f:converter converterId="javax.faces.Byte" />
      </h:inputText>
      <h:message for="byte2" showSummary="true" />

<%-- Case 9: Character Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Character1:" />
      <h:inputText id="char1">
        <f:converter converterId="javax.faces.Character" />
      </h:inputText>
      <h:message for="char1"  showSummary="true"/>
                                                                                         
<%-- Case 10: Character Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Character2:" />
      <h:inputText id="char2" label="Character2">
        <f:converter converterId="javax.faces.Character" />
      </h:inputText>
      <h:message for="char2" showSummary="true" />

<%-- Case 11: DateTime Converter - type="date" with no "label" attribute --%>

      <h:outputText value="Date1:" />
      <h:inputText id="date1" size="10" maxlength="20">
         <f:convertDateTime type="date" dateStyle="medium"/>
      </h:inputText>
      <h:message for="date1"  showSummary="true"/>

<%-- Case 12: DateTime Converter - type="date" with "label" attribute --%>

      <h:outputText value="Date2:" />
      <h:inputText id="date2" label="Date2" size="10" maxlength="20">
         <f:convertDateTime type="date" dateStyle="medium"/>
      </h:inputText>
      <h:message for="date2" showSummary="true" />

<%-- Case 13: DateTime Converter - type="time" with no "label" attribute --%>
                                                                                         
      <h:outputText value="Time1:" />
      <h:inputText id="time1" size="10" maxlength="20">
         <f:convertDateTime type="time" timeStyle="medium"/>
      </h:inputText>
      <h:message for="time1"  showSummary="true"/>
                                                                                         
<%-- Case 12: DateTime Converter - type="time" with "label" attribute --%>
                                                                                         
      <h:outputText value="Time2:" />
      <h:inputText id="time2" label="Time2" size="10" maxlength="20">
         <f:convertDateTime type="time" timeStyle="medium"/>
      </h:inputText>
      <h:message for="time2" showSummary="true" />

<%-- Case 13: DateTime Converter - type="both" with no "label" attribute --%>
                                                                                         
      <h:outputText value="DateTime1:" />
      <h:inputText id="datetime1" size="10" maxlength="20">
         <f:convertDateTime type="both"/>
      </h:inputText>
      <h:message for="datetime1"  showSummary="true"/>
                                                                                         
<%-- Case 14: DateTime Converter - type="both" with "label" attribute --%>
                                                                                         
      <h:outputText value="DateTime2:" />
      <h:inputText id="datetime2" label="DateTime2" size="10" maxlength="20">
         <f:convertDateTime type="both" />
      </h:inputText>
      <h:message for="datetime2" showSummary="true" />

<%-- Case 15: Double Converter with no "label" attribute --%>

      <h:outputText value="Double1:" />
      <h:inputText id="double1" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Double" />
      </h:inputText>
      <h:message for="double1"  showSummary="true"/>

<%-- Case 16: Double Converter with "label" attribute --%>

      <h:outputText value="Double2:" />
      <h:inputText id="double2" label="Double2" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Double" />
      </h:inputText>
      <h:message for="double2" showSummary="true" />

<%-- Case 17: Float Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Float1:" />
      <h:inputText id="float1" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Float" />
      </h:inputText>
      <h:message for="float1"  showSummary="true"/>
                                                                                         
<%-- Case 18: Float Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Float2:" />
      <h:inputText id="float2" label="Float2" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Float" />
      </h:inputText>
      <h:message for="float2" showSummary="true" />
                                                                                         
<%-- Case 19: Integer Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Integer1:" />
      <h:inputText id="integer1" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Integer" />
      </h:inputText>
      <h:message for="integer1"  showSummary="true"/>
                                                                                         
<%-- Case 20: Integer Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Integer2:" />
      <h:inputText id="integer2" label="Integer2" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Integer" />
      </h:inputText>
      <h:message for="integer2" showSummary="true" />
                                                                                         
<%-- Case 21: Long Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Long1:" />
      <h:inputText id="long1" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Long" />
      </h:inputText>
      <h:message for="long1"  showSummary="true"/>
                                                                                         
<%-- Case 22: Long Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Long2:" />
      <h:inputText id="long2" label="Long2" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Long" />
      </h:inputText>
      <h:message for="long2" showSummary="true" />

<%-- Case 23: Number Converter type="currency" with no "label" attribute --%>

      <h:outputText value="Number1:" />
      <h:inputText id="number1" size="10" maxlength="20">
         <f:convertNumber type="currency" />
      </h:inputText>
      <h:message for="number1"  showSummary="true"/>

<%-- Case 24: Number Converter type="currency" with "label" attribute --%>

      <h:outputText value="Number2:" />
      <h:inputText id="number2" label="Number2" size="10" maxlength="20">
         <f:convertNumber type="currency" />
      </h:inputText>
      <h:message for="number2" showSummary="true" />

<%-- Case 25: Number Converter type="number" with no "label" attribute --%>
                                                                                         
      <h:outputText value="Number3:" />
      <h:inputText id="number3" size="10" maxlength="20">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number3"  showSummary="true"/>
                                                                                         
<%-- Case 26: Number Converter type="number" with "label" attribute --%>
                                                                                         
      <h:outputText value="Number4:" />
      <h:inputText id="number4" label="Number4" size="10" maxlength="20">
         <f:convertNumber type="number" />
      </h:inputText>
      <h:message for="number4" showSummary="true" />

<%-- Case 27: Number Converter type="percent" with no "label" attribute --%>
                                                                                         
      <h:outputText value="Number5:" />
      <h:inputText id="number5" size="10" maxlength="20">
         <f:convertNumber type="percent" />
      </h:inputText>
      <h:message for="number5"  showSummary="true"/>
                                                                                         
<%-- Case 28: Number Converter type="percent" with "label" attribute --%>
                                                                                         
      <h:outputText value="Number6:" />
      <h:inputText id="number6" label="Number6" size="10" maxlength="20">
         <f:convertNumber type="percent" />
      </h:inputText>
      <h:message for="number6" showSummary="true" />

<%-- Case 29: Number Converter with pattern with no "label" attribute --%>
                                                                                         
      <h:outputText value="Number7:" />
      <h:inputText id="number7" size="10" maxlength="20">
         <f:convertNumber pattern="invalid pattern" />
      </h:inputText>
      <h:message for="number7"  showSummary="true"/>
                                                                                         
<%-- Case 30: Number Converter with pattern with "label" attribute --%>
                                                                                         
      <h:outputText value="Number8:" />
      <h:inputText id="number8" label="Number8" size="10" maxlength="20">
         <f:convertNumber pattern="invalid pattern" />
      </h:inputText>
      <h:message for="number8" showSummary="true" />

<%-- Case 31: Short Converter with no "label" attribute --%>
                                                                                         
      <h:outputText value="Short1:" />
      <h:inputText id="short1" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Short" />
      </h:inputText>
      <h:message for="short1"  showSummary="true"/>
                                                                                         
<%-- Case 32: Short Converter with "label" attribute --%>
                                                                                         
      <h:outputText value="Short2:" />
      <h:inputText id="short2" label="Short2" size="10" maxlength="20">
         <f:converter converterId="javax.faces.Short" />
      </h:inputText>
      <h:message for="short2" showSummary="true" />

      <h:commandButton value="submit" /> 

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
