/*
 * $Id: CarActionListener.java,v 1.4 2003/03/27 19:43:27 jvisvanathan Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

// CarActionListener.java

package cardemo;

import com.sun.faces.util.Util;
import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangedEvent;
import javax.faces.el.ValueBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *  <B>CarActionListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CarActionListener.java,v 1.4 2003/03/27 19:43:27 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CarActionListener implements ActionListener {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

    private static Log log = LogFactory.getLog(CarActionListener.class);
// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public CarActionListener() {
    }

//
// Class methods
//

//
// General Methods
//

    
    // This listener will process events after the phase specified.
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void processAction(ActionEvent event) {
        log.debug("CarActionListener.processAction : actionCommand : "+
            event.getActionCommand());
        String actionCommand = event.getActionCommand();

        ResourceBundle rb = ResourceBundle.getBundle(
            "cardemo/Resources", (FacesContext.getCurrentInstance().
                getLocale()));

        if (actionCommand.equals("custom")) {
            processCustom(event, rb);
        } else if (actionCommand.equals("standard")) {
            processStandard(event, rb);
        } else if (actionCommand.equals("performance")) {
            processPerformance(event, rb);
        } else if (actionCommand.equals("deluxe")) {
            processDeluxe(event, rb);
        } 
        else if (actionCommand.equals("recalculate")) {
            FacesContext context = FacesContext.getCurrentInstance();
            String currentPackage = (String)(Util.getValueBinding(
                "CurrentOptionServer.currentPackage")).getValue(context);
            if (currentPackage.equals("custom")) {
                 processCustom(event, rb);
            } else if (currentPackage.equals("standard")) {
                processStandard(event, rb);
            } else if (currentPackage.equals("performance")) {
                processPerformance(event, rb);
            } else if (currentPackage.equals("deluxe")) {
                processDeluxe(event, rb);
            }
        } else if (actionCommand.equals("buy")) {
            FacesContext context = FacesContext.getCurrentInstance();
            String carPrice = (String)
            (Util.getValueBinding("CurrentOptionServer.carCurrentPrice")).
                getValue(context);
            (Util.getValueBinding("CurrentOptionServer.packagePrice")).
                setValue(context, carPrice);
        }
            
            
    }

    // helper method to set UI values for "custom" package selection

    private void processCustom(ActionEvent event, ResourceBundle rb) {
        UIComponent component = event.getComponent();
        int i = 0;
        UIComponent foundComponent = null;
        UIOutput uiOutput = null;
//PENDING(rogerk) application data should be read from config file..

        FacesContext context = FacesContext.getCurrentInstance();

        String[] engines = {"V4", "V6", "V8"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        Util.getValueBinding("CurrentOptionServer.engineOption").
                setValue(context,engineOption);
        foundComponent = component.findComponent("currentEngine");
        uiOutput = (UIOutput) component;
        String value = null;
        boolean packageChange = false;
        if (!((Util.getValueBinding("CurrentOptionServer.currentPackage"))
                .getValue(context)).equals("custom")) {
            value = engines[0];
            packageChange = true;
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentEngineOption")).
                setValue(context,value);
        String[] suspensions = new String[2];
        suspensions[0] = (String)rb.getObject("Regular");
        suspensions[1] = (String)rb.getObject("Performance");
        ArrayList suspensionOption = new ArrayList(suspensions.length);
        for (i=0; i<suspensions.length; i++) {
            suspensionOption.add(new SelectItem(suspensions[i],
                suspensions[i], suspensions[i]));
        }
        (Util.getValueBinding("CurrentOptionServer.suspensionOption")).
                setValue(context, suspensionOption);
        
        foundComponent = component.findComponent("currentSuspension");
        uiOutput = (UIOutput) component;
        if (packageChange) {
            value = suspensions[0];
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentSuspensionOption")).
            setValue(context, value);

        (Util.getValueBinding("CurrentOptionServer.currentPackage")).
                setValue(context,"custom");

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.sunRoof")).
        setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.securitySystem")).
            setValue(context, Boolean.FALSE);

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.gps")).
            setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.cruiseControl")).
            setValue(context, Boolean.FALSE);

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.skiRack")).
            setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.keylessEntry")).
            setValue(context, Boolean.FALSE);

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "false");
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.towPackage")).
        setValue(context,Boolean.FALSE);

        // Display locale specific button labels..

        String buttonLabel = null;
        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("commandClass", "package-selected");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("commandClass", "package-unselected");

    }

    // helper method to set UI values for "standard" package selection

    private void processStandard(ActionEvent event, ResourceBundle rb) {
        UIComponent component = event.getComponent();
        int i = 0;
        UIComponent foundComponent = null;
        UIOutput uiOutput = null;
//PENDING(rogerk) application data should be read from config file..
 
        FacesContext context = FacesContext.getCurrentInstance();

        String[] engines = {"V4", "V6"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        (Util.getValueBinding("CurrentOptionServer.engineOption")).
        setValue(context,engineOption);

        foundComponent = component.findComponent("currentEngine");
        uiOutput = (UIOutput) component;
        String value = null;
        boolean packageChange = false;
        if (!((Util.getValueBinding("CurrentOptionServer.currentPackage"))
            .getValue(context)).equals("standard")) {
            value = engines[0];
            packageChange = true;
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentEngineOption")).
        setValue(context,value);

        String[] suspensions = new String[1];
        suspensions[0] = (String)rb.getObject("Regular");
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        (Util.getValueBinding("CurrentOptionServer.suspensionOption")).
        setValue(context,suspensionOption);
        foundComponent = component.findComponent("currentSuspension");
        uiOutput = (UIOutput) component;
        if (packageChange) {
            value = suspensions[0];
        } else {
            value = (String)uiOutput.getValue();
        } 
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentSuspensionOption")).
        setValue(context, value);

       (Util.getValueBinding("CurrentOptionServer.currentPackage")).
       setValue(context, "standard");

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.sunRoof")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.FALSE);
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.securitySystem")).
        setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.FALSE);
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.gps")).
        setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.cruiseControl")).
        setValue(context, Boolean.TRUE);

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.skiRack")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.keylessEntry")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.FALSE);
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.towPackage")).
        setValue(context,Boolean.FALSE);

        // Display locale specific button labels..

        String buttonLabel = null;
        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("commandClass", "package-selected");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("commandClass", "package-unselected");

    }

    // helper method to set UI values for "performance" package selection

    private void processPerformance(ActionEvent event, ResourceBundle rb) {
        UIComponent component = event.getComponent();
        // "value" is an attibute on UIOutput and subclasses of UIOutput
        // only. since we are dealing with ValueChangedEvent here, it is safe
        // to cast the component to UIOutput.
        UIComponent foundComponent = null;
        UIOutput uiOutput = null;
//PENDING(rogerk) application data should be read from config file..

        FacesContext context = FacesContext.getCurrentInstance();

        String[] engines = {"V8"};
        ArrayList engineOption = new ArrayList();
        engineOption.add(new SelectItem(engines[0], engines[0], engines[0]));
        (Util.getValueBinding("CurrentOptionServer.engineOption")).
        setValue(context,engineOption);

        foundComponent = component.findComponent("currentEngine");
        uiOutput = (UIOutput) component;
        String value = null;
        boolean packageChange = false;
        if (!((Util.getValueBinding("CurrentOptionServer.currentPackage")).
            getValue(context)).equals("performance")) {
            value = engines[0];
            packageChange = true;
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentEngineOption")).
         setValue(context,value);

        String[] suspensions = new String[1];
        suspensions[0] = (String)rb.getObject("Performance");
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        (Util.getValueBinding("CurrentOptionServer.suspensionOption")).
            setValue(context,suspensionOption);
        foundComponent = component.findComponent("currentSuspension");
        if (packageChange) {
            value = suspensions[0];
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentSuspensionOption")).
            setValue(context,value);

        (Util.getValueBinding("CurrentOptionServer.currentPackage")).
        setValue(context,"performance");

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.sunRoof")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.FALSE);
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.securitySystem")).
        setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.FALSE);
        foundComponent.setAttribute("selectbooleanClass", "option-unselected");
        (Util.getValueBinding("CurrentOptionServer.gps")).
        setValue(context,Boolean.FALSE);

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.cruiseControl")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.skiRack")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.keylessEntry")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.towPackage")).
        setValue(context,Boolean.TRUE);

        // Display locale specific button labels..

        String buttonLabel = null;
        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("commandClass", "package-selected");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("commandClass", "package-unselected");

    }

    // helper method to set UI values for "deluxe" package selection

    private void processDeluxe(ActionEvent event, ResourceBundle rb) {
        UIComponent component = event.getComponent();
        UIComponent foundComponent = null;
        UIOutput uiOutput = null;   
     
        int i = 0;

        FacesContext context = FacesContext.getCurrentInstance();

//PENDING(rogerk) application data should be read from config file..

        String[] engines = {"V4", "V6", "V8"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        (Util.getValueBinding("CurrentOptionServer.engineOption")).
        setValue(context, engineOption);

        foundComponent = component.findComponent("currentEngine");
        uiOutput = (UIOutput) component;
        String value = null;
        boolean packageChange = false;
        if (!((Util.getValueBinding("CurrentOptionServer.currentPackage")).
            getValue(context)).equals("deluxe")) {
            value = engines[0];
            packageChange = true;
        } else {
            value = (String)uiOutput.getValue();
        }
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentEngineOption")).
            setValue(context, value);

        String[] suspensions = new String[1];
        suspensions[0] = (String)rb.getObject("Performance");
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        (Util.getValueBinding("CurrentOptionServer.suspensionOption")).
            setValue(context, suspensionOption);

        foundComponent = component.findComponent("currentSuspension");
        uiOutput = (UIOutput) component;
        if (packageChange) {
            value = suspensions[0];
        } else {
            value = (String)uiOutput.getValue();
        } 
        uiOutput.setValue(value);
        (Util.getValueBinding("CurrentOptionServer.currentSuspensionOption")).
            setValue(context,value);

        (Util.getValueBinding("CurrentOptionServer.currentPackage")).
        setValue(context,"deluxe");

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.sunRoof")).
        setValue(context, Boolean.TRUE);

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.securitySystem")).
        setValue(context, Boolean.TRUE);

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.gps")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.cruiseControl")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.skiRack")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.keylessEntry")).
        setValue(context,Boolean.TRUE);

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", Boolean.TRUE);
        foundComponent.setAttribute("selectbooleanClass", "package-selected");
        (Util.getValueBinding("CurrentOptionServer.towPackage")).
        setValue(context,Boolean.TRUE);

        // Display locale specific button labels..

        String buttonLabel = null;
        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("commandClass", "package-unselected");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("commandClass", "package-selected");

    }

} // end of class CarActionListener
