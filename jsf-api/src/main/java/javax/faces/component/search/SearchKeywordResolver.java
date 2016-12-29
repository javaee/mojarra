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
 * <div class="changed_added_2_3">A <strong>SearchKeywordResolver</strong> is responsible for
 * resolving a single command. A command (e.g. <code>@child(2)</code>) is a "instance" of a keyword (e.g. <code>@child</code>).
 * 
 * New {@link SearchKeywordResolver} can be registered via
 *   {@link javax.faces.application.Application#addSearchKeywordResolver(javax.faces.component.search.SearchKeywordResolver)}
 * or via faces-config
 * <code>
 * &lt;application&gt;
 *   &lt;search-keyword-resolver&gt;...&lt;/search-keyword-resolver&gt;
 * &lt;/application&gt;
 * </code>
 * </div>
 * 
 * @since 2.3
 */
public abstract class SearchKeywordResolver {

   /**
     * <p class="changed_added_2_3">Try to resolve one or multiple {@link UIComponent}s based on the command and calls
     * {@link SearchKeywordContext#invokeContextCallback(javax.faces.component.UIComponent)} for each resolved component.
     * </p>
     *
     * @param searchKeywordContext the {@code SearchKeywordContext}
     * @param previous the previous resolved component or the source component (if called for the first command in the chain)
     * @param command the command
     *
     * @since 2.3
     */
    public abstract void resolve(SearchKeywordContext searchKeywordContext, UIComponent previous, String command);

    /**
     * <p class="changed_added_2_3">Checks if the current instance of the {@link SearchKeywordResolver}
     * is responsible for resolving the command / keyword.</p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param command the command
     *
     * @return <code>true</code> if it's responsible for resolving this command
     *
     * @since 2.3
     */
    public abstract boolean matchKeyword(SearchExpressionContext searchExpressionContext, String command);

    /**
     * <p class="changed_added_2_3">A passthrough keyword is a keyword, that according to the context,
     * does not require to be resolved on the server, and can be passed "unresolved" to the client.</p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param command the command
     *
     * @return <code>true</code> if it's passthrough keyword.
     *
     * @since 2.3
     */
    public boolean isPassthrough(SearchExpressionContext searchExpressionContext, String command) {
        return false;
    }

    /**
     * <p class="changed_added_2_3">A leaf keyword is a keyword that does not allow to be combined with keywords
     * or id chains to the right.
     * For example: @none:@parent.</p>
     *
     * @param searchExpressionContext the {@link SearchExpressionContext}
     * @param command the command
     *
     * @return <code>true</code> if it's leave keyword.
     *
     * @since 2.3
     */
    public boolean isLeaf(SearchExpressionContext searchExpressionContext, String command) {
        return false;
    }
}
