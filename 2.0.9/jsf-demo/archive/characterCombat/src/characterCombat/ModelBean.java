/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package characterCombat;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * <p>ModelBean is the backing bean for the CombatCharacter application.
 * It contains available characters and species types. It also contains
 * action handlers that process current bean state and return appropriate
 * results based on the action.</p>
 */
public class ModelBean {

    // -------------------------------------------------------------------------
    // Class Variables ---------------------------------------------------------
    // -------------------------------------------------------------------------

    /** <p>Array of SelectItem entries for the available species</p> */
    private static SelectItem characterSpeciesOptions[] = {
          new SelectItem("Maia"),
          new SelectItem("Istari"),
          new SelectItem("Ent"),
          new SelectItem("Elf"),
          new SelectItem("Man"),
          new SelectItem("Dwarf"),
          new SelectItem("Hobbit")
    };

    /** <p>Tie result String</p> */
    private static String tieResult = "No One";

    // -------------------------------------------------------------------------
    // Instance Variables ------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * <p>Map of available species and their respective properties. Map
     * is keyed by species type and contains SpeciesBean entries</p>
     */
    private HashMap<String, SpeciesBean> speciesPropertyMap = null;

    // -------------------------------------------------------------------------
    // Constructor -------------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * <p> Populate Map instance with species and their characteristics.
     * Populate a list of characters</p>
     */
    public ModelBean() {
        //create List of Map instances for pre-defined characters
        populate();
    }

    // -------------------------------------------------------------------------
    // Value Properties --------------------------------------------------------
    // -------------------------------------------------------------------------

    ArrayList<CharacterBean> dataList = null;

    /**
     * <p>Returns List of characters</p>
     *
     * @return the list of characters
     */
    public List<CharacterBean> getDataList() {
        return dataList;
    }

    /**
     * <p>Set the internal list of characters</p>
     *
     * @param dataList List of characters
     */
    public void setDataList(ArrayList<CharacterBean> dataList) {
        this.dataList = dataList;
    }

    private String customName = null;

    /**
     * <p>Get the custom entry's name</p>
     *
     * @return custom name String
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * <p>Set the custom entry's name</p>
     *
     * @param customName custom name
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    private String customSpecies = null;

    /**
     * <p>Get the custom entry's species</p>
     *
     * @return custom species String
     */
    public String getCustomSpecies() {
        return customSpecies;
    }

    /**
     * <p>Set the custom entry's species</p>
     *
     * @param customSpecies
     */
    public void setCustomSpecies(String customSpecies) {
        this.customSpecies = customSpecies;
    }

    private String currentSelection = null;

    /**
     * <p>Get the current character name. If there is no current
     * name, get one from a list of available characters</p>
     *
     * @return current character name String
     */
    public String getCurrentSelection() {
        return currentSelection;
    }

    /**
     * <p>Set the current character name</p>
     *
     * @param currentSelection
     */
    public void setCurrentSelection(String currentSelection) {
        this.currentSelection = currentSelection;
    }

    private String firstSelection = null;

    /**
     * <p>Get the first selected character name</p>
     *
     * @return first selected character name String
     */
    public String getFirstSelection() {
        if (null == firstSelection) {
            firstSelection = (dataList.get(0)).getName();
        }
        return firstSelection;
    }

    /**
     * <p>Set the first selected character name</p>
     *
     * @param firstSelection
     */
    public void setFirstSelection(String firstSelection) {
        this.firstSelection = firstSelection;
    }

    private String secondSelection = null;

    /**
     * <p>Get the second selected character name</p>
     *
     * @return second selected character name String
     */
    public String getSecondSelection() {
        if (null == secondSelection) {
            List<SelectItem> available = getCharactersToSelect();
            secondSelection = (String) (available.get(0)).getValue();
        }
        return secondSelection;
    }

    /**
     * <p>Set the second selected character name</p>
     *
     * @param secondSelection
     */
    public void setSecondSelection(String secondSelection) {
        this.secondSelection = secondSelection;
    }

    // -------------------------------------------------------------------------
    // Data Properties ---------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * <p>Get the list of available species options</p>
     *
     * @return List of available species options
     */
    public List<SelectItem> getSpeciesOptions() {
        return Arrays.asList(characterSpeciesOptions);
    }

    /**
     * <p>Get list of characters available for selection.
     * If a character has already been selected, do not
     * display it in the available characters list. Wrap
     * the list in SelectItems so that the items can
     * be handled by the JSF framework as selectable items</p>
     *
     * @return List of available SelectItem characters
     */
    public List<SelectItem> getCharactersToSelect() {
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        Iterator<CharacterBean> iter = dataList.iterator();
        SelectItem selectItem;

        while (iter.hasNext()) {
            CharacterBean item = iter.next();

            //If a character has been selected, do not include it
            if (!item.getName().equals(firstSelection)) {
                selectItem = new SelectItem(item.getName());
                selectItemList.add(selectItem);
            }
        }

        return selectItemList;
    }

