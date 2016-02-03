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
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
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
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:subview id="optionsPanel">

  <h:panelGrid>

    <h:outputText value="#{bundle.OptionsPackages}" />

    <!-- options package chooser -->

    <h:panelGrid columns="4">    

      <h:commandButton id="Custom" value="#{bundle.Custom}"
          styleClass="#{carstore.customizers.Custom.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Standard" value="#{bundle.Standard}"
          styleClass="#{carstore.customizers.Standard.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Performance" value="#{bundle.Performance}"
          styleClass="#{carstore.customizers.Performance.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Deluxe" value="#{bundle.Deluxe}"
          styleClass="#{carstore.customizers.Deluxe.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

    </h:panelGrid>

  </h:panelGrid>

  <h:panelGrid columns="2">

        <h:outputText value="#{bundle.Engine}" 
                       styleClass="optionLabel"/>

         <h:selectOneMenu styleClass="optionValue"
               binding="#{carstore.currentModel.components.engine}"/>

          <h:outputText value="#{bundle.Brakes}" 
               styleClass="optionLabel" />

          <h:selectOneRadio styleClass="optionValue"
               binding="#{carstore.currentModel.components.brake}"/>

         <h:outputText value="#{bundle.Suspension}" 
                       styleClass="optionLabel"/>

        <h:selectOneMenu  styleClass="optionValue"
                   binding="#{carstore.currentModel.components.suspension}"/>

        <h:outputText value="#{bundle.Speakers}" 
                       styleClass="optionLabel"/>

        <h:selectOneRadio  styleClass="optionValue"
           binding="#{carstore.currentModel.components.speaker}"/>

        <h:outputText value="#{bundle.Audio}" 
                       styleClass="optionLabel"/>

        <h:selectOneRadio styleClass="optionValue"
               binding="#{carstore.currentModel.components.audio}"/>

        <h:outputText value="#{bundle.Transmission}" 
                       styleClass="optionLabel"/>

        <h:selectOneMenu  styleClass="optionValue"
               binding="#{carstore.currentModel.components.transmission}"/>

      </h:panelGrid>

   <h:outputText value="#{bundle.OtherOptions}" 
                   styleClass="optionLabel"/>

   <h:panelGrid columns="6">

     <h:selectBooleanCheckbox title="#{bundle.sunroofLabel}" 
         binding="#{carstore.currentModel.components.sunroof}">
     </h:selectBooleanCheckbox>    

     <h:outputText value="#{bundle.sunroofLabel}" /> 

     <h:selectBooleanCheckbox  title="#{bundle.cruiseLabel}"  
         binding="#{carstore.currentModel.components.cruisecontrol}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.cruiseLabel}" /> 

     <h:selectBooleanCheckbox title="#{bundle.keylessLabel}"  
          binding="#{carstore.currentModel.components.keylessentry}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.keylessLabel}" /> 

     <h:selectBooleanCheckbox 
        title="#{bundle.securityLabel}" 
        binding="#{carstore.currentModel.components.securitySystem}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.securityLabel}" />  

     <h:selectBooleanCheckbox  title="#{bundle.skiRackLabel}"  
         binding="#{carstore.currentModel.components.skiRack}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.skiRackLabel}" /> 

     <h:selectBooleanCheckbox  title="#{bundle.towPkgLabel}"  
           binding="#{carstore.currentModel.components.towPackage}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.towPkgLabel}" /> 

     <h:selectBooleanCheckbox  title="#{bundle.gpsLabel}" 
         binding="#{carstore.currentModel.components.gps}" >
     </h:selectBooleanCheckbox>

     <h:outputText value="#{bundle.gpsLabel}" /> 

   </h:panelGrid>

</f:subview>
