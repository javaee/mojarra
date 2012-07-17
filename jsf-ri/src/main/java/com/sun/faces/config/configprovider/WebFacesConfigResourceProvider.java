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

package com.sun.faces.config.configprovider;

import java.net.URI;
import java.util.Collection;

import javax.servlet.ServletContext;

import static com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.JavaxFacesConfigFiles;

/**
 *
 */
public class WebFacesConfigResourceProvider extends BaseWebConfigResourceProvider {


    /**
     * <p>The resource path for the faces configuration in the
     * <code>WEB-INF</code> directory of an application.</p>
     */
    private static final String WEB_INF_RESOURCE =
         "/WEB-INF/faces-config.xml";

    private static final String[] EXCLUDES = { WEB_INF_RESOURCE };
    private static final String SEPARATORS = ",|;";


    // ------------------------------ Methods from ConfigurationResourceProvider


    /**
     * @see com.sun.faces.spi.ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public Collection<URI> getResources(ServletContext context) {

        Collection<URI> urls = super.getResources(context);

        // Step 5, parse "/WEB-INF/faces-config.xml" if it exists
        URI webFacesConfig = getContextURLForPath(context, WEB_INF_RESOURCE);
        if (webFacesConfig != null) {
            urls.add(webFacesConfig);
        }

        // PENDING (rlubke,driscoll) this is a temporary measure to prevent
        // having to find the web-based configuration resources twice
        context.setAttribute("com.sun.faces.webresources", urls);

        return urls;
        
    }


    // ------------------------------ Methods from BaseWebConfigResourceProvider


    protected WebContextInitParameter getParameter() {

        return JavaxFacesConfigFiles;

    }

    
    protected String[] getExcludedResources() {

        return EXCLUDES;

    }

    protected String getSeparatorRegex() {
        return SEPARATORS;
    }
}
