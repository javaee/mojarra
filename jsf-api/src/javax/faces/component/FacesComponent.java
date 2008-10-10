/*
 * $Id: Renderer.java,v 1.39.12.7 2008/04/17 18:51:29 edburns Exp $
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

package javax.faces.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;


/**
 * <p class="changed_added_2_0">The presence of this annotation on a
 * class automatically registers the class with the runtime as a {@link
 * UIComponent}.  The value of the {@link #value} attribute is taken to
 * be the <em>component-type</em> and the fully qualified class name of
 * the class to which this annotation is attached is taken to be the
 * <em>component-class</em>.  The implementation must guarantee that for
 * each class annotated with <code>FacesComponent</code>, found with the
 * scanning algorithm described below, {@link
 * javax.faces.application.Application#addComponent(java.lang.String,java.lang.String)}
 * is called, passing the derived <em>component-type</em> as the first
 * argument and the derived <em>component-class</em> as the second
 * argument.  The implementation must guarantee that all such calls to
 * <code>addComponent()</code> happen during application startup time
 * and before any requests are serviced.</p>

 * <div class="changed_added_2_0">

 * <p><em><a
 * name="componentConfigAnnotationScanningSpecification">Algorithm for
 * scanning classes for component configuration annotations</a></em></p>

 * <p>The following algorithm or one semantically equivalent to it must
 * be followed to scan the classes available to the application for the
 * presence of this and other similar annotations.  Each annotation for
 * which this algorithm applies will specifically reference this
 * algorithm in its javadoc.</p>

 * <ul>

 * <li><p>If the <code>&lt;faces-config&gt;</code> element in the
 * <code>WEB-INF/faces-config.xml</code> file contains
 * <code>metadata-complete</code> attribute whose value is
 * <code>"true"</code>, the implementation must not perform annotation
 * scanning on any classes except for those classes provided by the
 * implementation itself.  Otherwise, continue as follows.</p></li>

 * <li><p>All classes in <code>WEB-INF/classes</code> must be scanned.</p></li>

 * <li><p>For every jar in the application's <code>WEB-INF/lib</code>
 * directory, if the jar contains a
 * <code>META-INF/faces-config.xml</code> file (even an empty one), all
 * classes in that jar must be scanned.</p></li>
 *
 * </ul>
 *
 * </div>
 */ 

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FacesComponent {

    /**
     * <p class="changed_added_2_0">The value of this annotation
     * attribute is taken to be the <em>component-type</em> with which
     * instances of this class of component can be instantiated by
     * calling {@link
     * javax.faces.application.Application#createComponent(java.lang.String)}</p>
     */ 

    String value();


}
