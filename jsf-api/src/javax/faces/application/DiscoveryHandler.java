/*
 * $Id: ConfigManager.java,v 1.24 2008/03/05 21:34:52 rlubke Exp $
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

package javax.faces.application;

import java.util.Set;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><strong>DiscoveryHandler</strong> is the
 * run-time API by which non-container-compatible discovery is
 * performed.  This includes:</p>
 *
 * <div class="changed_added_2_0"> 

 * <ul>
 *
 * <li><p>discovery and processing of all the startup time annotations
 * defined in JavaServer Faces.</p></li>
 *
 * <li><p>discovery of resources on the classpath.</p></li>
 *
 * </ul>
 *
 * <p><strong>Usage</strong></p>
 *
 * <ul>
 *
 * <p>The specification for the usage of this class in the context of
 * each of the above features follows.</p>
 *
 * <p><strong><a name="startupTimeAnnotationProcessingAlgorithm">Startup
 * Time Annotation Processing Algorithm</a></strong></p>
 * 
 *  <ul>

 *     <li><p>Call {@link #getClassNamesWithFacesAnnotations}, passing
 *     the startup time {@link FacesContext}.</p>

 *     </li>
 *
 *     <li><p>Call {@link #processAnnotatedClasses}, passing the
 *     startup time <code>FacesContext</code> and the result from
 *     the previous step.</p></li>
 *
 *   </ul>

 * </ul>
 *
 * <p>The runtime must employ the decorator pattern as for every other
 * pluggable artifact in JSF.</p>

 *
 * </div>
 *
 * @since 2.0
 */
public abstract class DiscoveryHandler {

    /**
     * <p class="changed_added_2_0">Return a set of strings where each
     * string is a fully qualified java class name that must be
     * inspected for the presence of startup time annotations.</p>
     *
     * <div class="changed_added_2_0">

     * <p><strong><a name="configAnnotationScanningSpecification">Algorithm for
     * scanning classes for configuration annotations</a></strong></p>
     
     * <p>The following algorithm or one semantically equivalent to it must
     * be followed to scan the classes available to the application for the
     * presence of annotations that take the place of elements in the
     * application configuration resources.  Each annotation for which this
     * algorithm applies will specifically reference this algorithm in its
     * javadoc.</p>
     
     * <ul>
     
     * <li><p>If the <code>&lt;faces-config&gt;</code> element in the
     * <code>WEB-INF/faces-config.xml</code> file contains
     * <code>metadata-complete</code> attribute whose value is
     * <code>"true"</code>, the implementation must not perform annotation
     * scanning on any classes except for those classes provided by the
     * implementation itself.  Otherwise, continue as follows.</p></li>
     
     * <li><p>All classes in <code>WEB-INF/classes</code> must be
     * scanned.</p></li>
     
     * <li><p>For every jar in the application's <code>WEB-INF/lib</code>
     * directory, if the jar contains a
     * <code>META-INF/faces-config.xml</code> file (even an empty one), all
     * classes in that jar must be scanned.</p></li>
     *
     
     * </ul>

     * </div>

     * @param context the startup time <code>FacesContext</code>
     * for this application.
     *
     * @since 2.0
     */
    public abstract Set<String> getClassNamesWithFacesAnnotations(FacesContext context);
    
    /**
     * <p class="changed_added_2_0">Given the set of strings obtained
     * from {@link #getClassNamesWithFacesAnnotations}, process each
     * class to look for and handle startup time annotations as
     * described in the javadoc for each annotation.</p>
     
     * <div class="changed_added_2_0">

     * <p>The implementation must guarantee that any conflicts between a
     * constituent of the application configuration resources and a
     * constituent of the processed annotations are resolved to favor
     * the constituent in the application configuration resources.</p>
     *
     * </div>

     * @param context the startup time <code>FacesContext</code>
     * for this application.

     * @param annotatedClassnames the <code>Set&lt;String&gt;</code>
     * returned from <code>getClassNamesWithFacesAnnotations()</code>.
     *
     * @since 2.0
     */
    public abstract void processAnnotatedClasses(FacesContext context,
                                                 Set<String> annotatedClassnames);


}
