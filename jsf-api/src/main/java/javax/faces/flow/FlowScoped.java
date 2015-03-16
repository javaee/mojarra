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
package javax.faces.flow;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;

/**
 * <p class="changed_added_2_2"><strong class="changed_modified_2_3">
 * FlowScoped</strong> is a CDI
 * scope that causes the runtime to consider classes with this
 * annotation to be in the scope of the specified {@link Flow}.  The
 * implementation must provide an implementation of {@code
 * javax.enterprise.inject.spi.Extension} that implements the semantics
 * such that beans with this annotation are created when the user enters
 * into the specified {@code Flow}, and de-allocated when the user exits
 * the specified {@code Flow}.  See {@link FlowHandler#transition} for
 * the specification of flow entry and exit.</p>
 * 
 * <p class="changed_added_2_3">When replacing (rather than decorating) the flow 
 * implementation with a custom {@link FlowHandler} implementation, it is necessary
 * to also replace the CDI extension that implements the specified 
 * behavior regarding <code>FlowScoped</code> beans.</p>
 * 
 * @since 2.2
 */


@NormalScope
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FlowScoped {

   
    /**
     * <p class="changed_added_2_2">Must be equivalent to the {@link
     * Flow#getId} of a defined flow for this application.</p>
     *
     * @since 2.2
     */
    String value();

    /**
     * <p class="changed_added_2_2">If not empty, declare the defining
     * document id within which the {@link Flow} referenced by {@link
     * #value} is unique.  If empty the, the runtime assumes that all flow
     * ids are unique within the scope of the application.</p>
     *
     * @since 2.2
     */
    
    String definingDocumentId() default "";

}
