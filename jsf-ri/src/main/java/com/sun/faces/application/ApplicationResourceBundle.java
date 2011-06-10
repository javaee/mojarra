/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
