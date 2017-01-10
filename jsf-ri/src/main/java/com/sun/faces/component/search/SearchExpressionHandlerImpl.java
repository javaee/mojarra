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
import java.util.Collections;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.search.ComponentNotFoundException;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchKeywordResolver;
import javax.faces.context.FacesContext;

public class SearchExpressionHandlerImpl extends SearchExpressionHandler {

    protected void addHint(SearchExpressionContext searchExpressionContext, SearchExpressionHint hint) {
        // It is a Set already
        if (!searchExpressionContext.getExpressionHints().contains(hint))  {
            searchExpressionContext.getExpressionHints().add(hint);
        }
    }
    
    @Override
    public String resolveClientId(SearchExpressionContext searchExpressionContext, String expression) {
        if (expression == null) {
            expression = "";
        } else {
            expression = expression.trim();
        }

        addHint(searchExpressionContext, SearchExpressionHint.RESOLVE_SINGLE_COMPONENT);
        
        FacesContext facesContext = searchExpressionContext.getFacesContext();
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
        
        if (!expression.isEmpty() && handler.isPassthroughExpression(searchExpressionContext, expression)) {
            return expression;
        }
        
        ResolveClientIdCallback internalCallback = new ResolveClientIdCallback();
        
        if (!expression.isEmpty()) {
            handler.invokeOnComponent(searchExpressionContext, expression, internalCallback);
        }
        
        String clientId = internalCallback.getClientId();

        if (clientId == null && !isHintSet(searchExpressionContext, SearchExpressionHint.IGNORE_NO_RESULT)) {
            throw new ComponentNotFoundException("Cannot find component for expression \""
                + expression + "\" referenced from \""
                + searchExpressionContext.getSource().getClientId(facesContext) + "\".");
        }

        return clientId;
    }
    
    private static class ResolveClientIdCallback implements ContextCallback {
        private String clientId = null;

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target)  {
            if (clientId == null) {
                clientId = target.getClientId(context);
            }
        }

