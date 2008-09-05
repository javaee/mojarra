/*
 * $Id: DataModel.java,v 1.20 2007/04/27 22:00:09 ofung Exp $
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

package javax.faces.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_2_0">The presence of this annotation on a
 * field of a class annotated with {@link ManagedBean} instructs the
 * system to inject a value into this field whenever the bean is
 * instantiated.  The time of instantiation is dictated by the value of
 * the attributes on the usage of <code>ManagedBean</code> and by the
 * application logic itself.  The value of the {@link #value} attribute
 * may be a literal <code>String</code> or a
 * <code>ValueExpression</code>.  If the latter, the expression must not
 * be evaluated until the bean is instantiated.  The value of the name
 * attribute is taken to be the <em>managed-property-name</em> for this
 * property.  If not specified, the <em>managed-property-name</em> is
 * taken to be the name of the field to which this is attribute is
 * attached.</p>
 *
 * <p class="changed_added_2_0">If this annotation is present on a class
 * that does not have the <code>ManagedBean</code> annotation, the
 * implementation must take no action on this annotation.</p>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManagedProperty {

    /** <p class="changed_added_2_0">Taken to be the
     * <code>managed-property-name</code>.  See class documentation for
     * details.</p>
     */
    String name() default "";


    /** <p class="changed_added_2_0">Taken to be the value that is
     * injected into the field.  See class documentation for
     * details.</p>
     */
    String value();


}
