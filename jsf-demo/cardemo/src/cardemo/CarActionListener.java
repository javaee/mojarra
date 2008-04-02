/*
 * $Id: CarActionListener.java,v 1.13 2003/12/17 15:17:33 rkitain Exp $
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

import java.util.Enumeration;
import java.util.Properties;

/**
 * CarActionListener handles all the ActionEvents generated as a result
 * of some action on command_button components of the car demo app.
 */
public class CarActionListener implements ActionListener {
    
    private static Log log = LogFactory.getLog(CarActionListener.class);
    
    public CarActionListener() {
    }

    public void processAction(ActionEvent event) {
        log.debug("CarActionListener.processAction");
        FacesContext context = FacesContext.getCurrentInstance();

	UIComponent component = event.getComponent();
	String actionCommand = ((UICommand)component).getAction();
        processActionCommand(actionCommand);  
        // if user has already selected a car, we need to reset the state of 
        // the component. If the car is being chosen for the first time,this
        // will be a no-op. But if a car is chosen and for some reason, if
        // the user decides to start all over from StoreFront page, then we
        // need to reset the package back to "Custom", so that old package
        // selections are lost.
        (Util.getValueBinding("#{CarServer.currentPackageName}")).
            setValue(context, "Custom"); 
        updateComponentState(context, event.getComponent());
        changeButtonStyle("Custom", event.getComponent()); 
    }

    /**
     * Updates the state of the component that represents different options
     * based on the the options being available or not for the selected
     * package
     */
    protected void updateComponentState(FacesContext context, 
        UIComponent component) {
        UIComponent foundComponent = null;
        // get the available option for car package selected
        Properties packageProps = (Properties) 
        ((Util.getValueBinding("#{CarServer.currentPackage.packageProperties}")).
                getValue(context));
        if ( packageProps == null) {
            return;
        }

        // depending on each option for this package being available or not, 
        // for every option available for the package, locate the component that
        // is represented by the option and set the relevant attributes.
        // NOTE: componentIds must match exactly the keys used to name the 
        // options in the package resources files.
        Enumeration propsIt = packageProps.keys();
        while ( propsIt.hasMoreElements() ) {
            String componentId = (String) propsIt.nextElement();    
            String propValue = (String) packageProps.get(componentId);
            foundComponent = component.findComponent(componentId);
            if (foundComponent == null ) {
                return;
            }
            String packageName = (String)
                (Util.getValueBinding("#{CarServer.currentPackageName}")).
                getValue(context); 
            
            // propValue of 0 represents, the option will be enabled and 
            // unselected.
            // propValue of 1 represents, the option will be disabled and 
            // selected.
            // propValue of 2 represents, the option will be disabled and 
            // unselected.
            if ( propValue.equals("0")) {
                foundComponent.getAttributes().put("styleClass", 
                    "option-unselected");
                foundComponent.getAttributes().put("disabled", Boolean.FALSE);
                // if we do not check for Custom here, current value will be lost
                // and the value will be set to false.
                if (!(packageName.equals("Custom"))) {
                    ((UISelectBoolean)foundComponent).setValue(Boolean.FALSE);
                }    
            } else if (propValue.equals("1")){
                foundComponent.getAttributes().put("styleClass", 
                    "package-selected");
                foundComponent.getAttributes().put("disabled", Boolean.TRUE);
                ((UISelectBoolean)foundComponent).setValue(Boolean.TRUE);
            }  else if (propValue.equals("2")) {
                 foundComponent.getAttributes().put("styleClass", 
                    "package-selected");
                foundComponent.getAttributes().put("disabled", Boolean.TRUE);
                ((UISelectBoolean)foundComponent).setValue(Boolean.FALSE);
            } // end of if  
        }  // end of while
     }

    /**
     * Changes the style of the buttons representing different packages
     * based on the currently selected package.
     */
    public void changeButtonStyle(String packName, UIComponent component) {
        UIComponent foundComponent = null;
        foundComponent = component.findComponent("Custom");
        if (foundComponent == null) {
            return;
        }
        if ( packName.equals("Custom")) {
            foundComponent.getAttributes().put("styleClass", "package-selected");
        } else {
            foundComponent.getAttributes().put("styleClass", "package-unselected"); 
        }
        
        foundComponent = component.findComponent("Standard");
        if (foundComponent == null) {
            return;
        }
        if ( packName.equals("Standard")) {
            foundComponent.getAttributes().put("styleClass", "package-selected");
        } else {
            foundComponent.getAttributes().put("styleClass", "package-unselected"); 
        }
        
        foundComponent = component.findComponent("Performance");
        if (foundComponent == null) {
            return;
        }
        if ( packName.equals("Performance")) {
            foundComponent.getAttributes().put("styleClass", "package-selected");
        } else {
            foundComponent.getAttributes().put("styleClass", "package-unselected"); 
        }
        
        foundComponent = component.findComponent("Deluxe");
        if (foundComponent == null) {
            return;
        }
        if ( packName.equals("Deluxe")) {
            foundComponent.getAttributes().put("styleClass", "package-selected");
        } else {
            foundComponent.getAttributes().put("styleClass", "package-unselected"); 
        }
    }
   
    /**
     * Sets the CarId of the currently chosen Car.
     */
    protected void processActionCommand(String actionCommand) {
         // get CurrentOptionServer
        FacesContext context = FacesContext.getCurrentInstance();
        int carId = 0;
        if (actionCommand.equals("more1")) {
            carId = 1;
        } else if (actionCommand.equals("more2")) {
            carId = 2;
        } else if (actionCommand.equals("more3")) {
            carId = 3;
        } else if (actionCommand.equals("more4")) {
            carId = 4;
          
        } 
        if ( carId != 0 ) {
            (Util.getValueBinding("#{CarServer.carId}")).
                    setValue(context, new Integer(carId));
        }    
    }
} // end of class CarActionListener
