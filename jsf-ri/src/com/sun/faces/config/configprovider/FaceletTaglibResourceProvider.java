/*
 * $Id: WebResourceProvider.java,v 1.5 2007/08/06 18:07:19 rlubke Exp $
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
import com.sun.faces.facelets.tag.TagLibrary;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class FaceletTaglibResourceProvider implements ConfigurationResourceProvider {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    
    
    /**
     * <p>
     *  The list of the standard facelet taglibs bundled into JSF
     * </p>
     */
    private static final List<String> FACELET_TAGLIB_RESOURCES;
    
    static {
        List<String> l =
          new ArrayList<String>(6);
        l.add("META-INF/jsf-composite.taglib.mojarra.xml");
        l.add("META-INF/jsf-core.taglib.mojarra.xml");
        l.add("META-INF/jsf-html.taglib.mojarra.xml");
        l.add("META-INF/jsf-ui.taglib.mojarra.xml");
        l.add("META-INF/jstl-core.taglib.mojarra.xml");
        l.add("META-INF/jstl-fn.taglib.mojarra.xml");
        FACELET_TAGLIB_RESOURCES = Collections.unmodifiableList((List<String>) l);
    }    
    
    
    
    // ------------------------------ Methods from ConfigurationResourceProvider


    /**
     * @see ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public List<URL> getResources(ServletContext context) {
        WebConfiguration webConfig = WebConfiguration.getInstance(context);
        List<URL> list = new ArrayList<URL>(6);
        URL src;
        ClassLoader cl = Util.getCurrentLoader(this);
        
        // Step 1: Load the libraries built into JSF
        for (String cur : FACELET_TAGLIB_RESOURCES) {
            try {
                src = cl.getResource(cur.trim());
                if (src == null) {
                    throw new FileNotFoundException(cur);
                }
                list.add(src);
            } catch (IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Error Loading Library: " + cur,
                                   e);
                    }
            }
        }
        
        // Step 2: Load any user declared libraries
        String libParam = webConfig
              .getOptionValue(WebContextInitParameter.FaceletsLibraries);
        if (libParam != null) {
            libParam = libParam.trim();
            String[] libs = Util.split(libParam, ";");
            TagLibrary libObj;
            for (int i = 0; i < libs.length; i++) {
                try {
                    src = context.getResource(libs[i].trim());
                    if (src == null) {
                        throw new FileNotFoundException(libs[i]);
                    }
                    list.add(src);
                } catch (IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Error Loading Library: " + libs[i],
                                   e);
                    }
                }
            }
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
