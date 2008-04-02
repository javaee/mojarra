/*
 * $Id: FeatureBean.java,v 1.2 2004/01/27 20:13:37 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
