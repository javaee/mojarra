/*
 * $Id: FeatureRule.java,v 1.2 2004/01/27 20:13:58 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.DisplayNameBean;
import com.sun.faces.config.beans.FeatureBean;
import com.sun.faces.config.beans.IconBean;


/**
 * <p>Base Digester rule for elements whose configuration bean
 * extends {@link FeatureBean}.</p>
 */

public abstract class FeatureRule extends Rule {


    // --------------------------------------------------------- Package Methods


    // Merge "top" into "old"
    static void mergeDescription(DescriptionBean top, DescriptionBean old) {

        if (top.getDescription() != null) {
            old.setDescription(top.getDescription());
        }

    }


    // Merge "top" into "old"
    static void mergeDisplayName(DisplayNameBean top, DisplayNameBean old) {

        if (top.getDisplayName() != null) {
            old.setDisplayName(top.getDisplayName());
        }

    }


    // Merge "top" into "old"
    static void mergeFeatures(FeatureBean top, FeatureBean old) {

        DescriptionBean db[] = top.getDescriptions();
        for (int i = 0; i < db.length; i++) {
            DescriptionBean dbo = old.getDescription(db[i].getLang());
            if (dbo == null) {
                old.addDescription(db[i]);
            } else {
                mergeDescription(db[i], dbo);
            }
        }

        DisplayNameBean dnb[] = top.getDisplayNames();
        for (int i = 0; i < dnb.length; i++) {
            DisplayNameBean dnbo = old.getDisplayName(dnb[i].getLang());
            if (dnbo == null) {
                old.addDisplayName(dnb[i]);
            } else {
                mergeDisplayName(dnb[i], dnbo);
            }
        }

        IconBean ib[] = top.getIcons();
        for (int i = 0; i < ib.length; i++) {
            IconBean ibo = old.getIcon(ib[i].getLang());
            if (ibo == null) {
                old.addIcon(ib[i]);
            } else {
                mergeIcon(ib[i], ibo);
            }
        }

    }


    // Merge "top" into "old"
    static void mergeIcon(IconBean top, IconBean old) {

        if (top.getLargeIcon() != null) {
            old.setLargeIcon(top.getLargeIcon());
        }
        if (top.getSmallIcon() != null) {
            old.setSmallIcon(top.getSmallIcon());
        }

    }



}
