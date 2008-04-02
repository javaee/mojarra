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

package cardemo;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.util.Util;

/**
 * PackageValueChanged gets called when any of the package options for a
 * car in the more.jsp page changes
 */
public class PackageValueChanged implements ValueChangedListener {
    
    private static Log log = LogFactory.getLog(PackageValueChanged.class);

    /** 
     * Creates a new instance of PackageValueChanged 
     */
    public PackageValueChanged() {
    }
    
    
    public PhaseId getPhaseId() {
        return PhaseId.PROCESS_VALIDATIONS;
    }
    
    
    public void processValueChanged(ValueChangedEvent vEvent) {
        try {
            log.debug("ValueChangedEvent processEvent");
            String componentId = vEvent.getComponent().getComponentId();
            // handle each valuechangedevent here
            FacesContext context = FacesContext.getCurrentInstance();
            String currentPrice;
            int cPrice = 0;
            currentPrice = (String)
            (Util.getValueBinding("CarServer.carCurrentPrice"))
            .getValue(context);
            cPrice = Integer.parseInt(currentPrice);
            log.debug("Component Id: "+componentId);
            log.debug("vEvent.getOldValue: "+vEvent.getOldValue());
            log.debug("vEvent.getNewValue: "+vEvent.getNewValue());
            
            log.debug("Vevent name: " + (vEvent.getNewValue()).getClass().getName());
            // the if is for the SelectItems; else is for checkboxes
            if ((componentId.equals("currentEngine")) ||
                (componentId.equals("currentBrake")) ||
                (componentId.equals("currentSuspension")) ||
                (componentId.equals("currentSpeaker")) ||
                (componentId.equals("currentAudio")) ||
                (componentId.equals("currentTransmission"))) {
                log.debug("vEvent.getOldValue: "+vEvent.getOldValue());
                log.debug("vEvent.getNewValue: "+vEvent.getNewValue());

                cPrice = cPrice - (getPriceFor(((String)vEvent.getOldValue()), 
                        context));
                cPrice = cPrice + (getPriceFor(((String)vEvent.getNewValue()), 
                         context)); 
            } else {
                Boolean optionSet = (Boolean)vEvent.getNewValue();
                cPrice = calculatePrice(componentId, vEvent, cPrice, context); 
            }
            // update model value
            currentPrice = Integer.toString(cPrice);
            (Util.getValueBinding("CarServer.carCurrentPrice")).
            setValue(context,currentPrice);
        } catch (NumberFormatException ignored) {}
        
    }

    /**
     * Updates the price of the Car based for a particular option being
     * selected or deselected.
     */
    public int calculatePrice(String optionKey,ValueChangedEvent vEvent, 
            int cPrice, FacesContext context) {
        Boolean optionSet = (Boolean)vEvent.getNewValue();   
        Boolean oldValue = (Boolean)vEvent.getOldValue(); 
        if (optionSet.equals(Boolean.TRUE)) {
            cPrice = cPrice + (getPriceFor(optionKey, context));
        } else {
            cPrice = cPrice - (getPriceFor(optionKey, context));
        }
        return cPrice;
    }
    
    /**
     * Returns the price for a particular option.
     */
    public int getPriceFor(String option, FacesContext context) {
        if (option == null ) {
            return 0;
        }    
        CarServer carServer =(CarServer)
                (Util.getValueBinding("CarServer")).getValue(context);    
        return carServer.getPriceForOption(option);
    }
    
}
