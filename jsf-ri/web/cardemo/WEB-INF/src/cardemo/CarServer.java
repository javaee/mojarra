package cardemo;

import java.util.*;

public class CarServer extends Object {
    
    String car1Title = "Honda Civic 4dr Sedan";
    String car1Desc = "If all cars were as well built as the Honda Civic, we'd probably live in a world with less repair shops and more car washes. The Civic's bullet-proof build quality is but one of the reasons for its success; one could also point to the car's excellent resale value and loyal repeat buyer base. But if you really want to understand why so many love this car, you need only talk to group of Civic owners.";
    String car2Title = "Acura RSX";
    String car2Desc = "Acura introduces an all-new sport coupe to its line this year, dropping the legendary Integra name in favor of RSX. Two trim levels are offered, a base model and Type-S. The latter comes with a blisteringly fast, high-output 2.0-liter engine producing 200 horsepower. A six-speed manual transmission, high-end Bose sound system and leather-clad sport seats are also standard equipment.";
    String car3Title = "Lexus LS 300";
    String car3Desc = "Someone once said that perfection is a road, not a destination. If this is true, then the engineers and designers at Lexus probably have the route memorized. With no apparent fear of losing their way, the Lexus team has allowed their flagship sedan, the LS 430, to momentarily detour from its journey. For the time being, it is parked comfortably at the crossroads where science and art converge to create an extraordinary driving experience.";
    String car4Title = "Ford Explorer";
    String car4Desc = "The Ford Explorer has been completely redesigned for 2002. Ford engineers have replaced the old Explorer's solid rear axle with a new independent rear suspension, placing the differential high up in the frame. This design gives the Explorer greater ground clearance while vastly improving both its ride and handling. It also frees-up space for an optional hide-away third-row seat, capable of expanding the Explorer's seating capacity to seven.";
    
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
