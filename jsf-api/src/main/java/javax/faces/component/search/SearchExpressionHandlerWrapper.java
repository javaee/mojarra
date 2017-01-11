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
import javax.faces.FacesWrapper;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_3">Provides a simple implementation of {@link SearchExpressionHandler} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link SearchExpressionHandler} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link SearchExpressionHandler} instance.
 * Usage: extend this class and push the implementation being wrapped to the
 * constructor and use {@link #getWrapped} to access the instance being wrapped.</p>
 *
 * @since 2.3
 */
public abstract class SearchExpressionHandlerWrapper extends SearchExpressionHandler
        implements FacesWrapper<SearchExpressionHandler> {

    private final SearchExpressionHandler wrapped;

    /**
     * <p class="changed_added_2_3">If this search expression handler has been decorated,
     * the implementation doing the decorating should push the implementation being wrapped to this constructor.
     * The {@link #getWrapped()} will then return the implementation being wrapped.</p>
     *
     * @param wrapped The implementation being wrapped.
     * @since 2.3
     */
    public SearchExpressionHandlerWrapper(SearchExpressionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public SearchExpressionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public String resolveClientId(SearchExpressionContext searchExpressionContext, String expression) {
        return getWrapped().resolveClientId(searchExpressionContext, expression);
    }

    @Override
    public List<String> resolveClientIds(SearchExpressionContext searchExpressionContext, String expressions) {
        return getWrapped().resolveClientIds(searchExpressionContext, expressions);
    }

    @Override
    public void resolveComponent(SearchExpressionContext searchExpressionContext, String expression,
            ContextCallback callback) {
        getWrapped().resolveComponent(searchExpressionContext, expression, callback);
    }

    @Override
    public void resolveComponents(SearchExpressionContext searchExpressionContext, String expressions,
            ContextCallback callback) {
        getWrapped().resolveComponents(searchExpressionContext, expressions, callback);
    }

    @Override
    public void invokeOnComponent(SearchExpressionContext searchExpressionContext, String expression,
            ContextCallback callback) {
        getWrapped().invokeOnComponent(searchExpressionContext, expression, callback);
    }

    @Override
    public void invokeOnComponent(SearchExpressionContext searchExpressionContext, UIComponent previous, String expression,
            ContextCallback callback) {
        getWrapped().invokeOnComponent(searchExpressionContext, previous, expression, callback);
    }

    @Override
    public boolean isValidExpression(SearchExpressionContext searchExpressionContext, String expression) {
        return getWrapped().isValidExpression(searchExpressionContext, expression);
    }

    @Override
    public boolean isPassthroughExpression(SearchExpressionContext searchExpressionContext, String expression) {
        return getWrapped().isPassthroughExpression(searchExpressionContext, expression);
    }

    @Override
    public String[] splitExpressions(FacesContext context, String expressions) {
        return getWrapped().splitExpressions(context, expressions);
    }
    
    @Override
    public char[] getExpressionSeperatorChars(FacesContext context) {
        return getWrapped().getExpressionSeperatorChars(context);
    }
}
