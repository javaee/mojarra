/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

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
