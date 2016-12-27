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

import java.util.List;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * TODO
 */
public abstract class SearchExpressionHandler {

    public static final String KEYWORD_PREFIX = "@";
    
    protected static final char[] EXPRESSION_SEPARATOR_CHARS = new char[] {
        ',', ' '
    };

    /**
     * <p class="changed_added_2_3">Resolves to a single clientId or passthrough expression for the given expression.
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expression the search expression
     *
     * @throws ComponentNotFoundException if the expression can not be resolved
     *  and if {@link SearchExpressionHint#IGNORE_NO_RESULT} was not passed.
     *
     * @return The resolved clientId or passtrough expression. If the expression can not be resolved and if
     *  {@link SearchExpressionHint#IGNORE_NO_RESULT} was passed, <code>null</code> will be returned.
     *
     * @since 2.3
     */
    public abstract String resolveClientId(SearchExpressionContext searchExpressionContext, String expression);

    /**
     * <p class="changed_added_2_3">Resolves to a {@link List} with clientIds or passthrough expressions for the given expressions.
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expressions the search expressions
     *
     * @throws ComponentNotFoundException if one of the expression can not be resolved
     *  and if {@link SearchExpressionHint#IGNORE_NO_RESULT} was not passed.
     *
     * @return The resolved clientIds and passtrough expressions.
     *
     * @since 2.3
     */
    public abstract List<String> resolveClientIds(SearchExpressionContext searchExpressionContext, String expressions);

    /**
     * <p class="changed_added_2_3">Resolves a single {@link UIComponent}s for the given
     * expression. If the component is resolved, the {@link ContextCallback} will be invoked.
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expression the search expression
     * @param callback the callback for the resolved component
     *
     * @throws ComponentNotFoundException if the expression can not be resolved
     * and if {@link SearchExpressionHint#IGNORE_NO_RESULT} was not passed.
     *
     * @since 2.3
     */
    public abstract void resolveComponent(SearchExpressionContext searchExpressionContext, String expression,
            ContextCallback callback);

    /**
     * <p class="changed_added_2_3">Resolves multiple {@link UIComponent}s for the given
     * expression(s). For each resolved component, the {@link ContextCallback} will be invoked.
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expressions the search expression(s)
     * @param callback the callback for each resolved component
     *
     * @throws ComponentNotFoundException if any of the expressions can not be resolved
     * and if {@link SearchExpressionHint#IGNORE_NO_RESULT} was not passed.
     *
     * @since 2.3
     */
    public abstract void resolveComponents(SearchExpressionContext searchExpressionContext, String expressions,
            ContextCallback callback);

    /**
     * <p class="changed_added_2_3">Resolves multiple {@link UIComponent}s for the given
     * expression. For each resolved component, the {@link ContextCallback} will be invoked.
     * 
     * This method is the most essential method in the API.
     * It handles the recursion 
     * It's the core of the search API and does the recursion.
     * 
     * </p>
     * 
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expression the search expression
     * @param callback the callback for the resolved component
     * 
     * @since 2.3
     */
    public void invokeOnComponent(SearchExpressionContext searchExpressionContext,
            String expression, ContextCallback callback) {
        invokeOnComponent(searchExpressionContext, searchExpressionContext.getSource(), expression, callback);
    }

    public abstract void invokeOnComponent(SearchExpressionContext searchExpressionContext,
            UIComponent previous, String expression, ContextCallback callback);

    /**
     * <p class="changed_added_2_3">Splits an string, based on
     * {@link #getExpressionSeperatorChars(javax.faces.context.FacesContext)} with possible multiple expressions into an array.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param expressions The expressions as string
     * @return the expression(s) as array
     *
     * @since 2.3
     */
    public abstract String[] splitExpressions(FacesContext context, String expressions);

    /**
     * <p class="changed_added_2_3">Checks if the given expression is a "passtrough expression".
     * A passthrough expression must only be a keyword.
     * This keyword will not be resolved by the {@link SearchKeywordResolver} and will be returned untouched.
     *
     * The client is responsible to resolve it later.
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expression the expression
     * @return If the given expression is a passtrough expression
     *
     * @since 2.3
     */
    public abstract boolean isPassthroughExpression(SearchExpressionContext searchExpressionContext, String expression);

    /**
     * <p class="changed_added_2_3">Checks if the given expression is a valid expression.
     * 
     * A expression is invalid if:
     *  <ul>
     *   <li>No {@link SearchKeywordResolver} matches the requested keyword</li>
     *   <li>A keyword or id is placed after a leaf keyword (@none:@form)</li>
     *  </ul>
     * </p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param expression the expression
     * @return If the given expression is a passtrough expression
     *
     * @since 2.3
     */
    public abstract boolean isValidExpression(SearchExpressionContext searchExpressionContext, String expression);
    
    /**
     * <p class="changed_added_2_3">Return the characters used to separate expressions
     * in a series of expressions.</p>
     *
     * @param context the {@link FacesContext} for the current request
     * @return the separator chars
     *
     * @since 2.3
     */
    public char[] getExpressionSeperatorChars(FacesContext context) {
        return EXPRESSION_SEPARATOR_CHARS;
    }
}
