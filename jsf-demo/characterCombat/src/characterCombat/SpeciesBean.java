/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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
