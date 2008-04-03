/*
 * $Id: ApplicationResourceBundle.java,v 1.1 2007/04/22 21:41:04 rlubke Exp $
 */

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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;

import com.sun.faces.util.Util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <p>Contains an application level resource bundle
 * name and its associated descriptions, if any.</p>
 */
public class ApplicationResourceBundle {

    public static final String DEFAULT_KEY = "DEFAULT";

    private final String baseName;
    private final Map<String, String> displayNames;
    private final Map<String, String> descriptions;
    private volatile Map<Locale, ResourceBundle> resources;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>
     *   Constructs a new ApplicationResourceBundle
     * </p>
     * @param baseName the base name of the <code>ResourceBundle</code>
     * @param displayNames any display names that were associated
     *  with the resource bundle definition in the configuration resource
     * @param descriptions any descriptions that were associated
     *  with the resource bundle definition in the configuration resource
     */
    public ApplicationResourceBundle(String baseName,
                                     Map<String, String> displayNames,
                                     Map<String, String> descriptions) {

        if (baseName == null) {
            // PENDING i18n
            throw new IllegalArgumentException();
        }
        this.baseName = baseName;
        this.displayNames = displayNames;
        this.descriptions = descriptions;
        this.resources = new HashMap<Locale, ResourceBundle>(4, 1.0f);

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return the base name of the <code>ResourceBundle</code> associated with
     *  this <code>ApplicationResourceBundle</code> instance
     */
    public String getBaseName() {

        return baseName;

    }


    /**
     * @param locale a <code>Locale</code>
     * @return return the <code>ResourceBundle</code> associated with the
     *  specified </code>locale</code>
     */
    public ResourceBundle getResourceBundle(Locale locale) {

        if (locale == null) {
            locale = Locale.getDefault();
        }

        ResourceBundle bundle = resources.get(locale);
        if (bundle == null) {
            ClassLoader loader = Util.getCurrentLoader(this);
            synchronized(this) {
                bundle = resources.get(locale);
                if (bundle == null) {
                    bundle = ResourceBundle.getBundle(baseName, locale, loader);
                    resources.put(locale, bundle);
                }
            }
        }

        return bundle;

    }


    /**
     * @param locale a <code>Locale</code>
     * @return a text of a <code>display-name</code> element associated with the
     *  specified </code>locale</code>
     */
    public String getDisplayName(Locale locale) {

        String displayName = null;
        if (displayNames != null) {
            displayName = queryMap(locale, displayNames);            
        }

        return ((displayName != null) ? displayName : "");

    }


    /**
     * @param locale a <code>Locale</code>
     * @return a text of a <code>description</code> element associated with the
     *  specified </code>locale</code>
     */
    public String getDescription(Locale locale) {

        String description = null;
        if (descriptions != null) {
           description = queryMap(locale, descriptions);
        }

        return ((description != null) ? description : "");

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     *  Lookup and return the text for the specified <code>Locale</code>
     *   from within the specified <code>Map</code>.
     * </p>
     * @param locale <code>Locale</code> if interest
     * @param map a map containing localized text keyed by <code>Locale</code>
     * @return localized text, if any
     */
    private String queryMap(Locale locale, Map<String, String> map) {

        if (locale == null) {
            return map.get(DEFAULT_KEY);
        } else {
            String key = locale.toString();
            String description = map.get(key);
            if (description == null) {
                return map.get(DEFAULT_KEY);
            }
        }

        return null;

    }

}
