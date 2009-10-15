/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
