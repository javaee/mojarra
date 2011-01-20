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

package javax.faces.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0">The presence of this annotation on a
 * class automatically registers the class with the runtime as a {@link
 * Renderer}.  The value of the {@link #renderKitId} attribute is taken
 * to be the <em>render-kit-id</em> to which an instance of this
 * <code>Renderer</code> is to be added.  There must be a public
 * zero-argument constructor on any class where this annotation appears.
 * The implementation must indicate a fatal error if such a constructor
 * does not exist and the application must not be placed in service.
 * Within that {@link RenderKit}, The value of the {@link #rendererType}
 * attribute is taken to be the <em>renderer-type</em>, and the value of
 * the {@link #componentFamily} attribute is to be taken as the
 * <em>component-family</em>.  The implementation must guarantee that
 * for each class annotated with <code>FacesRenderer</code>, found with
 * the algorithm in section JSF.11.5,
 * the following actions are taken.</p>

 * <div class="changed_added_2_0">

 * <ul>

 * 	  <li><p>Obtain a reference to the {@link RenderKitFactory} for
 * 	  this application.</p></li>

	  <li><p>See if a <code>RenderKit</code> exists for
	  <em>render-kit-id</em>.  If so, let that instance be
	  <em>renderKit</em> for discussion.  If not, the implementation
	  must indicate a fatal error if such a <code>RenderKit</code>
	  does not exist and the application must not be placed in
	  service.</p></li>

	  <li><p>Create an instance of this class using the public
	  zero-argument constructor.</p></li>

	  <li><p>Call {@link RenderKit#addRenderer} on
	  <em>renderKit</em>, passing <em>component-family</em> as the
	  first argument, <em>renderer-type</em> as the second, and the
	  newly instantiated <code>RenderKit</code> instance as the
	  third argument.</p></li>

 * </ul>


 * </div>

 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FacesRenderer {


    /**
     * <p class="changed_added_2_0">The value of this annotation
     * attribute is taken to be the <em>render-kit-id</em> in which an
     * instance of this class of <code>Renderer</code> must be
     * installed.</p>
     */ 

    String renderKitId() default RenderKitFactory.HTML_BASIC_RENDER_KIT;


    /**
     * <p class="changed_added_2_0">The value of this annotation
     * attribute is taken to be the <em>renderer-type</em> which, in
     * combination with {@link #componentFamily} can be used to obtain a
     * reference to an instance of this {@link Renderer} by calling
     * {@link javax.faces.render.RenderKit#getRenderer(java.lang.String,
     * java.lang.String)}.</p>
     */ 

    String rendererType();


    /**
     * <p class="changed_added_2_0">The value of this annotation
     * attribute is taken to be the <em>component-family</em> which, in
     * combination with {@link #rendererType} can be used to obtain a
     * reference to an instance of this {@link Renderer} by calling
     * {@link javax.faces.render.RenderKit#getRenderer(java.lang.String,
     * java.lang.String)}.</p>
     */ 

    String componentFamily();


}
