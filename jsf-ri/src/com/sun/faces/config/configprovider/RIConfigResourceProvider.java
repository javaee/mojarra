/*
 * $Id: RIConfigResourceProvider.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
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

package com.sun.faces.config.configprovider;

import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.util.Util;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class RIConfigResourceProvider implements ConfigurationResourceProvider {

    private static final String JSF_RI_CONFIG =
         "com/sun/faces/jsf-ri-runtime.xml";


    // ------------------------------ Methods from ConfigurationResourceProvider


    /**
     * @see ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public List<URL> getResources(ServletContext context) {

        List<URL> list = new ArrayList(1);
        list.add(Util.getCurrentLoader(this).getResource(JSF_RI_CONFIG));
        return list;

    }

}
