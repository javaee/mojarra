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
package com.sun.faces.component.search;

import java.util.ArrayList;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchKeywordResolver;

public class CompositeSearchKeywordResolver extends SearchKeywordResolver {

    private static final int NUMBER_OF_DEFAULT_IMPLEMENTATIONS = 12;
    
    private final ArrayList<SearchKeywordResolver> resolvers;

    public CompositeSearchKeywordResolver() {
        this.resolvers = new ArrayList<>(NUMBER_OF_DEFAULT_IMPLEMENTATIONS);
    }

    public void add(SearchKeywordResolver searchKeywordResolver) {
        if (searchKeywordResolver == null) {
            throw new NullPointerException();
        }

        resolvers.add(0, searchKeywordResolver);
    }

    @Override
    public void resolve(SearchKeywordContext context, UIComponent current, String keyword) {
        context.setKeywordResolved(false);

        for (int i = 0; i < resolvers.size(); i++) {
            SearchKeywordResolver resolver = resolvers.get(i);
            if (resolver.isResolverForKeyword(context.getSearchExpressionContext(), keyword)) {
                resolver.resolve(context, current, keyword);
                if (context.isKeywordResolved()) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean isResolverForKeyword(SearchExpressionContext searchExpressionContext, String keyword) {
        for (int i = 0; i < resolvers.size(); i++) {
            SearchKeywordResolver resolver = resolvers.get(i);
            if (resolver.isResolverForKeyword(searchExpressionContext, keyword)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isPassthrough(SearchExpressionContext searchExpressionContext, String keyword) {
        for (int i = 0; i < resolvers.size(); i++) {
            SearchKeywordResolver resolver = resolvers.get(i);
            if (resolver.isResolverForKeyword(searchExpressionContext, keyword)) {
                return resolver.isPassthrough(searchExpressionContext, keyword);
            }
        }

        return false;
    }

    @Override
    public boolean isLeaf(SearchExpressionContext searchExpressionContext, String keyword) {
        for (int i = 0; i < resolvers.size(); i++) {
            SearchKeywordResolver resolver = resolvers.get(i);
            if (resolver.isResolverForKeyword(searchExpressionContext, keyword)) {
                return resolver.isLeaf(searchExpressionContext, keyword);
            }
        }

        return false;
    }
}
