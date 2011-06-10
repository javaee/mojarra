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

/** <p>SpeciesBean represents the data associated with a species type</p> */
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
     * @param type - species type
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
     * @param language - species language
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
     * @param immortal - is the species immortal
     */
    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

}
