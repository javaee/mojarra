/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.application.applicationimpl;

import static com.sun.faces.cdi.CdiUtils.getBeanReference;
import static com.sun.faces.util.Util.getCdiBeanManager;
import static com.sun.faces.util.Util.getWebXmlVersion;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import com.sun.faces.cdi.CdiExtension;


public class Version {

    private Boolean isJsf23;
    
    /**
     * Are we running in JSF 2.3
     * 
     * @return true if we are, false otherwise.
     */
    public boolean isJsf23() {
        
        if (isJsf23 == null) {
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            BeanManager beanManager = getCdiBeanManager(facesContext);
            
            if (beanManager == null) {
                // TODO: use version enum and >=
                if (getFacesConfigXmlVersion(facesContext).equals("2.3") || getWebXmlVersion(facesContext).equals("4.0")) {
                    throw new FacesException("Unable to find CDI BeanManager");
                }
                isJsf23 = false;
            } else {
                isJsf23 = getBeanReference(beanManager, CdiExtension.class).isAddBeansForJSFImplicitObjects();
            }
            
        }
        
        return isJsf23;
    }

    /**
     * Get the faces-config.xml version (if any).
     *
     * @param facesContext the Faces context.
     * @return the version found, or "" if none found.
     */
    private String getFacesConfigXmlVersion(FacesContext facesContext) {
        String result = "";
        InputStream stream = null;
        try {
            URL url = facesContext.getExternalContext().getResource("/WEB-INF/faces-config.xml");
            if (url != null) {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                xpath.setNamespaceContext(new JavaeeNamespaceContext());
                stream = url.openStream();
                result = xpath.evaluate("string(/javaee:faces-config/@version)", new InputSource(stream));
            }
        } catch (MalformedURLException mue) {
            // Ignore
        } catch (XPathExpressionException | IOException xpee) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                    // Ignore
                }
            }
        }
        return result;
    }

  

    public class JavaeeNamespaceContext implements NamespaceContext {

        @Override
        public String getNamespaceURI(String prefix) {
            return "http://xmlns.jcp.org/xml/ns/javaee";
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return "javaee";
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    }

}
