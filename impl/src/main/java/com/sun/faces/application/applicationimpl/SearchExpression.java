/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.application.applicationimpl;

import static com.sun.faces.util.MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID;
import static com.sun.faces.util.Util.notNull;
import static java.util.logging.Level.FINE;

import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchKeywordResolver;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.component.search.CompositeSearchKeywordResolver;
import com.sun.faces.component.search.SearchKeywordResolverImplAll;
import com.sun.faces.component.search.SearchKeywordResolverImplChild;
import com.sun.faces.component.search.SearchKeywordResolverImplComposite;
import com.sun.faces.component.search.SearchKeywordResolverImplForm;
import com.sun.faces.component.search.SearchKeywordResolverImplId;
import com.sun.faces.component.search.SearchKeywordResolverImplNamingContainer;
import com.sun.faces.component.search.SearchKeywordResolverImplNext;
import com.sun.faces.component.search.SearchKeywordResolverImplNone;
import com.sun.faces.component.search.SearchKeywordResolverImplParent;
import com.sun.faces.component.search.SearchKeywordResolverImplPrevious;
import com.sun.faces.component.search.SearchKeywordResolverImplRoot;
import com.sun.faces.component.search.SearchKeywordResolverImplThis;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

public class SearchExpression {
    
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    
    private final ApplicationAssociate associate;
    
    private CompositeSearchKeywordResolver searchKeywordResolvers;

    public SearchExpression(ApplicationAssociate applicationAssociate) {
        this.associate = applicationAssociate;
        
        searchKeywordResolvers = new CompositeSearchKeywordResolver();
        
        searchKeywordResolvers.add(new SearchKeywordResolverImplThis());
        searchKeywordResolvers.add(new SearchKeywordResolverImplParent());
        searchKeywordResolvers.add(new SearchKeywordResolverImplForm());
        searchKeywordResolvers.add(new SearchKeywordResolverImplComposite());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNext());
        searchKeywordResolvers.add(new SearchKeywordResolverImplPrevious());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNone());
        searchKeywordResolvers.add(new SearchKeywordResolverImplNamingContainer());
        searchKeywordResolvers.add(new SearchKeywordResolverImplRoot());
        searchKeywordResolvers.add(new SearchKeywordResolverImplId());
        searchKeywordResolvers.add(new SearchKeywordResolverImplChild());
        searchKeywordResolvers.add(new SearchKeywordResolverImplAll());
        
        if (associate.getSearchKeywordResolversFromFacesConfig() != null) {
            for (SearchKeywordResolver resolver : associate.getSearchKeywordResolversFromFacesConfig()) {
                searchKeywordResolvers.add(resolver);
            }
        }
    }
    
    public SearchExpressionHandler getSearchExpressionHandler() {
        return associate.getSearchExpressionHandler();
    }

    public void setSearchExpressionHandler(SearchExpressionHandler searchExpressionHandler) {
        notNull("searchExpressionHandler", searchExpressionHandler);

        associate.setSearchExpressionHandler(searchExpressionHandler);

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("Set SearchExpressionHandler Instance to ''{0}''", searchExpressionHandler.getClass().getName()));
        }
    }

    public void addSearchKeywordResolver(SearchKeywordResolver resolver) {
        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                    MessageUtils.getExceptionMessageString(ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "SearchKeywordResolver"));
        }

        searchKeywordResolvers.add(resolver);
    }

    public SearchKeywordResolver getSearchKeywordResolver() {
        return searchKeywordResolvers;
    }
    
}
