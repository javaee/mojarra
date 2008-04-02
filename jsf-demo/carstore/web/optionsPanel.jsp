<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:subview id="optionsPanel">

  <h:panel_grid>

    <h:output_text value="#{bundle.OptionsPackages}" />

    <!-- options package chooser -->

    <h:panel_grid columns="4">    

      <h:command_button id="Custom" value="#{bundle.Custom}" immediate="true"
          styleClass="#{carstore.customizers.Custom.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:command_button id="Standard" value="#{bundle.Standard}" immediate="true"
          styleClass="#{carstore.customizers.Standard.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:command_button id="Performance" value="#{bundle.Performance}" immediate="true"
          styleClass="#{carstore.customizers.Performance.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:command_button id="Deluxe" value="#{bundle.Deluxe}" immediate="true"
          styleClass="#{carstore.customizers.Deluxe.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

    </h:panel_grid>

  </h:panel_grid>

  <h:panel_grid columns="2">

        <h:output_text value="#{bundle.Engine}" 
                       styleClass="optionLabel"/>

         <h:selectone_menu styleClass="optionValue"
               binding="#{carstore.currentModel.components.engine}"/>

          <h:output_text value="#{bundle.Brakes}" 
               styleClass="optionLabel" />

          <h:selectone_radio styleClass="optionValue"
               binding="#{carstore.currentModel.components.brake}"/>

         <h:output_text value="#{bundle.Suspension}" 
                       styleClass="optionLabel"/>

        <h:selectone_menu  styleClass="optionValue"
                   binding="#{carstore.currentModel.components.suspension}"/>

        <h:output_text value="#{bundle.Speakers}" 
                       styleClass="optionLabel"/>

        <h:selectone_radio  styleClass="optionValue"
           binding="#{carstore.currentModel.components.speaker}"/>

        <h:output_text value="#{bundle.Audio}" 
                       styleClass="optionLabel"/>

        <h:selectone_radio styleClass="optionValue"
               binding="#{carstore.currentModel.components.audio}"/>

        <h:output_text value="#{bundle.Transmission}" 
                       styleClass="optionLabel"/>

        <h:selectone_menu  styleClass="optionValue"
               binding="#{carstore.currentModel.components.transmission}"/>

      </h:panel_grid>

   <h:output_text value="#{bundle.OtherOptions}" 
                   styleClass="optionLabel"/>

   <h:panel_grid columns="6">

     <h:selectboolean_checkbox title="#{bundle.sunroofLabel}" 
         alt="#{bundle.sunroofLabel}" 
         binding="#{carstore.currentModel.components.sunroof}">
     </h:selectboolean_checkbox>    

     <h:output_text value="#{bundle.sunroofLabel}" /> 

     <h:selectboolean_checkbox  title="#{bundle.cruiseLabel}"  
         binding="#{carstore.currentModel.components.cruisecontrol}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.cruiseLabel}" /> 

     <h:selectboolean_checkbox title="#{bundle.keylessLabel}"  
          alt="#{bundle.keylessLabel}"
          binding="#{carstore.currentModel.components.keylessentry}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.keylessLabel}" /> 

     <h:selectboolean_checkbox 
        title="#{bundle.securityLabel}"  alt="#{bundle.securityLabel}"
        binding="#{carstore.currentModel.components.securitySystem}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.securityLabel}" />  

     <h:selectboolean_checkbox  title="#{bundle.skiRackLabel}"  
         alt="#{bundle.skiRackLabel}" 
         binding="#{carstore.currentModel.components.skiRack}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.skiRackLabel}" /> 

     <h:selectboolean_checkbox  title="#{bundle.towPkgLabel}"  
           alt="#{bundle.towPkgLabel}" 
           binding="#{carstore.currentModel.components.towPackage}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.towPkgLabel}" /> 

     <h:selectboolean_checkbox  title="#{bundle.gpsLabel}" 
         alt="#{bundle.gpsLabel}"
         binding="#{carstore.currentModel.components.gps}" >
     </h:selectboolean_checkbox>

     <h:output_text value="#{bundle.gpsLabel}" /> 

   </h:panel_grid>

</f:subview>
