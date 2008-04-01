package cardemo;

import java.util.*;
import java.io.*;

public class CurrentOptionServer extends Object {
    
    
    int carId = 0001;
    String carTitle = "Foo";
    String carDesc = "Blah blah blah blah blah blah blah blah blah blah.";
    String basePrice = "Foo";
    String currentPrice = "Foo";
    Collection engineOption;
    Object defaultEngineOption;
    Object currentEngineOption;
    Collection brakeOption;
    Object defaultBrakeOption;
    Object currentBrakeOption;
    Collection suspensionOption;
    Object defaultSuspensionOption;
    Object currentSuspensionOption;
    Collection speakerOption;
    Object defaultSpeakerOption;
    Object currentSpeakerOption;
    Collection audioOption;
    Object defaultAudioOption;
    Object currentAudioOption;
    Collection transmissionOption;
    Object defaultTransmissionOption;
    Object currentTransmissionOption;
    
    
    public CurrentOptionServer() {
        super();
        
        engineOption = new Vector();
        defaultEngineOption = new Object();
        currentEngineOption = new Object();
        
        brakeOption = new Vector();
        defaultBrakeOption = new Object();
        currentBrakeOption = new Object();
        
        suspensionOption = new Vector();
        defaultSuspensionOption = new Object();
        currentSuspensionOption = new Object();
        
        speakerOption = new Vector();
        defaultSpeakerOption = new Object();
        currentSpeakerOption = new Object();
        
        audioOption = new Vector();
        defaultAudioOption = new Object();
        currentAudioOption = new Object();
        
        transmissionOption = new Vector();
        defaultTransmissionOption = new Object();
        currentTransmissionOption = new Object();
        
        engineOption.add(new String("4 cylinder"));
        engineOption.add("V6");
        engineOption.add("V8");
        defaultEngineOption = "4 cylinder";
        currentEngineOption = "4 cylinder";
        
        brakeOption.add("disk");
        brakeOption.add("drum");
        defaultBrakeOption = "disk";
        currentBrakeOption = "disk";
        
        suspensionOption.add("regular");
        suspensionOption.add("performance");
        defaultSuspensionOption = "regular";
        currentSuspensionOption = "regular";
        
        speakerOption.add("4");
        speakerOption.add("6");
        defaultSpeakerOption = "4";
        currentSpeakerOption = "4";
        
        audioOption.add("standard");
        audioOption.add("premium");
        defaultAudioOption = "standard";
        currentAudioOption = "standard";
        
        transmissionOption.add("auto");
        transmissionOption.add("manual");
        defaultTransmissionOption = "manual";
        currentTransmissionOption = "manual";
        
    }
    
    public void setCurrentOptionsFromFile (String propFileName) {
        // load properties from propFileName
    }
    
    public void setCar(int id) {
        // reload all properties based on car Id
    }
    
    public int getCar() {
        return carId;
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
    
    public void setBasePrice(String bp) {
        basePrice = bp;
    }
    
    public String getBasePrice() {
        return basePrice;
    }
    
    public void setCurrentPrice(String cp) {
        currentPrice = cp;
    }
    
    public String getCurrentPrice() {
        return currentPrice;
    }
    
    public void setEngineOption(Object eng) {
        engineOption = (Collection)eng;
    }
    
    public Object getEngineOption() {
        return engineOption;
    }
    
    public void setDefaultEngineOption(Object eng) {
        defaultEngineOption = eng;
    }
    
    public Object getDefaultEngineOption() {
        return defaultEngineOption;
    }
    
    public void setCurrentEngineOption(Object eng) {
        currentEngineOption = eng;
    }
    
    public Object getCurrentEngineOption() {
        return currentEngineOption;
    }
    
    public void setBrakeOption(Object bk) {
        brakeOption =  (Collection)bk;
    }
    
    public Object getBrakeOption() {
        return brakeOption;
    }
    
    public void setDefaultBrakeOption(Object op) {
        defaultBrakeOption = op;
    }
    
    public Object getDefaultBrakeOption() {
        return defaultBrakeOption;
    }
    
    public void setCurrentBrakeOption(Object op) {
        currentBrakeOption = op;
    }
    
    public Object getCurrentBrakeOption() {
        return currentBrakeOption;
    }
    
    public void setSuspensionOption(Object op) {
        suspensionOption =  (Collection)op;
    }
    
    public Object getSuspensionOption() {
        return suspensionOption;
    }
    
    public void setDefaultSuspensionOption(Object op) {
        defaultSuspensionOption = op;
    }
    
    public Object getDefaultSuspensionOption() {
        return defaultSuspensionOption;
    }
    
    public void setCurrentSuspensionOption(Object op) {
        currentSuspensionOption = op;
    }
    
    public Object getCurrentSuspensionOption() {
        return currentSuspensionOption;
    }
    
    public void setSpeakerOption(Object op) {
        speakerOption =  (Collection)op;
    }
    
    public Object getSpeakerOption() {
        return speakerOption;
    }
    
    public void setDefaultSpeakerOption(Object op) {
        defaultSpeakerOption = op;
    }
    
    public Object getDefaultSpeakerOption() {
        return defaultSpeakerOption;
    }
    
    public void setCurrentSpeakerOption(Object op) {
        currentSpeakerOption = op;
    }
    
    public Object getCurrentSpeakerOption() {
        return currentSpeakerOption;
    }
    
    public void setAudioOption(Object op) {
        audioOption =  (Collection)op;
    }
    
    public Object getAudioOption() {
        return audioOption;
    }
    
    public void setDefaultAudioOption(Object op) {
        defaultAudioOption = op;
    }
    
    public Object getDefaultAudioOption() {
        return defaultAudioOption;
    }
    
    public void setCurrentAudioOption(Object op) {
        currentAudioOption = op;
    }
    
    public Object getCurrentAudioOption() {
        return currentAudioOption;
    }
    
    public void setTransmissionOption(Object op) {
        transmissionOption =  (Collection)op;
    }
    
    public Object getTransmissionOption() {
        return transmissionOption;
    }
    
    public void setDefaultTransmissionOption(Object op) {
        defaultTransmissionOption = op;
    }
    
    public Object getDefaultTransmissionOption() {
        return defaultTransmissionOption;
    }
    
    public void setCurrentTransmissionOption(Object op) {
        currentTransmissionOption = op;
    }
    
    public Object getCurrentTransmissionOption() {
        return currentTransmissionOption;
    }
}
