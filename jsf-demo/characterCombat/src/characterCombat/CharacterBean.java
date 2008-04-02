package characterCombat;

/**
 * <p>CharacterBean represents the data of an individual character</p>
 */
public class CharacterBean {

    String name = null;

    /**
     * <p>Get the character name</p>
     *
     * @return character name String
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Set the character name</p>
     *
     * @param character name String
     */
    public void setName(String name) {
        this.name = name;
    }

    SpeciesBean species = null;

    /**
     * <p>Get the species bean</p>
     *
     * @return species SpeciesBean
     */
    public SpeciesBean getSpecies() {
        return species;
    }

    /**
     * <p>Set the species bean</p>
     *
     * @param species SpeciesBean
     */
    public void setSpecies(SpeciesBean species) {
        this.species = species;
    }

}
