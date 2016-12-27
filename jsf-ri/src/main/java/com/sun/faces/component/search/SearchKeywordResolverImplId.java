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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

public class SearchKeywordResolverImplId extends AbstractSearchKeywordResolverImpl {

    private static final Pattern PATTERN = Pattern.compile("id\\((\\w+)\\)");

    @Override
    public void resolve(SearchKeywordContext searchKeywordContext, UIComponent previous, String command) {
        FacesContext facesContext = searchKeywordContext.getSearchExpressionContext().getFacesContext();

        String id = extractId(command);

        if (isHintSet(searchKeywordContext.getSearchExpressionContext(), SearchExpressionHint.SKIP_VIRTUAL_COMPONENTS)) {
            // Avoid visit tree because in this case we need real component instances.
            // This means components inside UIData will not be scanned. 
            findWithId(facesContext, id, previous, searchKeywordContext.getCallback());
        } else {
            previous.visitTree(
                    VisitContext.createVisitContext(facesContext, null, searchKeywordContext.getSearchExpressionContext().getVisitHints()),
                    new VisitCallback() {
                        @Override
                        public VisitResult visit(VisitContext context, UIComponent target) {
                            if (id.equals(target.getId())) {
                                searchKeywordContext.invokeContextCallback(target);

                                if (isHintSet(searchKeywordContext.getSearchExpressionContext(), SearchExpressionHint.RESOLVE_SINGLE_COMPONENT)) {
                                    return VisitResult.COMPLETE;
                                }

                                return VisitResult.ACCEPT;
                            } else {
                                return VisitResult.ACCEPT;
                            }
                        }
                    });
        }

        searchKeywordContext.setCommandResolved(true);
    }
    
    @Override
    public boolean matchKeyword(SearchExpressionContext searchExpressionContext, String command) {

        if (command.length() > 5 && command.substring(0, "id".length()).equalsIgnoreCase("id")) {
            try {
                Matcher matcher = PATTERN.matcher(command);
                return matcher.matches();
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    protected String extractId(String expression) {
        try {
            Matcher matcher = PATTERN.matcher(expression);
            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                throw new FacesException("Expression does not match following pattern @id(id). Expression: \""
                        + expression + "\"");
            }

        } catch (Exception e) {
            throw new FacesException("Expression does not match following pattern @id(id). Expression: \""
                    + expression + "\"", e);
        }
    }

    private void findWithId(FacesContext context, String id, UIComponent base, ContextCallback callback) {

        if (id.equals(base.getId())) {
            callback.invokeContextCallback(context, base);
        }

        if (base.getFacetCount() > 0) {
            for (UIComponent facet : base.getFacets().values()) {
                findWithId(context, id, facet, callback);
            }
        }

        if (base.getChildCount() > 0) {
            for (int i = 0, childCount = base.getChildCount(); i < childCount; i++) {
                UIComponent child = base.getChildren().get(i);
                findWithId(context, id, child, callback);
            }
        }
    }
}
