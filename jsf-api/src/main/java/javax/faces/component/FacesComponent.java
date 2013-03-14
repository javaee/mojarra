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
 * scanning algorithm in section JSF.11.5, {@link
 * javax.faces.application.Application#addComponent(java.lang.String,java.lang.String)}
 * is called, passing the derived <em>component-type</em> as the first
 * argument and the derived <em>component-class</em> as the second
 * argument.  The implementation must guarantee that all such calls to
 * <code>addComponent()</code> happen during application startup time
 * and before any requests are serviced.</p>

 */ 

/**
 * <p><span class="changed_modified_2_2">The</span> presence of this annotation
 * on a class that extends {@link UIComponent} must cause the runtime to 
 * register this class as a component suitable for inclusion in a view.
 * <span class="changed_added_2_2">If the <code>createTag</code> attribute
 * is <code>true</code>, the runtime must create a corresponding Facelet
 * tag handler according to the rules specified in the attributes of 
 * this annotation.</span></p>
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FacesComponent {
    
    /**
     * <p class="changed_added_2_2">Components that declare a 
     * <code>createTag = true</code> attribute will be placed into this tag
     * namespace if the namespace attribute is omitted.</p>
     */
    public static final String NAMESPACE = "http://xmlns.jcp.org/jsf/component";

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span>
     * value of this annotation attribute is taken to be the 
     * <em>component-type</em> with which instances of this class of component can be instantiated by
     * calling {@link
     * javax.faces.application.Application#createComponent(java.lang.String)}.
     * <span class="changed_added_2_2">If no value is specified, or the value is
     * <code>null</code>, the value is taken to be the return of calling
     * <code>getSimpleName</code> on the class to which this annotation
     * is attached and lowercasing the first character.  If more than one
     * component with this derived name is found, the results are undefined.</span></p>
     */ 

    String value() default "";
    
    /**
     * <p class="changed_added_2_2">If the value of this attribute is 
     * <code>true</code>, the runtime must create a Facelet tag handler, 
     * that extends from {@link javax.faces.view.facelets.ComponentHandler},
     * suitable for use in pages under the tag library with namespace given
     * by the value of the {@link #namespace} attribute.</p>
     */
    
    boolean createTag() default false;
    
    /**
     * <p class="changed_added_2_2">If the value of the {@link #createTag} 
     * attribute is <code>true</code>, the runtime must use this
     * value as the tag name for including an instance of the component
     * annotated with this annotation in a view.  If this attribute is not
     * specified on a usage of this annotation, the simple name of the 
     * class on which this annotation is declared, with the first character 
     * lowercased, is taken to be the value.</p>
     */
    String tagName() default "";
    
    /**
     * <p class="changed_added_2_2">If the value of the {@link #createTag} 
     * attribute is <code>true</code>, the value of this attribute is taken
     * to be the tag library namespace into which this component is placed.</p>
     * 
     */
    
    String namespace() default NAMESPACE;
    
}
