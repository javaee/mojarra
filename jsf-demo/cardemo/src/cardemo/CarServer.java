/*
 * $Id: CarServer.java,v 1.4 2003/08/28 08:22:17 rkitain Exp $
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

import java.util.ResourceBundle;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collection;
import java.util.MissingResourceException;
import java.io.IOException;

import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.application.Action;

/**
 * CarServer is central to the CarDemo application. It serves as the model object
 * for all the base options available for a particular Car. It also maintains 
 * references to CarDemoResources and currently selected package for a given
 * car. Lifetime of this object is specified by the application.
 */
public class CarServer extends Object {
 
    protected String thisUrl = "/current.gif";
    protected int carId = 1;
    protected String carTitle = "You shouldn't see this title";
    protected String carDesc = "This description should never be seen. " + 
        "If it is, your properties files aren't being read.";
    protected String basePrice = null;
    protected String currentPrice = null;
   
    protected ArrayList engineOption;
    protected Object currentEngineOption = null;
    
    protected ArrayList brakeOption;
    protected Object currentBrakeOption = null;
    
    protected ArrayList suspensionOption;
    protected Object currentSuspensionOption = null;
    
    protected ArrayList speakerOption;
    protected Object currentSpeakerOption = null;
    
    protected ArrayList audioOption;
    protected Object currentAudioOption = null;
    
    protected ArrayList transmissionOption;
    protected Object currentTransmissionOption = null;

    protected String currentPackageName = "null"; 
    protected String defaultPackage = "Custom";
    protected CarPackage currentPackage = null;
    protected CarDemoResources carDemoResources = null;
    
    public static String CARDEMO_PREFIX = "cardemo/";
    protected HashMap optionValues = null;
    protected CarBuyAction carBuyAction = null;
    
    public CarServer() {
        super();
        carDemoResources = new CarDemoResources();
    }
    
    /**
     * Tokenizes the passed in String which is a comma separated string of 
     * option values that serve as keys into the main resources file.
     * For example, optionStr could be "Disc,Drum", which corresponds to
     * brake options available for the chosen car. This String is tokenized
     * and used as key into the main resource file to get the localized option
     * values and stored in the passed in ArrayList.
     */
    public ArrayList parseStringIntoArrayList(String optionStr,
            ResourceBundle bundle, Locale locale) {
        ArrayList optionsList = null;
        int i = 0;
        String optionValue = null;
        
        if ( optionStr == null ) {
            return null;
        }     
        StringTokenizer st = new StringTokenizer(optionStr, ",");
        optionsList = new ArrayList((st.countTokens())+1);
        while (st.hasMoreTokens()) {
            String optionKey = st.nextToken();
            if ( locale != null ) {
                optionValue = (String) bundle.getObject(optionKey);
            } else {   
                // options like v4, v6 etc are not localized.
                optionValue = optionKey;
            } 
            optionsList.add(new SelectItem(optionValue, optionValue, 
                    optionValue));
            // we have to save the localized option values and thier keys 
            // because the ValueChanged event gives us the localized new option 
            // value. But the prices are stored based on OptionKey not 
            // optionValue.
            if ( optionValues == null ) {
                optionValues = new HashMap();
            }
            optionValues.put(optionValue, optionKey);
        }    
        return optionsList;
     }  
    
    /**
     * Loads options available for the base model of the chosen car by reading 
     * resources file for the chosen car. The option values serve as the key
     * into the main resource bundle which contains localized option values.
     */
    public void loadOptions(ResourceBundle rb) throws MissingResourceException{
        if ( rb == null ) {
            return;
        }    
        setCarTitle(((String)rb.getObject("CarTitle")));
        setCarDesc((String)rb.getObject("CarDesc"));
        setCarBasePrice((String)rb.getObject("CarBasePrice"));
        setCarCurrentPrice((String)rb.getObject("CarCurrentPrice"));
        setCarImage("/" + ((String)rb.getObject("carImage")));
        if ( optionValues != null ) {
            optionValues.clear();
        }
        Locale locale = (FacesContext.getCurrentInstance()).getLocale();
        ResourceBundle carResources = carDemoResources.
                getBundle(CARDEMO_PREFIX + "Resources", locale);
        
        String engineOptions = (String) rb.getObject("engine");
        engineOption = parseStringIntoArrayList(engineOptions,carResources,null);
       
        String brakeOptions = (String)rb.getObject("brake");
        brakeOption = parseStringIntoArrayList(brakeOptions,carResources,locale);
       
        String suspensionOptions = (String)rb.getObject("suspension");
        suspensionOption = parseStringIntoArrayList(suspensionOptions,
                carResources,locale);
       
        String speakerOptions = (String)rb.getObject("speaker");
        speakerOption = parseStringIntoArrayList(speakerOptions,carResources,null);
       
        String audioOptions = (String)rb.getObject("audio");
        audioOption = parseStringIntoArrayList(audioOptions,carResources,locale);
       
        String transmissionOptions = (String)rb.getObject("transmission");
        transmissionOption = parseStringIntoArrayList(transmissionOptions, 
                carResources , locale);
       
        // if we get here, all the lists were populated successfully and lists
        // shouldn't be null.
        currentSpeakerOption = ((SelectItem)speakerOption.get(0)).getLabel();
        currentEngineOption = ((SelectItem)engineOption.get(0)).getLabel();
        currentBrakeOption = ((SelectItem)brakeOption.get(0)).getLabel();
        currentSuspensionOption = ((SelectItem)suspensionOption.get(0)).getLabel();
        currentAudioOption = ((SelectItem)audioOption.get(0)).getLabel();
        currentTransmissionOption = ((SelectItem)transmissionOption.get(0)).getLabel();
    }

