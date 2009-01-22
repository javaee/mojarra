/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jstl.core;

import com.sun.faces.facelets.tag.TagHandlerImpl;
import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.TagException;

/**
 * Simplified implementation of c:set
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class SetHandler extends JstlTagHandler {

    private final TagAttribute var;
    
    private final TagAttribute value;
    
    private final TagAttribute target;
    
    private final TagAttribute property;
    
    private final TagAttribute scope;
    
    public SetHandler(TagConfig config) {
        super(config);
        this.value = this.getAttribute("value");
        this.var = this.getAttribute("var");
        this.target = this.getAttribute("target");
        this.property = this.getAttribute("property");
        this.scope = this.getAttribute("scope");
        
    }

    public void deferredApply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        
        // Apply precedence algorithm for attributes.  The JstlCoreTLV doesn't
        // seem to enforce much in the way of this, so I edburns needs to check
        // with an authority on the matter, probabyl Kin-Man Chung
        
        // If var is set, value must be set, otherwise an error is thrown
        if (null != this.var && 
            (null == this.value || 0 == this.value.getValue().length())) {
            throw new TagException(tag, "var set with null or empty value");
        }
        ValueExpression veObj = this.value.getValueExpression(ctx, Object.class);
        ValueExpression lhs;
        String expr;
        
        // Otherwise, if var is set, ignore the other attributes
        if (null != this.var) {
            String scopeStr, varStr = this.var.getValue(ctx);
            
            // If scope is set, check for validity
            if (null != this.scope) {
                if (0 == this.scope.getValue().length()) {
                    throw new TagException(tag, "zero length scope attribute set");
                }
                
                if (this.scope.isLiteral()) {
                    scopeStr = this.scope.getValue();
                } else {
                    scopeStr = this.scope.getValue(ctx);
                }
                if (scopeStr.equals("page")) {
                    throw new TagException(tag, "page scope does not exist in JSF, consider using view scope instead.");
                }
                if (scopeStr.equals("request") || scopeStr.equals("session") ||
                        scopeStr.equals("application") || scopeStr.equals("view")) {
                    scopeStr = scopeStr + "Scope";
                }
                // otherwise, assume it's a valid scope.  With custom scopes,
                // it may be.
                // Conjure up an expression
                expr = "#{" + scopeStr +"." + varStr + "}";
                lhs = ctx.getExpressionFactory().
                        createValueExpression(ctx, expr, Object.class);
                lhs.setValue(ctx, veObj.getValue(ctx));
            }
            else {
                ctx.getVariableMapper().setVariable(varStr, veObj);
            }
        } else  {
            assert(null == this.var);
            // Otherwise, target, property and value must be set
            if ((null == this.target || null == this.target.getValue() ||
                 this.target.getValue().length() <= 0) ||
                (null == this.property || null == this.property.getValue() ||
                 this.property.getValue().length() <= 0) ||
                (null == this.value || null == this.value.getValue() ||
                 this.value.getValue().length() <= 0)) {
                throw new TagException(tag, "when using this tag either one of var and value, or (target, property, value) must be set.");
            }
            // Ensure that target is an expression
            if (this.target.isLiteral()) {
                throw new TagException(tag, "value of target attribute must be an expression");
            }
            // Get the value of property
            String propertyStr = null;
            if (this.property.isLiteral()) {
                propertyStr = this.property.getValue();
            } else {
                propertyStr = this.property.getValue(ctx);
            }
            ValueExpression targetVe = this.target.getValueExpression(ctx, Object.class);
            Object targetValue = targetVe.getValue(ctx);
            ctx.getFacesContext().getELContext().getELResolver().setValue(ctx, 
                    targetValue, propertyStr, veObj.getValue(ctx));
            
        }
    }
}
