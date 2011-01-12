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

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Base class for configuration beans for "features" such as
 * attributes and components.</p>
 */

public abstract class FeatureBean {


    // -------------------------------------------------------------- Properties



    // ----------------------------------------------- DescriptionHolder Methods


    private Map<String,DescriptionBean> descriptions = new TreeMap<String, DescriptionBean>();


    public void addDescription(DescriptionBean descriptor) {
        descriptions.put(descriptor.getLang(), descriptor);
    }


    public DescriptionBean getDescription(String lang) {
        return (descriptions.get(lang));
    }


    public DescriptionBean[] getDescriptions() {
        DescriptionBean results[] = new DescriptionBean[descriptions.size()];
        return (descriptions.values().toArray(results));
    }


    public void removeDescription(DescriptionBean descriptor) {
        descriptions.remove(descriptor.getLang());
    }


    // ----------------------------------------------- DisplayNameHolder Methods


    private Map<String,DisplayNameBean> displayNames = new TreeMap<String, DisplayNameBean>();


    public void addDisplayName(DisplayNameBean descriptor) {
        displayNames.put(descriptor.getLang(), descriptor);
    }


    public DisplayNameBean getDisplayName(String lang) {
        return (displayNames.get(lang));
    }


    public DisplayNameBean[] getDisplayNames() {
        DisplayNameBean results[] = new DisplayNameBean[displayNames.size()];
        return (displayNames.values().toArray(results));
    }


    public void removeDisplayName(DisplayNameBean descriptor) {
        displayNames.remove(descriptor.getLang());
    }


    // ------------------------------------------------------ IconHolder Methods


    private Map<String,IconBean> icons = new TreeMap<String, IconBean>();


    public void addIcon(IconBean descriptor) {
        icons.put(descriptor.getLang(), descriptor);
    }


    public IconBean getIcon(String lang) {
        return (icons.get(lang));
    }


    public IconBean[] getIcons() {
        IconBean results[] = new IconBean[icons.size()];
        return (icons.values().toArray(results));
    }


    public void removeIcon(IconBean descriptor) {
        icons.remove(descriptor.getLang());
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
