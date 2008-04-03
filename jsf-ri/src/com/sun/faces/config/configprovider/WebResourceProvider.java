/*
 * $Id: WebResourceProvider.java,v 1.2 2007/04/27 22:00:55 ofung Exp $
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
