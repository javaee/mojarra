/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.util;

import java.util.Locale;

//This class LocaleBVP47 represents a language tag object based on the syntax of BCP47
public class LocaleBCP47 {
    //Language subtag fields
    private String language = ""; //language subtag
    private String script = ""; //script subtag
    private String region = ""; //region subtag

    //Parse a language tag string and return a LocaleBCP47 object
    //Return null if the string doesn't fulfill the BCP47 sytax
    public static LocaleBCP47 parse(String languageTag) {
        if (languageTag == null || languageTag.isEmpty()) {
            return null;
        }
        String[] tokens = languageTag.split(SEP);
        int index = 0;
        //langtag must start with language
        if (!isLanguage(tokens[index])) {
            return null;
        }
        LocaleBCP47 langtag = new LocaleBCP47();
        //**Set language
        langtag.setLanguage(tokens[index++]);
        //Filter out extended language subtags, which sometimes follow languge subtag
        int count = 0; //counter for extlangs, maximum 3
        while (index < tokens.length) {
            //parse extlangs
            if (count++ == 3 || !isExtlang(tokens[index])) {
                break;
            }
            index++;
        }
        //**Set script
        if (index != tokens.length && isScript(tokens[index])) {
            langtag.setScript(tokens[index++]);
        }
        //**Set region
        if (index != tokens.length && isRegion(tokens[index])) {
            langtag.setRegion(tokens[index]);
        }
        langtag.addRegion(); //For zh-Hans, set region to CN; For zh-Hant, set region to TW
        return langtag;
    }

    //Return a language tag string in form of "language-region"
    public String toLangtag() {
        return toLangtag(language);
    }

    public String toLangtag(String lang) {
        if (region.equals("")) {
            return lang;
        }
        return lang + SEP + region;
    }

    //Setter method for language subtag field
    private void setLanguage(String language) {
        String lang = language.toLowerCase(Locale.US);
        this.language = lang;
    }

    //Setter method for script subtag field
    private void setScript(String script) {
        this.script = script.substring(0, 1).toUpperCase(Locale.US) + script.substring(1).toLowerCase(Locale.US);
    }

    //Setter method for region subtag field
    private void setRegion(String region) {
        this.region = region.toUpperCase(Locale.US);
    }

    //To handle zh-Hant and zh-Hans where there is no region subtag,  region attribute is injected
    private void addRegion() {
        //To handle zh-Hant and zh-Hans
        if (language.equals("zh") && region.isEmpty()) {
            if (script.equals("Hant")) {
                this.region = "TW";
                return;
            }
            if (script.equals("Hans")) {
                this.region = "CN";
            }
        }
    }

    //Getter method for language subtag field
    public String getLanguage() {
        return language;
    }

    //Getter method for script subtag field
    private String getScript() {
        return script;
    }

    //Getter method for region subtag field
    public String getRegion() {
        return region;
    }

    //Language subtag syntax checking methods
    private static boolean isLanguage(String s) {
        // language = 2*3ALPHA                        ; shortest ISO 649 code
        //                       ["-" extlang]                       ; sometimes followed by
        //                                                                  ;     extended language subtags
        //                    / 4ALPHA                             ; or reserved for future use
        //                    / 5*8ALPHA                         ; or registered language subtag
        return (s.length() >= 2) && (s.length() <= 8) && isAlphaString(s);
    }

    //Verify if the input string is a extended language subtag according to BCP47 definition
    private static boolean isExtlang(String s) {
        //extlang = 3ALPHA                                  ;selected ISO 639 codes
        //                 *2("-" 3ALPHA)                       ;permeanently reserved
        return s.length() == 3 && isAlphaString(s);
    }

    //Verify if the input string is a script subtag according to BCP47 definition
    private static boolean isScript(String s) {
        //script = 4ALPHA                                      ; ISO 15924 code
        return (s.length() == 4) && isAlphaString(s);
    }

    //Verify if the input string is a region subtag according to BCP47 definition
    private static boolean isRegion(String s) {
        //region = 2ALPHA                                     ; ISO 3166-1 code
        //             / 3DIGIT                                         ; UN M.49 code
        return ((s.length() == 2) && isAlphaString(s)) || ((s.length() == 3) && isNumericString(s));
    }

    //Verify if the input string is composed of alphabets only
    private static boolean isAlphaString(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!isAlpha(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //Verify if the input char is an alphabet
    private static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private static boolean isNumericString(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!isNumeric(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //Verify if the input string is numeric
    private static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }
    //Static fields
    public static final String SEP = "-";
    
}
