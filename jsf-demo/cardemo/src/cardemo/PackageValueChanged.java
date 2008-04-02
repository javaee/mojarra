/*
 *
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
 *
 */

package cardemo;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;

import java.io.IOException;
import java.util.ResourceBundle;


/**
 * PackageValueChanged gets called when any of the package options for a
 * car in the more.jsp page changes
 */
public class PackageValueChanged implements ValueChangedListener {
    
    /** Creates a new instance of PackageValueChanged */
    public PackageValueChanged() {
    }
    
    
    public PhaseId getPhaseId() {
        return PhaseId.PROCESS_VALIDATIONS;
    }
    
    
    public void processValueChanged(ValueChangedEvent vEvent) {
        try {
            System.out.println("ValueChangedEvent processEvent");
            // handle each valuechangedevent here
            FacesContext context = FacesContext.getCurrentInstance();
            String currentPrice;
            int cPrice = 0;
            currentPrice = (String)context.getModelValue("CurrentOptionServer.carCurrentPrice");
            cPrice = Integer.parseInt(currentPrice);
            String componentId = vEvent.getComponent().getComponentId();
            System.out.println("Component Id: "+componentId);
            System.out.println("vEvent.getOldValue: "+vEvent.getOldValue());
            System.out.println("vEvent.getNewValue: "+vEvent.getNewValue());
            
            System.out.println("Vevent name: " + (vEvent.getNewValue()).getClass().getName());
            
            if ((componentId.equals("currentEngine")) ||
            (componentId.equals("currentBrake")) ||
            (componentId.equals("currentSuspension")) ||
            (componentId.equals("currentSpeakers")) ||
            (componentId.equals("currentAudio")) ||
            (componentId.equals("currentTransmission"))) {
                System.out.println("vEvent.getOldValue: "+vEvent.getOldValue());
                System.out.println("vEvent.getNewValue: "+vEvent.getNewValue());
                
                cPrice = cPrice - (this.getPriceFor((String)vEvent.getOldValue()));
                cPrice = cPrice + (this.getPriceFor((String)vEvent.getNewValue()));
                //cPrice = cPrice + 100;
            }
            
            else if( (componentId.equals("sunroof")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("sunrooftrue"));
            }
            else if( (componentId.equals("cruisecontrol")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("cruisecontroltrue"));
            }
            else if( (componentId.equals("keylessentry")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("keylessentrytrue"));
            }
            else if( (componentId.equals("securitySystem")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("securitySystemtrue"));
            }
            else if( (componentId.equals("skirack")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("skiracktrue"));
            }
            else if( (componentId.equals("towPackage")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("towPackagetrue"));
            }
            else if( (componentId.equals("gps")) && (((Boolean)vEvent.getNewValue()).equals(Boolean.TRUE))) {
                cPrice = cPrice + (this.getPriceFor("gpstrue"));
            }
            
            // update model value
            currentPrice = Integer.toString(cPrice);
            context.setModelValue("CurrentOptionServer.carCurrentPrice", currentPrice);
        } catch (NumberFormatException ignored) {}
        
    }
    
    //PENDING(rajprem): this information should eventually 
    //go into CarOptionsn.properties
    public int getPriceFor(String option) {
        ResourceBundle rb = ResourceBundle.getBundle(
        "cardemo/Resources", (FacesContext.getCurrentInstance().getLocale()));

        if (option.equals("V4")) {
            return (100);
        }
        else if (option.equals("V6")) {
            return (200);
        }
        else if (option.equals("V8")) {
            return (300);
        }
        else if (option.equals((String)rb.getObject("Disc"))) {
            return (100);
        }
        else if (option.equals((String)rb.getObject("Drum"))) {
            return (200);
        }
        else if (option.equals((String)rb.getObject("Regular"))) {
            return (150);
        }
        else if (option.equals((String)rb.getObject("Performance"))) {
            return (300);
        }
        else if (option.equals("4")) {
            return (100);
        }
        else if (option.equals("6")) {
            return (200);
        }
        else if (option.equals((String)rb.getObject("Standard"))) {
            return (100);
        }
        else if (option.equals((String)rb.getObject("Premium"))) {
            return (200);
        }
        else if (option.equals((String)rb.getObject("Auto"))) {
            return (300);
        }
        else if (option.equals((String)rb.getObject("Manual"))) {
            return (200);
        }
        else if (option.equals("sunrooffalse")) {
            return (0);
        }
        else if (option.equals("sunrooftrue")) {
            return (100);
        }
        else if (option.equals("cruisecontrolfalse")) {
            return (0);
        }
        else if (option.equals("cruisecontroltrue")) {
            return (150);
        }
        else if (option.equals("keylessentryfalse")) {
            return (0);
        }
        else if (option.equals("keylessentrytrue")) {
            return (100);
        }
        else if (option.equals("securitySystemfalse")) {
            return (0);
        }
        else if (option.equals("securitySystemtrue")) {
            return (100);
        }
        else if (option.equals("skirackfalse")) {
            return (0);
        }
        else if (option.equals("skiracktrue")) {
            return (200);
        }
        else if (option.equals("towPackagefalse")) {
            return (0);
        }
        else if (option.equals("towPackagetrue")) {
            return (200);
        }
        else if (option.equals("gpsfalse")) {
            return (0);
        }
        else if (option.equals("gpstrue")) {
            return (200);
        }
        else return 0;
        
    }
    
    
}
