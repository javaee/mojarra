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

package com.sun.faces.config.rules;


import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.DisplayNameBean;
import com.sun.faces.config.beans.FeatureBean;
import com.sun.faces.config.beans.IconBean;
import org.apache.commons.digester.Rule;


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