    /**
     * <p>Get the list of all characters, regardless of whether or not
     * they are selected</p>
     *
     * @return List of all SelectItem characters
     */
    public List<SelectItem> getAllCharactersToSelect() {
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        Iterator<CharacterBean> iter = dataList.iterator();
        SelectItem selectItem;

        while (iter.hasNext()) {
            CharacterBean item = iter.next();

            selectItem = new SelectItem(item.getName());
            selectItemList.add(selectItem);
        }

        return selectItemList;
    }

    /**
     * <p>Very simple algorithm to determine combat winner based on
     * species. If both characters are the same species, the result is
     * a tie.</p>
     * <p>This method could be expanded to include other criteria
     * and randomization.</p>
     *
     * @return combat winner name String
     */
    public String getCombatWinner() {
        String firstSelectionSpecies = getSpeciesByName(firstSelection);
        String secondSelectionSpecies = getSpeciesByName(secondSelection);

        int firstCount = -1;
        int secondCount = -1;
        for (int i = 0; i < characterSpeciesOptions.length; i++) {
            if (firstSelectionSpecies.equals(
                  characterSpeciesOptions[i].getLabel())) {
                firstCount = i;
            }
            if (secondSelectionSpecies.equals(
                  characterSpeciesOptions[i].getLabel())) {
                secondCount = i;
            }
        }

        if (firstCount == secondCount) {
            return tieResult;
        }
        return (firstCount < secondCount) ? firstSelection : secondSelection;
    }

    // -------------------------------------------------------------------------
    // Action Handlers ---------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * <p>Add the new name to character list if name is not empty or does
     * not already exist in the list<p>
     *
     * @param event the ActionEvent that triggered the action
     */
    public void addCustomName(ActionEvent event)
          throws AbortProcessingException {
        if ((customName != null) && (!customName.trim().equals(""))) {
            customName = customName.trim();

            //check to see if name already exists in list
            for (CharacterBean item : dataList) {
                if (item.getName().equals(customName)) {
                    reset();
                    return;
                }
            }

            //create new entry
            CharacterBean item = new CharacterBean();
            item.setName(customName);
            item.setSpecies(speciesPropertyMap.get(customSpecies));
            dataList.add(item);
        }
    }

    // -------------------------------------------------------------------------
    // Private Methods ---------------------------------------------------------
    // -------------------------------------------------------------------------

    /**
     * <p>Get species type based on character name<p>
     *
     * @param name
     *
     * @return species type String
     */
    private String getSpeciesByName(String name) {

        for (CharacterBean item : dataList) {
            if (item.getName().equals(name)) {
                return item.getSpecies().getType();
            }
        }
        return null;
    }

    /**
     * <p>Populate both the species property map of species type to
     * species property bean mappings as well as initial list of
     * available characters</p>
     */
    private void populate() {
        populateSpeciesMap();
        populateCharacterList();
    }


    /** <p>Populate species type to properties mappings</p> */
    private void populateSpeciesMap() {
        speciesPropertyMap = new HashMap<String, SpeciesBean>();
        SpeciesBean species = new SpeciesBean();
        species.setType("Maia");
        species.setLanguage("Black Speech");
        species.setImmortal(true);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Istari");
        species.setLanguage("Common Tongue");
        species.setImmortal(true);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Elf");
        species.setLanguage("Quenya/Sindarin");
        species.setImmortal(true);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Ent");
        species.setLanguage("Quenya/Sindarin");
        species.setImmortal(true);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Man");
        species.setLanguage("Common Tongue");
        species.setImmortal(false);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Dwarf");
        species.setLanguage("Dwarfish");
        species.setImmortal(false);
        speciesPropertyMap.put(species.getType(), species);

        species = new SpeciesBean();
        species.setType("Hobbit");
        species.setLanguage("Common Tongue");
        species.setImmortal(false);
        speciesPropertyMap.put(species.getType(), species);
    }

    /** <p>Populate initial characters list</p> */
    private void populateCharacterList() {
        dataList = new ArrayList<CharacterBean>();
        CharacterBean item = new CharacterBean();
        item.setName("Gandalf");
        item.setSpecies(speciesPropertyMap.get("Istari"));
        dataList.add(item);

        item = new CharacterBean();
        item.setName("Frodo");
        item.setSpecies(speciesPropertyMap.get("Hobbit"));
        dataList.add(item);

        item = new CharacterBean();
        item.setName("Legolas");
        item.setSpecies(speciesPropertyMap.get("Elf"));
        dataList.add(item);
    }

    /**
     * <p>Clear out internal selection strings in preparation to go
     * to the selection pages</p>
     */
    private void reset() {
        currentSelection = null;
        firstSelection = null;
        secondSelection = null;
    }

}
