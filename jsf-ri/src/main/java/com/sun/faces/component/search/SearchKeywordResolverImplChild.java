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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.search.UntargetableComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.component.search.SearchKeywordResolver;

public class SearchKeywordResolverImplChild extends SearchKeywordResolver {

    private static final Pattern PATTERN = Pattern.compile("child\\((\\d+)\\)");
    
    @Override
    public void resolve(SearchKeywordContext searchKeywordContext, UIComponent previous, String command) {

        Matcher matcher = PATTERN.matcher(command);

        if (matcher.matches()) {

            int childNumber = Integer.parseInt(matcher.group(1));
            if (childNumber + 1 > previous.getChildCount()) {
                throw new FacesException("Component with clientId \""
                        + previous.getClientId(searchKeywordContext.getSearchExpressionContext().getFacesContext()) 
                        + "\" has fewer children as \"" + 
                          childNumber + "\". Expression: \"" + command + "\"");
            }

            List<UIComponent> list = previous.getChildren();
            int count = 0;
            for (int i = 0; i < previous.getChildCount(); i++) {
                if (!(list.get(i) instanceof UntargetableComponent)) {
                    count++;
                }
                if (count == childNumber + 1) {
                    searchKeywordContext.invokeContextCallback(previous.getChildren().get(childNumber));
                    break;
                }
            }

            if (count < childNumber) {
                throw new FacesException("Component with clientId \""
                        + previous.getClientId(searchKeywordContext.getSearchExpressionContext().getFacesContext()) 
                        + "\" has fewer children as \"" + 
                          childNumber + "\". Expression: \"" + command + "\"");
            }
        } else {
            throw new FacesException(
                    "Expression does not match following pattern @child(n). Expression: \"" + command + "\"");
        }
    }

    @Override
    public boolean matchKeyword(SearchExpressionContext searchExpressionContext, String command) {
        
        if (command.startsWith("child")) {
            try {
                Matcher matcher = PATTERN.matcher(command);
                return matcher.matches();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
}
