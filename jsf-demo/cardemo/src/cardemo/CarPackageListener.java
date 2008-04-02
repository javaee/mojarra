/*
 * $Id: CarPackageListener.java,v 1.4 2003/12/17 15:17:34 rkitain Exp $
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

// CarPackageListener.java

package cardemo;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CarPackageListener handles all the ActionEvents generated as a result
 * of a "package" action on command_button components of the car demo app.
 */
public class CarPackageListener extends CarActionListener implements ActionListener {
    
    private static Log log = LogFactory.getLog(CarPackageListener.class);
    
    public CarPackageListener() {
    }


    public void processAction(ActionEvent event) {
        log.debug("CarPackageListener.processAction");
        FacesContext context = FacesContext.getCurrentInstance();
        processPackage(context, event);
    }

    /**
     * Handles the selection of the particular package. Updates the state
     * of the component representing different options and packages.
     */
    private void processPackage(FacesContext context, ActionEvent event) {
        UIComponent component = event.getComponent();

        // Note: Name of the Package resources file must match the key attribute
        // of the UICommand component. The key was chosen so that it is not
        // dependent in any locale
        String packageAction = (String)((UICommand)component).getAction();
        (Util.getValueBinding("#{CarServer.currentPackageName}")).
                setValue(context, packageAction);
        updateComponentState(context, component);
        changeButtonStyle(packageAction, component);
    }     
} // end of class CarPackageListener
