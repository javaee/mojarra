package characterCombat;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class CharacterBean {

    String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    String species = null;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
    

    String language = null;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    boolean immortal = false;

    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }


}
