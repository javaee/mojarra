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

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;

/**
 * <p class="changed_added_2_3"><strong>SearchKeywordContext</strong>
 * provides context information that may be useful to 
 * {@link SearchKeywordResolver#resolve} implementations.
 * </p>
 *
 * @since 2.3
 */
public class SearchKeywordContext {

    private final SearchExpressionContext searchExpressionContext;
    private final ContextCallback callback;
    private final String remainingExpression;
    
    private boolean keywordResolved;

    /**
     * <p class="changed_added_2_3">Construct a new context with the given arguments.</p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext} for the current request.
     * @param callback the {@link ContextCallback}.
     * @param remainingExpression the remaining expression.
     */
    public SearchKeywordContext(SearchExpressionContext searchExpressionContext, ContextCallback callback, String remainingExpression) {
        this.searchExpressionContext = searchExpressionContext;
        this.callback = callback;
        this.remainingExpression = remainingExpression;
    }

    /**
     * <p class="changed_added_2_3">This method will be called by an implementation of {@link
     * SearchKeywordResolver#resolve} with the resolved component for the keyword.</p>
     *
     * @param target the resolved {@link UIComponent}.
     * 
     * @since 2.3
     */
    public void invokeContextCallback(UIComponent target) {
        keywordResolved = true;
        callback.invokeContextCallback(searchExpressionContext.getFacesContext(), target);
    }

    /**
     * <p class="changed_added_2_3">Returns the {@link SearchExpressionContext} for the current request.</p>
     * 
     * @return the {@link SearchExpressionContext}.
     * 
     * @since 2.3
     */
    public SearchExpressionContext getSearchExpressionContext() {
        return searchExpressionContext;
    }

    /**
     * <p class="changed_added_2_3">Returns the {@link ContextCallback} for the current request.</p>
     * 
     * @return the {@link ContextCallback}.
     * 
     * @since 2.3
     */
    public ContextCallback getCallback() {
        return callback;
    }

    /**
     * <p class="changed_added_2_3">Returns the remaining expression for the current request.</p>
     * 
     * @return the remaining expression.
     * 
     * @since 2.3
     */
    public String getRemainingExpression() {
        return remainingExpression;
    }

    /**
     * <p class="changed_added_2_3">Returns if the keyword was resolved.</p>
     * 
     * @return if the keyword was resolved.
     * 
     * @since 2.3
     */
    public boolean isKeywordResolved() {
        return keywordResolved;
    }

    /**
     * <p class="changed_added_2_3">Sets if the keyword was resolved.</p>
     * 
     * @param keywordResolved if the keyword was resolved.
     * 
     * @since 2.3
     */
    public void setKeywordResolved(boolean keywordResolved) {
        this.keywordResolved = keywordResolved;
    }
}
