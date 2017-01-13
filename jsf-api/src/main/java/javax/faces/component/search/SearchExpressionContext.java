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

import java.util.Set;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_3">A context object that is used to hold 
 * state relating to resolve a search expression.</p>
 *
 * @see SearchExpressionHandler
 *
 * @since 2.3
 */
public abstract class SearchExpressionContext {

    /**
     * <p class="changed_added_2_3">Returns the source / base component from which we will start to perform our search.</p>
     *
     * @return the source component.
     *
     * @since 2.3
     */
    public abstract UIComponent getSource();

    /**
     * <p class="changed_added_2_3">Returns hints that influence the behavior of the tree visit,
     * if it's used by an {@link SearchKeywordResolver} implementation.</p>
     *
     * @return a non-empty, unmodifiable collection of {@link VisitHint}s
     *
     * @since 2.3
     * @see javax.faces.component.visit.VisitContext#getHints()
     */
    public abstract Set<VisitHint> getVisitHints();

    /**
     * <p class="changed_added_2_3">Returns hints that influence the behavior of resolving the expression.</p>
     *
     * @return a non-empty, unmodifiable collection of {@link SearchExpressionHint}s
     *
     * @since 2.3
     */
    public abstract Set<SearchExpressionHint> getExpressionHints();

    /**
     * <p class="changed_added_2_3">Returns the FacesContext for the
     * current request.</p>
     *
     * @return the FacesContext.
     *
     * @since 2.3
     */
    public abstract FacesContext getFacesContext();

    /**
     * <p class="changed_added_2_3">Creates a {@link SearchExpressionContext} instance
     * for use with the {@link SearchExpressionHandler}.
     * This method can be used to obtain a SearchExpressionContext instance
     * without any {@link VisitHint} or {@link SearchExpressionHint}.</p>
     *
     * @param context the FacesContext for the current request
     * @param source the source / base component from which we will start to perform our search.
     *
     * @return a {@link SearchExpressionContext} instance
     *
     * @since 2.3
     */
    public static SearchExpressionContext createSearchExpressionContext(FacesContext context, UIComponent source) {
        return createSearchExpressionContext(context, source, null, null);
    }

    /**
     * <p class="changed_added_2_3">Creates a {@link SearchExpressionContext} instance
     * for use with the {@link SearchExpressionHandler}.</p>
     *
     * @param context the FacesContext for the current request
     * @param source the source / base component from which we will start to perform our search.
     * @param expressionHints the SearchExpressionHint to apply to the search.
     *                  If <code>null</code>, no hints are applied.
     * @param visitHints the VisitHints to apply to the visit, if used by a {@link SearchKeywordResolver}.
     *                  If <code>null</code>, no hints are applied.
     *
     * @return a {@link SearchExpressionContext} instance
     *
     * @since 2.3
     */
    public static SearchExpressionContext createSearchExpressionContext(FacesContext context,  UIComponent source,
            Set<SearchExpressionHint> expressionHints, Set<VisitHint> visitHints) {

        SearchExpressionContextFactory factory
                = (SearchExpressionContextFactory) FactoryFinder.getFactory(FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY);
        return factory.getSearchExpressionContext(context, source, expressionHints, visitHints);
    }
}
