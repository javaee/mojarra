/*
 * $Id: PackageAction.java,v 1.2 2003/02/04 00:10:41 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// PackageAction.java

package cardemo;

import com.sun.faces.util.Util;
import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 *  <B>PackageAction</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: PackageAction.java,v 1.2 2003/02/04 00:10:41 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class PackageAction implements ActionListener {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public PackageAction() {
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
        System.out.println("PackageAction.processAction : actionCommand : "+
            event.getActionCommand());
        String actionCommand = event.getActionCommand();

        if (actionCommand.equals("custom")) {
            processCustom(event);
        } else if (actionCommand.equals("standard")) {
            processStandard(event);
        } else if (actionCommand.equals("performance")) {
            processPerformance(event);
        } else if (actionCommand.equals("deluxe")) {
            processDeluxe(event);
        }
    }

    // helper method to set UI values for "custom" package selection

    private void processCustom(ActionEvent event) {
        UIComponent component = event.getComponent();
        int i = 0;
        UIComponent foundComponent = null;

//PENDING(rogerk) application data should be read from config file..

        FacesContext context = FacesContext.getCurrentInstance();
        String[] engines = {"V4", "V6", "V8"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        context.setModelValue("CurrentOptionServer.engineOption", engineOption);

        String[] suspensions = {"regular", "performance"};
        ArrayList suspensionOption = new ArrayList(suspensions.length);
        for (i=0; i<suspensions.length; i++) {
            suspensionOption.add(new SelectItem(suspensions[i],
                suspensions[i], suspensions[i]));
        }
        context.setModelValue("CurrentOptionServer.suspensionOption",
            suspensionOption);

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "false");

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "false");

//PENDING(rogerk)get locale specific string for button labels

        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("label", "Custom *");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("label", "Standard");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("label", "Performance");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("label", "Deluxe");
    }

    // helper method to set UI values for "standard" package selection

    private void processStandard(ActionEvent event) {
        UIComponent component = event.getComponent();
        int i = 0;
        UIComponent foundComponent = null;

//PENDING(rogerk) application data should be read from config file..
 
        FacesContext context = FacesContext.getCurrentInstance();
        String[] engines = {"V4", "V6"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        context.setModelValue("CurrentOptionServer.engineOption", engineOption);

//PENDING(rogerk)get locale specific string

        String[] suspensions = {"regular"};
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        context.setModelValue("CurrentOptionServer.suspensionOption",
            suspensionOption);

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");

//PENDING(rogerk)get locale specific string for button labels

        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("label", "Custom");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("label", "Standard *");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("label", "Performance");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("label", "Deluxe");
        
        context.renderResponse();
    }

    // helper method to set UI values for "performance" package selection

    private void processPerformance(ActionEvent event) {
        UIComponent component = event.getComponent();
        UIComponent foundComponent = null;

//PENDING(rogerk) application data should be read from config file..

        FacesContext context = FacesContext.getCurrentInstance();
        String[] engines = {"V8"};
        ArrayList engineOption = new ArrayList();
        engineOption.add(new SelectItem(engines[0], engines[0], engines[0]));
        context.setModelValue("CurrentOptionServer.engineOption", engineOption);

//PENDING(rogerk)get locale specific string

        String[] suspensions = {"performance"};
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        context.setModelValue("CurrentOptionServer.suspensionOption",
            suspensionOption);

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

//PENDING(rogerk)get locale specific string for button labels

        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("label", "Custom");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("label", "Standard");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("label", "Performance *");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("label", "Deluxe");

        context.renderResponse();
    }

    // helper method to set UI values for "deluxe" package selection

    private void processDeluxe(ActionEvent event) {
        UIComponent component = event.getComponent();
        UIComponent foundComponent = null;
        int i = 0;

        FacesContext context = FacesContext.getCurrentInstance();

//PENDING(rogerk) application data should be read from config file..

        String[] engines = {"V4", "V6", "V8"};
        ArrayList engineOption = new ArrayList(engines.length);
        for (i=0; i<engines.length; i++) {
            engineOption.add(new SelectItem(
                engines[i], engines[i], engines[i]));
        }
        context.setModelValue("CurrentOptionServer.engineOption", engineOption);

//PENDING(rogerk)get locale specific string

        String[] suspensions = {"performance"};
        ArrayList suspensionOption = new ArrayList();
        suspensionOption.add(new SelectItem(suspensions[0],
            suspensions[0], suspensions[0]));
        context.setModelValue("CurrentOptionServer.suspensionOption",
            suspensionOption);

        foundComponent = component.findComponent("sunroof");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("securitySystem");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("gps");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("cruisecontrol");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("skirack");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("keylessentry");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

        foundComponent = component.findComponent("towPackage");
        foundComponent.setAttribute("disabled", "true");
        foundComponent.setAttribute("value", "true");

//PENDING(rogerk)get locale specific string for button labels

        foundComponent = component.findComponent("custom");
        foundComponent.setAttribute("label", "Custom");

        foundComponent = component.findComponent("standard");
        foundComponent.setAttribute("label", "Standard");

        foundComponent = component.findComponent("performance");
        foundComponent.setAttribute("label", "Performance");

        foundComponent = component.findComponent("deluxe");
        foundComponent.setAttribute("label", "Deluxe *");

        context.renderResponse();
    }

} // end of class PackageAction
