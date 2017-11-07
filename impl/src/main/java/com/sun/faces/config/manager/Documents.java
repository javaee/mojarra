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

package com.sun.faces.config.manager;

import static com.sun.faces.RIConstants.JAVAEE_XMLNS;
import static com.sun.faces.util.Util.isEmpty;
import static java.util.Arrays.asList;
import static java.util.logging.Level.INFO;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javax.faces.application.ApplicationConfigurationPopulator;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.manager.documents.DocumentInfo;
import com.sun.faces.config.manager.documents.DocumentOrderingWrapper;
import com.sun.faces.config.manager.tasks.FindConfigResourceURIsTask;
import com.sun.faces.config.manager.tasks.ParseConfigResourceToDOMTask;
import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.util.FacesLogger;

public class Documents {
    
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    
    /**
     * <p>
     *   Obtains an array of <code>Document</code>s to be processed
     * </p>
     *
     * @param servletContext the <code>ServletContext</code> for the application to be
     *  processed
     * @param providers <code>List</code> of <code>ConfigurationResourceProvider</code>
     *  instances that provide the URL of the documents to parse.
     * @param executor the <code>ExecutorService</code> used to dispatch parse
     *  request to
     * @param validating flag indicating whether or not the documents
     *  should be validated
     * @return an array of <code>DocumentInfo</code>s
     */
    public static DocumentInfo[] getXMLDocuments(ServletContext servletContext, List<ConfigurationResourceProvider> providers, ExecutorService executor, boolean validating) {
        
        // Query all configuration providers to give us a URL to the configuration they are providing
        
        List<FutureTask<Collection<URI>>> uriTasks = new ArrayList<>(providers.size());
        
        for (ConfigurationResourceProvider provider : providers) {
            FutureTask<Collection<URI>> uriTask = new FutureTask<>(new FindConfigResourceURIsTask(provider, servletContext));
            uriTasks.add(uriTask);
            
            if (executor != null) {
                executor.execute(uriTask);
            } else {
                uriTask.run();
            }
        }

        // Load and XML parse all documents to which the URLs that we collected above point to
        
        List<FutureTask<DocumentInfo>> docTasks = new ArrayList<>(providers.size() << 1);

        for (FutureTask<Collection<URI>> uriTask : uriTasks) {
            try {
                for (URI uri : uriTask.get()) {
                    FutureTask<DocumentInfo> docTask = new FutureTask<>(new ParseConfigResourceToDOMTask(servletContext, validating, uri));
                    docTasks.add(docTask);
                    
                    if (executor != null) {
                        executor.execute(docTask);
                    } else {
                        docTask.run();
                    }
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }

        // Collect the results of the documents we parsed above 
        
        List<DocumentInfo> docs = new ArrayList<>(docTasks.size());
        for (FutureTask<DocumentInfo> docTask : docTasks) {
            try {
                docs.add(docTask.get());
            } catch (ExecutionException e) {
                throw new ConfigurationException(e);
            } catch (InterruptedException ignored) {
            }
        }

        return docs.toArray(new DocumentInfo[docs.size()]);
    }
    
    public static List<DocumentInfo> getProgrammaticDocuments(List<ApplicationConfigurationPopulator> configPopulators) throws ParserConfigurationException {
        
        List<DocumentInfo> programmaticDocuments = new ArrayList<>();
        
        DOMImplementation domImpl = createDOMImplementation();
        for (ApplicationConfigurationPopulator populator : configPopulators) {
            
            Document facesConfigDoc = createEmptyFacesConfigDocument(domImpl);
            
            try {
                populator.populateApplicationConfiguration(facesConfigDoc);
                
                programmaticDocuments.add(new DocumentInfo(facesConfigDoc, null));
            } catch (Throwable e) {
                if (LOGGER.isLoggable(INFO)) {
                    LOGGER.log(INFO, 
                        "{0} thrown when invoking {1}.populateApplicationConfigurationResources: {2}", 
                        new String [] {
                            e.getClass().getName(),
                            populator.getClass().getName(),
                            e.getMessage()
                        }
                    );
                }
            }
        }
        
        return programmaticDocuments;
    }

    public static DocumentInfo[] mergeDocuments(DocumentInfo[] facesDocuments, List<DocumentInfo> programmaticDocuments) {

        if (programmaticDocuments.isEmpty()) {
            return facesDocuments;
        }

        if (isEmpty(facesDocuments)) {
            return programmaticDocuments.toArray(new DocumentInfo[0]);
        }

        List<DocumentInfo> mergedDocuments = new ArrayList<>(facesDocuments.length + programmaticDocuments.size());

        // The first programmaticDocuments element represents jsf-ri-runtime,
        // and should be the first one in the merged list
        mergedDocuments.add(programmaticDocuments.get(0));

        // Copy the existing facesDocuments next to the merged list
        mergedDocuments.addAll(asList(facesDocuments));

        // Copy the programmaticDocuments next, but skip the first one as we've already added that
        mergedDocuments.addAll(programmaticDocuments.subList(1, programmaticDocuments.size()));

        return mergedDocuments.toArray(new DocumentInfo[0]);
    }
    
    /**
     * <p>
     * Sort the <code>faces-config</code> documents found on the classpath
     * and those specified by the <code>javax.faces.CONFIG_FILES</code> context
     * init parameter.
     * </p>
     *
     * @param facesDocuments an array of <em>all</em> <code>faces-config</code>
     *  documents
     * @param webInfFacesConfig FacesConfigInfo representing the WEB-INF/faces-config.xml
     *  for this app
     *
     * @return the sorted documents
     */
    public static DocumentInfo[] sortDocuments(DocumentInfo[] facesDocuments, FacesConfigInfo webInfFacesConfig) {

        int len = webInfFacesConfig.isWebInfFacesConfig() ? facesDocuments.length - 1 : facesDocuments.length;

        List<String> absoluteOrdering = webInfFacesConfig.getAbsoluteOrdering();

        if (len > 1) {
            List<DocumentOrderingWrapper> list = new ArrayList<>();
            for (int i = 1; i < len; i++) {
                list.add(new DocumentOrderingWrapper(facesDocuments[i]));
            }
            
            DocumentOrderingWrapper[] ordering = list.toArray(new DocumentOrderingWrapper[list.size()]);
            if (absoluteOrdering == null) {
                DocumentOrderingWrapper.sort(ordering);
                
                // Sorting complete, now update the appropriate locations within
                // the original array with the sorted documentation.
                for (int i = 1; i < len; i++) {
                    facesDocuments[i] = ordering[i - 1].getDocument();
                }
                
                return facesDocuments;
            } else {
                DocumentOrderingWrapper[] result = DocumentOrderingWrapper.sort(ordering, absoluteOrdering);
                DocumentInfo[] ret = new DocumentInfo[
                                          webInfFacesConfig.isWebInfFacesConfig() ? 
                                          result.length + 2 : 
                                          result.length + 1];
                
                for (int i = 1; i < len; i++) {
                    ret[i] = result[i - 1].getDocument();
                }
                
                // Add the impl specific config file
                ret[0] = facesDocuments[0];
                
                // Add the WEB-INF if necessary
                if (webInfFacesConfig.isWebInfFacesConfig()) {
                    ret[ret.length - 1] = facesDocuments[facesDocuments.length - 1];
                }
                return ret;
            }
        }

        return facesDocuments;
    }
    
    private static DOMImplementation createDOMImplementation() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        
        return documentBuilderFactory.newDocumentBuilder()
                                     .getDOMImplementation();
    }
    
    private static Document createEmptyFacesConfigDocument(DOMImplementation domImpl) {
        Document document = domImpl.createDocument(JAVAEE_XMLNS, "faces-config", null);
        
        Attr versionAttribute = document.createAttribute("version");
        versionAttribute.setValue("2.2");
        document.getDocumentElement()
                .getAttributes()
                .setNamedItem(versionAttribute);
        
        return document;
    }

}
