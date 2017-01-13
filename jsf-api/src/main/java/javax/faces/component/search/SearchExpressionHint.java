/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package javax.faces.component.search;

import javax.faces.component.UIComponent;

/**
 * <p class="changed_added_2_3">An
 * enum that specifies hints that impact
 * the behavior of a component tree search.</p>
 *
 * @since 2.3
 */
public enum SearchExpressionHint {

    /**
     * <p class="changed_added_2_3">
     * Hint that indicates that if a expression resolves to <code>null</code>, <code>null</code> will be returned.
     * Otherwise a {@link ComponentNotFoundException} will be thrown.
     * </p>
     *
     * @since 2.3
     */
    IGNORE_NO_RESULT,

    /**
     * <p class="changed_added_2_3">
     * Hint that indicates that only real {@link UIComponent}s should be resolved.
     * Virtual components are components, which are reused in repeatable components like {@link javax.faces.component.UIData} or <code>ui:repeat</code>.
     * </p>
     *
     * @since 2.3
     */
    SKIP_VIRTUAL_COMPONENTS,
    
    /**
     * <p class="changed_added_2_3">
     * Hint that indicates that only one component should be resolved.
     *
     * This hint is important if a {@link SearchKeywordResolver} uses {@link UIComponent#visitTree},
     * as the tree visit can be terminated after the first component was resolved.
     * 
     * This hint will be automatically added internally if
     *   {@link SearchExpressionHandler#resolveClientId(javax.faces.component.search.SearchExpressionContext, java.lang.String)}
     * or
     *   {@link SearchExpressionHandler#resolveComponent(javax.faces.component.search.SearchExpressionContext, java.lang.String, javax.faces.component.ContextCallback)}
     * is used.
     * </p>
     *
     * @since 2.3
     */
    RESOLVE_SINGLE_COMPONENT,

    /**
     * <p class="changed_added_2_3">
     * Hint that indicates that a keyword can be resolved later and will just be returned as passthrough, if supported by the keyword.
     * For example:
     * The AJAX client- and server-side is able to handle @all or @form.
     * So it's not necessary at all to resolve them to their clientId's on the server side.
     * </p>
     * 
     * @see SearchKeywordResolver#isPassthrough(javax.faces.component.search.SearchExpressionContext, java.lang.String)
     *
     * @since 2.3
     */
    RESOLVE_CLIENT_SIDE
}
