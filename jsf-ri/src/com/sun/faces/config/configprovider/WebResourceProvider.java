/*
 * $Id: WebResourceProvider.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
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

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WebResourceProvider implements ConfigurationResourceProvider {

    /**
     * <p>The resource path for the faces configuration in the
     * <code>WEB-INF</code> directory of an application.</p>
     */
    private static final String WEB_INF_RESOURCE =
         "/WEB-INF/faces-config.xml";


    // ------------------------------ Methods from ConfigurationResourceProvider


    /**
     * @see ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public List<URL> getResources(ServletContext context) {
        // Step 4, parse any context-relative resources specified in
        // the web application deployment descriptor
        WebConfiguration webConfig = WebConfiguration.getInstance(context);
        String paths =
             webConfig
                  .getContextInitParameter(WebConfiguration.WebContextInitParameter.JavaxFacesConfigFiles);
        List<URL> list = new ArrayList(6);
        if (paths != null) {
            for (String token : Util.split(paths.trim(), ",")) {
                if (!WEB_INF_RESOURCE.equals(token.trim())) {
                    URL u = getContextURLForPath(context, token.trim());
                    if (u != null) {
                        list.add(u);
                    } else {
                        // PENDING log a message
                    }
                }

            }
        }

        // Step 5, parse "/WEB-INF/faces-config.xml" if it exists
        URL webFacesConfig = getContextURLForPath(context, WEB_INF_RESOURCE);
        if (webFacesConfig != null) {
            list.add(webFacesConfig);
        }
        return list;
    }


    // --------------------------------------------------------- Private Methods


    private URL getContextURLForPath(ServletContext context, String path) {
        try {
            return context.getResource(path);
        } catch (MalformedURLException mue) {
            throw new FacesException(mue);
        }
    }

}