        public String getClientId() {
            return clientId;
        }
    }

    @Override
    public List<String> resolveClientIds(SearchExpressionContext searchExpressionContext, String expressions) {
        if (expressions == null) {
            expressions = "";
        } else {
            expressions = expressions.trim();
        }
        
        FacesContext facesContext = searchExpressionContext.getFacesContext();
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();

        ResolveClientIdsCallback internalCallback = new ResolveClientIdsCallback();

        if (!expressions.isEmpty()) {
            for (String expression : handler.splitExpressions(facesContext, expressions)) {
                if (handler.isPassthroughExpression(searchExpressionContext, expression)) {
                    internalCallback.addClientId(expression);
                } else {
                    handler.invokeOnComponent(searchExpressionContext, expression, internalCallback);
                }
            }
        }

        if (internalCallback.getClientIds() == null && !isHintSet(searchExpressionContext, SearchExpressionHint.IGNORE_NO_RESULT)) {
            throw new ComponentNotFoundException("Cannot find component for expressions \""
                + expressions + "\" referenced from \""
                + searchExpressionContext.getSource().getClientId(facesContext) + "\".");
        }
        
        List<String> clientIds = internalCallback.getClientIds();
        if (clientIds == null) {
            clientIds = Collections.emptyList();
        }
        
        return clientIds;
    }

    private static class ResolveClientIdsCallback implements ContextCallback {
        private List<String> clientIds = null;

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            addClientId(target.getClientId(context));
        }

        public List<String> getClientIds() {
            return clientIds;
        }

        public void addClientId(String clientId) {
            if (clientIds == null) {
                clientIds = new ArrayList<>();
            }
            clientIds.add(clientId);
        }
    }
    
    @Override
    public void resolveComponent(SearchExpressionContext searchExpressionContext, String expression, ContextCallback callback) {
        
        if (expression != null) {
            expression = expression.trim();
        }
        
        addHint(searchExpressionContext, SearchExpressionHint.RESOLVE_SINGLE_COMPONENT);
        
        FacesContext facesContext = searchExpressionContext.getFacesContext();
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();

        ResolveComponentCallback internalCallback = new ResolveComponentCallback(callback);        
        handler.invokeOnComponent(searchExpressionContext, expression, internalCallback);

        if (!internalCallback.isInvoked() && !isHintSet(searchExpressionContext, SearchExpressionHint.IGNORE_NO_RESULT)) {
            throw new ComponentNotFoundException("Cannot find component for expression \""
                + expression + "\" referenced from \""
                + searchExpressionContext.getSource().getClientId(facesContext) + "\".");
        }
    }

    private static class ResolveComponentCallback implements ContextCallback {
        private final ContextCallback callback;
        private boolean invoked;

        public ResolveComponentCallback(ContextCallback callback) {
            this.callback = callback;
            this.invoked = false;
        }

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            if (!isInvoked()) {
                invoked = true;
                callback.invokeContextCallback(context, target);
            }
        }

        public boolean isInvoked() {
            return invoked;
        }
    }
    
    @Override
    public void resolveComponents(SearchExpressionContext searchExpressionContext, String expressions, ContextCallback callback) {
        
        if (expressions != null) {
            expressions = expressions.trim();
        }
        
        FacesContext facesContext = searchExpressionContext.getFacesContext();
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();

        ResolveComponentsCallback internalCallback = new ResolveComponentsCallback(callback);

        if (expressions != null) {
            for (String expression : handler.splitExpressions(facesContext, expressions)) {
                handler.invokeOnComponent(searchExpressionContext, expression, internalCallback);
            }
        }

        if (!internalCallback.isInvoked() && !isHintSet(searchExpressionContext, SearchExpressionHint.IGNORE_NO_RESULT)) {
            throw new ComponentNotFoundException("Cannot find component for expressions \""
                + expressions + "\" referenced from \""
                + searchExpressionContext.getSource().getClientId(facesContext) + "\".");
        }
    }

    private static class ResolveComponentsCallback implements ContextCallback {
        private final ContextCallback callback;
        private boolean invoked;

        public ResolveComponentsCallback(ContextCallback callback) {
            this.callback = callback;
            this.invoked = false;
        }

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            invoked = true;
            callback.invokeContextCallback(context, target);
        }

        public boolean isInvoked() {
            return invoked;
        }
    }
    
    @Override
    public void invokeOnComponent(SearchExpressionContext searchExpressionContext, UIComponent previous, String expression, final ContextCallback callback) {
        
        if (expression == null || previous == null) {
            return;
        }
        
        expression = expression.trim();

        FacesContext facesContext = searchExpressionContext.getFacesContext();
        SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();

        // contains keyword? If not, just try findComponent and don't apply our algorithm
        if (expression.contains(KEYWORD_PREFIX)) {

            // absolute expression and keyword as first command -> try again from ViewRoot
            char separatorChar = facesContext.getNamingContainerSeparatorChar();
            if (expression.charAt(0) == separatorChar && expression.charAt(1) == KEYWORD_PREFIX.charAt(0)) {
                handler.invokeOnComponent(searchExpressionContext, facesContext.getViewRoot(), expression.substring(1), callback);
                return;
            }
 
            String command = extractFirstCommand(facesContext, expression);

            // check if there are remaining keywords/id's after the first command
            String remainingExpression = null;
            if (command.length() < expression.length()) {
                remainingExpression = expression.substring(command.length() + 1);
            }

            if (command.startsWith(KEYWORD_PREFIX)) {
                
                String keyword = command.substring(KEYWORD_PREFIX.length());

                if (remainingExpression == null) {
                    invokeKeywordResolvers(searchExpressionContext, previous, keyword, null, callback);
                } else {

                    if (facesContext.getApplication().getSearchKeywordResolver().isLeaf(searchExpressionContext, keyword)) {
                        throw new FacesException("It's not valid to place a keyword or id after a leaf keyword: "
                                + KEYWORD_PREFIX + keyword + ". Expression: " + expression);
                    }

                    final String finalRemainingExpression = remainingExpression;

                    invokeKeywordResolvers(searchExpressionContext, previous, keyword, remainingExpression, new ContextCallback() {
                        @Override
                        public void invokeContextCallback(FacesContext facesContext, UIComponent target) {
                            handler.invokeOnComponent(searchExpressionContext, target, finalRemainingExpression, callback);
                        }
                    });
                }
            } else {                
                String id = command;

                UIComponent target = previous.findComponent(id);
                if (target != null) {
                    if (remainingExpression == null) {
                        callback.invokeContextCallback(facesContext, target);
                    } else {
                        handler.invokeOnComponent(searchExpressionContext, target, remainingExpression, callback);
                    }
                }
            } 
        }
        else {
            UIComponent target = previous.findComponent(expression);
            if (target != null) {
                callback.invokeContextCallback(facesContext, target);
            } else if (!isHintSet(searchExpressionContext, SearchExpressionHint.SKIP_VIRTUAL_COMPONENTS)) {
                // fallback
                // invokeOnComponent doesnt work with the leading ':'
                char separatorChar = facesContext.getNamingContainerSeparatorChar();
                if (expression.charAt(0) == separatorChar) {
                    expression = expression.substring(1);
                }

                facesContext.getViewRoot().invokeOnComponent(facesContext, expression, callback);
            }
        }
    }

    protected void invokeKeywordResolvers(SearchExpressionContext searchExpressionContext, UIComponent previous,
                             String keyword, String remainingExpression, ContextCallback callback)
    {
        // take the keyword and resolve it using the chain of responsibility pattern.
        SearchKeywordContext searchContext = new SearchKeywordContext(searchExpressionContext, callback, remainingExpression);

        searchExpressionContext.getFacesContext().getApplication()
                .getSearchKeywordResolver().resolve(searchContext, previous, keyword);
    }
    
    @Override
    public String[] splitExpressions(FacesContext context, String expressions) {
        // we can't use a split(",") or split(" ") as keyword parameters might contain spaces or commas        
        List<String> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        char[] separators = getExpressionSeperatorChars(context);

        int parenthesesCounter = 0;

        char[] charArray = expressions.toCharArray();

        for (char c : charArray) {
            if (c == '(') {
                parenthesesCounter++;
            }

            if (c == ')') {
                parenthesesCounter--;
            }

            if (parenthesesCounter == 0) {
                boolean isSeparator = false;
                for (char separator : separators) {
                    if (c == separator) {
                        isSeparator = true;
                    }
                }

                if (isSeparator)  {
                    // lets add token inside buffer to our tokens
                    String bufferAsString = buffer.toString().trim();
                    if (bufferAsString.length() > 0) {
                        tokens.add(bufferAsString);
                    }
                    // now we need to clear buffer
                    buffer.delete(0, buffer.length());
                } else {
                    buffer.append(c);
                }
            } else {
                buffer.append(c);
            }
        }

        // lets not forget about part after the separator
        tokens.add(buffer.toString());

        return tokens.toArray(new String[tokens.size()]);
    }

    @Override
    public boolean isPassthroughExpression(SearchExpressionContext searchExpressionContext, String expression) {
        if (expression != null) {
            expression = expression.trim();
        }

        if (expression != null && expression.contains(KEYWORD_PREFIX)) {
            FacesContext facesContext = searchExpressionContext.getFacesContext();
            String command = extractFirstCommand(facesContext, expression);

            // check if there are remaining commands/id's after the first command
            String remainingExpression = null;
            if (command.length() < expression.length()) {
                remainingExpression = expression.substring(command.length() + 1);
            }

            if (command.startsWith(KEYWORD_PREFIX) && remainingExpression == null) {   
                String keyword = command.substring(KEYWORD_PREFIX.length());

                SearchKeywordResolver keywordResolver = facesContext.getApplication().getSearchKeywordResolver();
                return keywordResolver.isPassthrough(searchExpressionContext, keyword);
            }

            // check again the remainingExpression
            SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
            return handler.isPassthroughExpression(searchExpressionContext, remainingExpression);
        }

        return false;
    }

    @Override
    public boolean isValidExpression(SearchExpressionContext searchExpressionContext, String expression) {
        if (expression != null) {
            expression = expression.trim();
        }
        
        if (expression == null || expression.isEmpty()) {
            return true;
        }

        if (expression.contains(KEYWORD_PREFIX)) {
            FacesContext facesContext = searchExpressionContext.getFacesContext();
            SearchExpressionHandler handler = facesContext.getApplication().getSearchExpressionHandler();
            
            // absolute expression and keyword as first command -> try again from ViewRoot
            char separatorChar = facesContext.getNamingContainerSeparatorChar();
            if (expression.charAt(0) == separatorChar) {
                expression = expression.substring(1);
            }

            String command = extractFirstCommand(facesContext, expression);

            // check if there are remaining commands/id's after the first command
            String remainingExpression = null;
            if (command.length() < expression.length()) {
                remainingExpression = expression.substring(command.length() + 1);
            }

            if (command.startsWith(KEYWORD_PREFIX)) {   
                String keyword = command.substring(KEYWORD_PREFIX.length());

                // resolver for keyword available?
                SearchKeywordResolver keywordResolver = facesContext.getApplication().getSearchKeywordResolver();
                if (!keywordResolver.isResolverForKeyword(searchExpressionContext, keyword)) {
                    return false;
                }

                if (remainingExpression != null && !remainingExpression.trim().isEmpty()) {
                    // there is remaingExpression avialable but the current keyword is leaf -> invalid
                    if (keywordResolver.isLeaf(searchExpressionContext, keyword)) {
                        return false;
                    }

                    return handler.isValidExpression(searchExpressionContext, remainingExpression);
                }
            }
            else {
                if (remainingExpression != null) {
                    return handler.isValidExpression(searchExpressionContext, remainingExpression);
                }
            }
        }

        return true;
    }

    protected boolean isHintSet(SearchExpressionContext searchExpressionContext, SearchExpressionHint hint) {        
        return searchExpressionContext.getExpressionHints().contains(hint);
    }
    
    /**
     * Extract the first command from the expression.
     * @child(1):myId => @child(1)
     * myId:@parent => myId
     * 
     * @param facesContext
     * @param expression
     * @return 
     */
    protected String extractFirstCommand(FacesContext facesContext, String expression) {
        // we can't use a split(":") or split(" ") as keyword parameters might contain spaces or commas   
        int parenthesesCounter = -1;
        int count = -1;

        for (int i = 0; i < expression.length(); i++)  {
            char c = expression.charAt(i);
            if (c == '(') {
                if (parenthesesCounter == -1) {
                    parenthesesCounter = 0;
                }
                parenthesesCounter++;
            }
            if (c == ')') {
                parenthesesCounter--;
            }

            if (parenthesesCounter == 0) {
                //Close first parentheses
                count = i+1;
                break;
            }
            if (parenthesesCounter == -1) {
                if (i > 0 && c == facesContext.getNamingContainerSeparatorChar()) {
                    count = i;
                    break;
                }
            }
        }

        if (count == -1) {
            return expression;
        } else {
            return expression.substring(0, count);
        }
    }
}
