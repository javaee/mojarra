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
package com.sun.faces.config.manager.tasks;

import static java.util.Collections.emptyList;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;

import com.sun.faces.config.ConfigManager;
import com.sun.faces.spi.ConfigurationResourceProvider;


/**
 * <p>
 *  This <code>Callable</code> will be used by {@link ConfigManager#getXMLDocuments(javax.servlet.ServletContext, java.util.List, java.util.concurrent.ExecutorService, boolean)}.
 *  It represents one or more URIs to configuration resources that require processing.
 * </p>
 */
public class FindConfigResourceURIsTask implements Callable<Collection<URI>> {

    private ConfigurationResourceProvider provider;
    private ServletContext servletContext;


    // -------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>URITask</code> instance.
     * @param provider the <code>ConfigurationResourceProvider</code> from
     *  which zero or more <code>URL</code>s will be returned
     * @param servletContext the <code>ServletContext</code> of the current application
     */
    public FindConfigResourceURIsTask(ConfigurationResourceProvider provider, ServletContext servletContext) {
        this.provider = provider;
        this.servletContext = servletContext;
    }


    // ----------------------------------------------- Methods from Callable


    /**
     * @return zero or more <code>URL</code> instances
     * @throws Exception if an Exception is thrown by the underlying
     *  <code>ConfigurationResourceProvider</code> 
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<URI> call() throws Exception {
        Collection<?> untypedCollection = provider.getResources(servletContext);
        Iterator<?> untypedCollectionIterator = untypedCollection.iterator();
        
        Collection<URI> result = emptyList();
        
        if (untypedCollectionIterator.hasNext()) {
            Object cur = untypedCollectionIterator.next();
            
            // Account for older versions of the provider that return Collection<URL>.
            if (cur instanceof URL) {
                result = new ArrayList<>(untypedCollection.size());
                result.add(new URI(((URL)cur).toExternalForm()));
                while (untypedCollectionIterator.hasNext()) {
                    cur = untypedCollectionIterator.next();
                    result.add(new URI(((URL)cur).toExternalForm()));
                }
            } else {
                result = (Collection<URI>) untypedCollection;
            }
        }

        return result;
    }

} 