    /**
     * Loads the properties all the properties for the selected car.
     */
    public void setCarId(int id) throws MissingResourceException {
        this.carId = id;
        Locale locale = (FacesContext.getCurrentInstance()).getLocale();
        try {            
            ResourceBundle rb = null;            
            String idStr = String.valueOf(id);
            String carBundleName = CARDEMO_PREFIX + "CarOptions" + idStr; 
            rb = carDemoResources.getBundle(carBundleName, locale); 
            // load all properties based on car Id
            loadOptions(rb);
            setCurrentPackageName(defaultPackage);
	} catch (Exception exc) {
//            exc.printStackTrace();
            System.out.println("Exception in CarServer: " + exc.getMessage());
        }
        
    }
    
    
    /**
     * Returns the price for the option by reading the resource file for this 
     * car.
     */
    public int getPriceForOption(String optionKey) 
            throws MissingResourceException{
        int price= 0;
        String priceStr = null;
        
        Locale locale = (FacesContext.getCurrentInstance()).getLocale();
        ResourceBundle rb = null;            
        String idStr = String.valueOf(carId);
        String carBundleName = CARDEMO_PREFIX + "CarOptions" + idStr; 
        rb = carDemoResources.getBundle(carBundleName, locale); 
        optionKey = optionKey.trim();
        try {
            priceStr = (String) rb.getObject(optionKey);
            if ( priceStr != null ) {
                price = Integer.parseInt(priceStr);
            }
        } catch(MissingResourceException mse) {
            // if we get here, it could be that the option is a localized value
            // which would be the case if we are trying to get the price for
            // one of the Select one options. In that case we need to get the
            // key and use that to look up to look up the price.
            String lookupKey = (String) optionValues.get(optionKey);
            priceStr = (String) rb.getObject(lookupKey);
            if ( priceStr != null ) {
                price = Integer.parseInt(priceStr);
            }
        }
        return price;
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
    
    /**
     * Loads the options avalable for the package by reading the 
     * resources file for the chosen package.
     */
    public void setCurrentPackageName(String packageName) throws IOException{
        
        currentPackageName = packageName;
        try {
            Properties props = (Properties)carDemoResources.
                getResource(CARDEMO_PREFIX + packageName + ".properties");
            currentPackage = new CarPackage(props);
        } catch(Exception ep) {
            ep.printStackTrace();
            System.out.println("Exception in CarServer  " + ep.getMessage());
        }    
        setCurrentPackage(currentPackage);
    }

    public String getCurrentPackageName() {
        return currentPackageName;
    }
    
    public void setCurrentPackage(CarPackage carPackage) {
        currentPackage = carPackage;
    }

    public CarPackage getCurrentPackage() {
        return currentPackage;
    }
    
    public Action getCarBuyAction() {
        if (carBuyAction == null) {
            carBuyAction = new CarBuyAction();
        }
        return carBuyAction;
    }
    
    public String getOutOfStockOption() {
        if (carId == 1 && currentPackageName.equals("Custom") &&
            currentPackage.getSunRoofSelected()) {
            currentPackage.setSunRoofSelected(false);
            // reduce the price of sunroof option from currentCarPrice
            int cPrice = Integer.parseInt(currentPrice);
            int sunroofPrice = getPriceForOption("sunroof");
            cPrice = cPrice - sunroofPrice;
            currentPrice = String.valueOf(cPrice);
            return "out of stock";
        } else {
            return "success";
        }  
    }
    
    class CarBuyAction extends Action {
        public String invoke() {
            return (getOutOfStockOption());
        }
    }
}
