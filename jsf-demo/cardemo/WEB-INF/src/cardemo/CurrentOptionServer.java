/*
 * $Id: CurrentOptionServer.java,v 1.2 2002/10/07 22:53:05 jball Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import java.util.*;
import java.io.*;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.util.Properties;

import javax.faces.component.SelectItem;

import java.io.*;

public class CurrentOptionServer extends Object {
    
    
    protected String thisUrl = "/current.gif";
    protected int carId = 1;
    protected String carTitle = "You shouldn't see this title";
    protected String carDesc = "This description should never be seen. If it is, your properties files aren't being read.";
    protected String basePrice = "$300";
    protected String currentPrice = "$250";
    protected String engines[] = {
        "V4", "V6", "V8"
    };
    protected String brakes[] = {
        "disk", "drum"
    };
    protected String suspensions[] = {
        "regular", "performance"
    };
    protected String speakers[] = {
        "4", "6"
    };
    protected String audio[] = {
        "standard", "premium"
    };
    protected String transmissions[] = {
        "auto", "manual"
    };
    protected ArrayList engineOption;
    protected Object currentEngineOption = engines[0];
    protected ArrayList brakeOption;
    protected Object currentBrakeOption = brakes[0];
    protected ArrayList suspensionOption;
    protected Object currentSuspensionOption = suspensions[0];
    protected ArrayList speakerOption;
    protected Object currentSpeakerOption = speakers[0];
    protected ArrayList audioOption;
    protected Object currentAudioOption = audio[0];
    protected ArrayList transmissionOption;
    protected Object currentTransmissionOption = transmissions[0];
    protected boolean sunRoof = false;
    protected boolean cruiseControl = false;
    protected boolean keylessEntry = false;
    protected boolean securitySystem = false;
    protected boolean skiRack = false;
    protected boolean towPackage = false;
    protected boolean gps = false;
    
    
    
    public CurrentOptionServer() {
        super();
        
        System.out.println("CurrentOptionServer created");
        
        engineOption = new ArrayList(engines.length);
        brakeOption = new ArrayList(brakes.length);       
        suspensionOption = new ArrayList(suspensions.length);        
        speakerOption = new ArrayList(speakers.length);        
        audioOption = new ArrayList(audio.length);        
        transmissionOption = new ArrayList(transmissions.length);
        
        int i = 0;
        
        for (i = 0; i < engines.length; i++) {
            engineOption.add(new SelectItem(engines[i], engines[i], engines[i]));
        }        
        for (i = 0; i < brakes.length; i++) {
            brakeOption.add(new SelectItem(brakes[i], brakes[i], brakes[i]));
        }        
        for (i = 0; i < suspensions.length; i++) {
            suspensionOption.add(new SelectItem(suspensions[i], suspensions[i], suspensions[i]));
        }        
        for (i = 0; i < speakers.length; i++) {
            speakerOption.add(new SelectItem(speakers[i], speakers[i], speakers[i]));
        }        
        for (i = 0; i < audio.length; i++) {
            audioOption.add(new SelectItem(audio[i], audio[i], audio[i]));
        }        
        for (i = 0; i < transmissions.length; i++) {
            transmissionOption.add(new SelectItem(transmissions[i], transmissions[i], transmissions[i]));
        }        
        
    }
    
    
    public void setCarId(int id) {
        
        try {            
            System.out.println("SetCarId called on id = " + id);            
            Properties carProps = new Properties();            
            InputStream in;
            
            // reload all properties based on car Id
            switch (id) {
                
                case 1:
                    // load car 1 data
                    String optionsOne = "cardemo/CarOptions1.properties";
       		    try {
       		       in = this.getClass().getClassLoader().getResourceAsStream(optionsOne);
        	    } catch (Throwable t) {
        	       throw new RuntimeException("Error Opening File:"+optionsOne);
	            }
		    carProps.load(in);
                    setCarImage("200x168_Jalopy.jpg");
                    break;
                    
                case 2:
                    // load car 2 data
                    String optionsTwo = "cardemo/CarOptions2.properties";
       		    try {
       		       in = this.getClass().getClassLoader().getResourceAsStream(optionsTwo);
        	    } catch (Throwable t) {
        	       throw new RuntimeException("Error Opening File:"+optionsTwo);
	            }
		    carProps.load(in);
                    setCarImage("200x168_Roadster.jpg");
                    break;
                    
                case 3:
                    // load car 3 data
                    String optionsThree = "cardemo/CarOptions3.properties";
       		    try {
       		       in = this.getClass().getClassLoader().getResourceAsStream(optionsThree);
        	    } catch (Throwable t) {
        	       throw new RuntimeException("Error Opening File:"+optionsThree);
	            }
		    carProps.load(in);
                    setCarImage("200x168_Luxury.jpg");
                    break;
                                        
                case 4:
                    // load car 4 data
                    String optionsFour = "cardemo/CarOptions4.properties";
       		    try {
       		       in = this.getClass().getClassLoader().getResourceAsStream(optionsFour);
        	    } catch (Throwable t) {
        	       throw new RuntimeException("Error Opening File:"+optionsFour);
	            }
		    carProps.load(in);
                    setCarImage("200x168_SUV.jpg");
                    break;
                                        

                   default:
                    // this should never happen
                    optionsOne = "cardemo/CarOptions1.properties";
       		    try {
       		       in = this.getClass().getClassLoader().getResourceAsStream(optionsOne);
        	    } catch (Throwable t) {
        	       throw new RuntimeException("Error Opening File:"+optionsOne);
	            }
                    carProps.load(in);
                    break;
            }
            
            // load this bean's properties with properties from currentBundle
            this.setCarTitle((String)carProps.getProperty("CarTitle"));
            this.setCarDesc((String)carProps.getProperty("CarDesc"));
            this.setCarBasePrice((String)carProps.getProperty("CarBasePrice"));
            this.setCarCurrentPrice((String)carProps.getProperty("CarCurrentPrice"));
            
            
        } catch (Exception exc) {
            System.out.println("Exception in CurrentOptionServer: " + exc.toString());
        }
        
    }
    
    public int getCarId() {
        return carId;
    }      
        
    public void setCarImage(String url) {
        thisUrl = url;
    }

    public String getCarImage() {
        return thisUrl;
    } 
    
    public void setCarTitle(String title) {
        carTitle = title;
    }
    
    public String getCarTitle() {
        return carTitle;
    }
    
    public void setCarDesc(String desc) {
        carDesc = desc;
    }
    
    public String getCarDesc() {
        return carDesc;
    }
    
    public void setCarBasePrice(String bp) {
        basePrice = bp;
    }
    
    public String getCarBasePrice() {
        return basePrice;
    }
    
    public void setCarCurrentPrice(String cp) {
        currentPrice = cp;
    }
    
    public String getCarCurrentPrice() {
        return currentPrice;
    }
    
    public void setEngineOption(Collection eng) {
        engineOption = new ArrayList(eng);
    }
    
    public Collection getEngineOption() {
        return engineOption;
    }
    
    public void setCurrentEngineOption(Object eng) {
        currentEngineOption = eng;
    }
    
    public Object getCurrentEngineOption() {
        return currentEngineOption;
    }
    
    public void setBrakeOption(Collection bk) {
        brakeOption =  new ArrayList(bk);
    }
    
    public Collection getBrakeOption() {
        return brakeOption;
    }
    
    public void setCurrentBrakeOption(Object op) {
        currentBrakeOption = op;
    }
    
    public Object getCurrentBrakeOption() {
        return currentBrakeOption;
    }
    
    public void setSuspensionOption(Collection op) {
        suspensionOption =  new ArrayList(op);
    }
    
    public Collection getSuspensionOption() {
        return suspensionOption;
    }
    
    public void setCurrentSuspensionOption(Object op) {
        currentSuspensionOption = op;
    }
    
    public Object getCurrentSuspensionOption() {
        return currentSuspensionOption;
    }
    
    public void setSpeakerOption(Collection op) {
        speakerOption =  new ArrayList(op);
    }
    
    public Collection getSpeakerOption() {
        return speakerOption;
    }
    
    public void setCurrentSpeakerOption(Object op) {
        currentSpeakerOption = op;
    }
    
    public Object getCurrentSpeakerOption() {
        return currentSpeakerOption;
    }
    
    public void setAudioOption(Collection op) {
        audioOption =  new ArrayList(op);
    }
    
    public Collection getAudioOption() {
        return audioOption;
    }
    
    public void setCurrentAudioOption(Object op) {
        currentAudioOption = op;
    }
    
    public Object getCurrentAudioOption() {
        return currentAudioOption;
    }
    
    public void setTransmissionOption(Collection op) {
        transmissionOption =  new ArrayList(op);
    }
    
    public Collection getTransmissionOption() {
        return transmissionOption;
    }
    
    public void setCurrentTransmissionOption(Object op) {
        currentTransmissionOption = op;
    }
    
    public Object getCurrentTransmissionOption() {
        return currentTransmissionOption;
    }
    
    public void setSunRoof(boolean roof) {
        sunRoof = roof;
    }
    
    public boolean getSunRoof() {
        return sunRoof;
    }
    
    public void setCruiseControl(boolean cruise) {
        cruiseControl = cruise;
    }
    
    public boolean getCruiseControl() {
        return cruiseControl;
    }
    
    public void setKeylessEntry(boolean entry) {
        keylessEntry = entry;
    }
    
    public boolean getKeylessEntry() {
        return keylessEntry;
    }
    
    public void setSecuritySystem(boolean security) {
        securitySystem = security;
    }
    
    public boolean getSecuritySystem() {
        return securitySystem;
    }
    
    public void setSkiRack(boolean ski) {
        skiRack = ski;
    }
    
    public boolean getSkiRack() {
        return skiRack;
    }
    
    public void setTowPackage(boolean tow) {
        towPackage = tow;
    }
    
    public boolean getTowPackage() {
        return towPackage;
    }
    
    public void setGps(boolean g) {
        gps = g;
    }
    
    public boolean getGps() {
        return gps;
    }
}
