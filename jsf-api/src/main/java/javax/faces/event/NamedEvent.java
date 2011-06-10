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

package javax.faces.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;


/**
 * <p class="changed_added_2_0">The presence of this annotation on a
 * class automatically registers the class with the runtime as a {@link
 * ComponentSystemEvent} for use with the <code>&lt;f:event /&gt;</code>
 * tag in a page.  The value of the {@link #shortName} attribute is taken to
 * be the short name for the {@link javax.faces.event.ComponentSystemEvent}.
 * If the <em>shortName</em> has already been registered, the current class 
 * must be added to a
 * List of of duplicate events for that name.  If the event name is then 
 * referenced by an
 * application, a <code>FacesException</code> must be thrown listing the 
 * <em>shortName</em> and the offending classes.</p>
 * @since 2.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface NamedEvent {

    /**
     * <p class="changed_added_2_0">The value of this annotation
     * attribute is taken to be the short name for the  {@link
     * javax.faces.event.ComponentSystemEvent}.  If the value of this
     * attribute is ommitted, the following algorithm must be used
     * by the code that processes this annotation to determine its value.</p>
     * 
     * <div class="changed_added_2_0">

	<ol>

	  <li><p>Get the unqualified class name (e.g.,
	  <code>UserLoginEvent</code>) </p></li>

	  <li><p>Strip off the trailing "Event", if present (e.g.,
	  <code>UserLogin</code>) </p></li>

	  <li><p>Convert the first character to lower-case (e.g.,
	  <code>userLogin</code>) </p></li>

	  <li><p>Prepend the package name to the lower-cased name.
	  </p></li>

	</ol>

     *
     * </div>
     */
    String shortName() default "";
}
