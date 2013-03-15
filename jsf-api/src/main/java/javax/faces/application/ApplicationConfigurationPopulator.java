/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.application;

import org.w3c.dom.Document;

/**

 * <p class="changed_added_2_2">This class defines a {@code java.util.ServiceLoader}
 * service which enables programmatic configuration of the JSF runtime using the existing
 * Application Configuration Resources schema. See the section "Application Startup
 * Behavior" in the specification prose document for the specification on when
 * and how implementations of this service are used.</p>
 * 
 * @since 2.2
 * 
 */
public abstract class ApplicationConfigurationPopulator {
    
    /**
     * <p class="changed_added_2_2">Service providers that implement
     * this service must be called by the JSF runtime
     * exactly once for each implementation, at startup, before any requests have 
     * been serviced.  Before calling the 
     * {@link #populateApplicationConfiguration} method,
     * the runtime must ensure that the {@code Document} argument is empty aside
     * from being pre-configured to be in the proper namespace for an
     * Application Configuration Resources file: 
     * {@code http://xmlns.jcp.org/xml/ns/javaee}.  Implementations of this service
     * must ensure that any changes made to the argument
     * {@code Document} conform to that schema <a target="_"
     * href="../../web-facesconfig.html">as defined in the
     * specification</a>.  The JSF runtime is not required to validate the
     * {@code Document} after control returns from the service implementation,
     * though it may do so.</p>
     * 
     * <div class="changed_added_2_2">
     * 
     * <p>Ordering of Artifacts</p>
     * 
     * <p>If the document is made to contain an {@code <ordering>} element,
     * as specified in the section <em>Ordering of Artifacts</em> in the
     * specification prose document, the document will be prioritized
     * accordingly.  Otherwise, the runtime must place the document in the
     * list of other Application Configuration Resources documents at the
     * "lowest" priority, meaning any conflicts that may arise between the
     * argument document and any other Application Configuration Resources
     * are resolved in favor of the other document.</p>
     * 
     * </div>
     * 
     * @param toPopulate The Document to populate with configuration.
     * 
     * @since 2.2
     */
    
    public abstract void populateApplicationConfiguration(Document toPopulate);
    
}
