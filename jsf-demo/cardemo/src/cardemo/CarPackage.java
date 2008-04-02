/*
 * $Id: CarPackage.java,v 1.2 2003/08/28 08:22:17 rkitain Exp $
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

package cardemo;

import java.util.*;
import java.io.*;

import java.util.Properties;
import java.util.Enumeration;

import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;

/**
 * CarPackage acts a model object to access various options available in a
 * particular package. The lifetime of this class is the same
 * as the lifetime of the CarServer since it is created and maintained by
 * CarServer.
 */
public class CarPackage extends Object {
   
    /**
     * This value represents whether the particular option is available for
     * this package.
     * Value of 0 represents, the option will be enabled and unselected
     * Value of 1 represents, the option will be disbaled and selected.
     * Value of 2 represents, the option will be disabled and unselected.
     */
    protected int sunRoof = 0;
    /**
     * This value represents whether this option is currently selected by
     * the buyer or not.
     */
    protected boolean sunRoofSelected = false;
    
    protected int cruiseControl = 0;
    protected boolean cruiseControlSelected = false;
    
    protected int keylessEntry = 0;
    protected boolean keylessEntrySelected = false;
    
    protected int securitySystem = 0;
    protected boolean securitySystemSelected = false;
    
    protected int skiRack = 0;
    protected boolean skiRackSelected = false;
    
    protected int towPackage = 0;
    protected boolean towPackageSelected = false;
    
    protected int gps = 0;
    protected boolean gpsSelected = false;
 
    /**
     * List of all options available for this package. The key is the option
     * itself and its value is whether it is avilable for this package.
     */
    protected Properties packageProperties;
    
    public CarPackage() {
        super();
    }     
    public CarPackage(Properties props) throws IOException {
        super();
        this.packageProperties = props;
        setSunRoof(Integer.parseInt((String)props.get("sunroof")));
        setKeylessEntry(Integer.parseInt((String)props.get("keylessentry")));
        setCruiseControl(Integer.parseInt((String)props.get("cruisecontrol")));
        setSecuritySystem(Integer.parseInt((String)props.get("securitySystem")));
        setSkiRack(Integer.parseInt((String)props.get("skirack")));
        setTowPackage(Integer.parseInt((String)props.get("towPackage")));
        setGps(Integer.parseInt((String)props.get("gps")));
    }
 
   public Properties getPackageProperties() {
       return packageProperties;
   }
   
   public void setPackageProperties(Properties props) {
       this.packageProperties = props;
   }
   
    public void setSunRoof(int roof) {
        sunRoof = roof;
    }
    
    public int getSunRoof() {
        return sunRoof;
    }
    
    public void setSunRoofSelected(boolean roof) {
        sunRoofSelected = roof;
    }

    public boolean getSunRoofSelected() {
        return sunRoofSelected;
    }
    
    public void setCruiseControl(int cruise) {
        cruiseControl = cruise;
    }
    
    public int getCruiseControl() {
        return cruiseControl;
    }

    public void setCruiseControlSelected(boolean cruise) {
        cruiseControlSelected = cruise;
    }

    public boolean getCruiseControlSelected() {
        return cruiseControlSelected;
    }
    
    public void setKeylessEntry(int entry) {
        keylessEntry = entry;
    }
    
    public int getKeylessEntry() {
        return keylessEntry;
    }
    
    public void setKeylessEntrySelected(boolean entry) {
        keylessEntrySelected = entry;
    }

    public boolean getKeylessEntrySelected() {
        return keylessEntrySelected;
    }

    public void setSecuritySystem(int security) {
        securitySystem = security;
    }
    
    public int getSecuritySystem() {
        return securitySystem;
    }
    
    public void setSecuritySystemSelected(boolean security) {
        securitySystemSelected = security;
    }

    public boolean getSecuritySystemSelected() {
        return securitySystemSelected;
    }

    public void setSkiRack(int ski) {
        skiRack = ski;
    }
    
    public int getSkiRack() {
        return skiRack;
    }
    
    public void setSkiRackSelected(boolean ski) {
        skiRackSelected = ski;
    }

    public boolean getSkiRackSelected() {
        return skiRackSelected;
    }

    public void setTowPackage(int tow) {
        towPackage = tow;
    }
    
    public int getTowPackage() {
        return towPackage;
    }
    
    public void setTowPackageSelected(boolean tow) {
        towPackageSelected = tow;
    }

    public boolean getTowPackageSelected() {
        return towPackageSelected;
    }

    public void setGps(int g) {
        gps = g;
    }
    
    public int getGps() {
        return gps;
    }

    public boolean getGpsSelected() {
        return gpsSelected;
    }

    public void setGpsSelected(boolean g) {
        gpsSelected = g;
    }
}
