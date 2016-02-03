/*
 * $Id: FeatureBean.java,v 1.3.38.2 2007/04/27 21:28:33 ofung Exp $
 */

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


    private Map descriptions = new TreeMap();


    public void addDescription(DescriptionBean descriptor) {
        descriptions.put(descriptor.getLang(), descriptor);
    }


    public DescriptionBean getDescription(String lang) {
        return ((DescriptionBean) descriptions.get(lang));
    }


    public DescriptionBean[] getDescriptions() {
        DescriptionBean results[] = new DescriptionBean[descriptions.size()];
        return ((DescriptionBean[]) descriptions.values().toArray(results));
    }


    public void removeDescription(DescriptionBean descriptor) {
        descriptions.remove(descriptor.getLang());
    }


    // ----------------------------------------------- DisplayNameHolder Methods


    private Map displayNames = new TreeMap();


    public void addDisplayName(DisplayNameBean descriptor) {
        displayNames.put(descriptor.getLang(), descriptor);
    }


    public DisplayNameBean getDisplayName(String lang) {
        return ((DisplayNameBean) displayNames.get(lang));
    }


    public DisplayNameBean[] getDisplayNames() {
        DisplayNameBean results[] = new DisplayNameBean[displayNames.size()];
        return ((DisplayNameBean[]) displayNames.values().toArray(results));
    }


    public void removeDisplayName(DisplayNameBean descriptor) {
        displayNames.remove(descriptor.getLang());
    }


    // ------------------------------------------------------ IconHolder Methods


    private Map icons = new TreeMap();


    public void addIcon(IconBean descriptor) {
        icons.put(descriptor.getLang(), descriptor);
    }


    public IconBean getIcon(String lang) {
        return ((IconBean) icons.get(lang));
    }


    public IconBean[] getIcons() {
        IconBean results[] = new IconBean[icons.size()];
        return ((IconBean[]) icons.values().toArray(results));
    }


    public void removeIcon(IconBean descriptor) {
        icons.remove(descriptor.getLang());
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
