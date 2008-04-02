package characterCombat;

/**
 * <p>SpeciesBean represents the data associated with a species type</p>
 */
public class SpeciesBean {

    String type = null;

    /**
     * <p>Get the species type</p>
     *
     * @return species type String
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Set the species type</p>
     *
     * @param species type String
     */
    public void setType(String type) {
        this.type = type;
    }

    String language = null;

    /**
     * <p>Get the language associated with the species</p>
     *
     * @return species language String
     */
    public String getLanguage() {
        return language;
    }

    /**
     * <p>Set the language associated with the species</p>
     *
     * @param species language String
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    boolean immortal = false;

    /**
     * <p>Get the immortal state associated with the species</p>
     *
     * @return species immortal boolean
     */
    public boolean isImmortal() {
        return immortal;
    }

    /**
     * <p>Set the immortal state associated with the species</p>
     *
     * @param species immortal boolean
     */
    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

}
