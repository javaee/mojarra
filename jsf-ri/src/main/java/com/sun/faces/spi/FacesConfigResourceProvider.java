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

package com.sun.faces.spi;

/**
 * <p> Classes that implement this interface return zero or more
 * <code>URL</code>s which refer to application configuration resources (i.e.
 * documents conforming the faces-config DTD or Schema). </p>
 *
 * <p>
 * Implementations of this interface are made known to the runtime using
 * service discovery.
 * </p>
 *
 * <p>For example:</p>
 *
 * <pre>
 *     META-INF/services/com.sun.faces.spi.FacesConfigResourceProvider
 * </pre>
 *
 * <p>
 * The file, <code>com.sun.faces.spi.FacesConfigResourceProvider</code>,
 * contains a single line which represents the fully qualified class name
 * of the concrete <code>FacesConfigResourceProvider</code>.
 * </p>
 *
 * <p>
 * The <code>FacesConfigResourceProvider</code> instances that are found
 * will be inserted into a List of existing <code>ConfigurationResourceProviders</code>
 * <em>after</em> those that process <code>faces-config.xml</code> files in <code>META-INF</code>
 * but <em>before</em> those that process <code>faces-config.xml</code> files in the
 * web application.  If the documents returned by this <code>ConfigurationResourceProvider</code>
 * instance require specific ordering semantics, then use the partial or absolute ordering
 * feature provided by the JavaServer Faces 2.0 specification.
 * </p>
 */
public interface FacesConfigResourceProvider extends ConfigurationResourceProvider {

    public static final String SERVICES_KEY = "com.sun.faces.spi.FacesConfigResourceProvider";
    
}
