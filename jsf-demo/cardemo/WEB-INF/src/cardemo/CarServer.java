/*
 * $Id: CarServer.java,v 1.1 2002/09/30 21:42:19 jball Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import java.util.*;

public class CarServer extends Object {
    
    String car1Title = "Duke's Stripped-Down Jalopy";
    String car1Desc = "If you're the kind of person who doesn't care what anyone thinks, this is the car for you. Strictly for \"point-a-to-point-b\" types.";
    String car2Title = "Duke's MLC Roadster";
    String car2Desc = "Getting on in years? Need to make one last-ditch grasp at youth?  You'll feel 20 years younger when step behind the wheel of this baby.";
    String car3Title = "Duke's Vulgar Luxury Car";
    String car3Desc = "Those Joneses next door have everything, don't they? Well, show them that you're the one	pulling in the big bucks by driving up in this loaded chassis.";
    String car4Title = "Duke's Bloated SUV";
    String car4Desc = "Sometimes you can never be high enough or big enough compared to the car next to you.  And those newborns come out bigger every time.  Get the SUV that will hold everything and scare the begeezes out of everyone on the road.";
    
    public CarServer() {
        super();
    }
    
    public void setCar1Title(String title) {
        car1Title = title;
    }
    
    public String getCar1Title() {
        return car1Title;
    }
    
    public void setCar2Title(String title) {
        car2Title = title;
    }
    
    public String getCar2Title() {
        return car2Title;
    }
    
    public void setCar3Title(String title) {
        car3Title = title;
    }
    
    public String getCar3Title() {
        return car3Title;
    }
    
    public void setCar4Title(String title) {
        car4Title = title;
    }
    
    public String getCar4Title() {
        return car4Title;
    }
    
    public void setCar1Desc(String title) {
        car1Desc = title;
    }
    
    public String getCar1Desc() {
        return car1Desc;
    }
    
    public void setCar2Desc(String title) {
        car2Desc = title;
    }
    
    public String getCar2Desc() {
        return car2Desc;
    }
    
    public void setCar3Desc(String title) {
        car3Desc = title;
    }
    
    public String getCar3Desc() {
        return car3Desc;
    }
    
    public void setCar4Desc(String title) {
        car4Desc = title;
    }
    
    public String getCar4Desc() {
        return car4Desc;
    }
}
