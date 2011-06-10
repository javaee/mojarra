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

package javax.faces.bean;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0">The presence of this annotation on a
 * class automatically registers the class with the runtime as a managed
 * bean class.  Classes must be scanned for the presence of this
 * annotation at application startup, before any requests have been
 * serviced.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>The value of the {@link #name} attribute is taken to be the
 * <em>managed-bean-name</em>.  If the value of the <em>name</em>
 * attribute is unspecified or is the empty <code>String</code>, the
 * <em>managed-bean-name</em> is derived from taking the unqualified
 * class name portion of the fully qualified class name and converting
 * the first character to lower case.  For example, if the
 * <code>ManagedBean</code> annotation is on a class with the fully
 * qualified class name <code>com.foo.Bean</code>, and there is no
 * <em>name</em> attribute on the annotation, the
 * <em>managed-bean-name</em> is taken to be <code>bean</code>.  The
 * fully qualified class name of the class to which this annotation is
 * attached is taken to be the <em>managed-bean-class</em>.</p>

 * <p>The scope of the managed bean is declared using one of {@link
 * NoneScoped}, {@link RequestScoped}, {@link ViewScoped}, {@link
 * SessionScoped}, {@link ApplicationScoped}, or {@link CustomScoped}
 * annotations.  If the scope annotations are omitted, the bean must be
 * handled as if the {@link RequestScoped} annotation is present.</p>

 * <p> If the value of the {@link #eager} attribute is
 * <code>true</code>, and the <code>managed-bean-scope</code> value is
 * "application", the runtime must instantiate this class when the
 * application starts.  This instantiation and storing of the instance
 * must happen before any requests are serviced.  If <em>eager</em> is
 * unspecified or <code>false</code>, or the
 * <code>managed-bean-scope</code> is something other than
 * "application", the default "lazy" instantiation and scoped storage of
 * the managed bean happens.</p>
 *
 * <p>When the runtime processes this annotation, if a managed bean
 * exists whose name is equal to the derived <em>managed-bean-name</em>,
 * a <code>FacesException</code> must be thrown and the
 * application must not be placed in service.</p>
 *
 * <p>A class tagged with this annotation must have a public
 * zero-argument constructor.  If such a constructor is not defined on
 * the class, a <code>FacesException</code> must be thrown and the
 * application must not be placed in service.</p>
 *
 * </div>
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ManagedBean {

    /** <p class="changed_added_2_0">Taken to be the
     * <code>managed-bean-name</code>.  See class documentation for
     * details.</p>
     */

    String name() default "";


    /** <p class="changed_added_2_0">Taken to be the value of the
     * <code>eager</code> attribute of the <code>managed-bean</code>.
     * See class documentation for details.</p>
     */
    boolean eager() default false;

}
