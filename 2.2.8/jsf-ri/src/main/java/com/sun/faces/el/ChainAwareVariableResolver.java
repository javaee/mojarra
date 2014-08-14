/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.el;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import com.sun.faces.util.RequestStateManager;
import com.sun.faces.el.FacesCompositeELResolver.ELResolverChainType;

/**
 * <p> This special VariableResolver serves as the "original"
 * VariableResolver that is passed to the one-arg ctor for the
 * <b>first</b> custom VariableResolver that is encountered during
 * application configuration.  Subsequent VariableResolver instances get
 * passed the previous VariableResolver instance, per section 10.4.5 of
 * the spec. </p>
 *
 * <p>The "specialness" of this VariableResolver is found in its {@link
 * #resolveVariable} method, which delegates to the head of the
 * "correct" ELResolver chain based on the context in which this
 * expression is being evaluated.  If the expression being evaluated
 * originated in a JSP page, the <code>Application</code>'s
 * <code>ExpressionFactory</code> is used to create a
 * <code>ValueExpression</code>, which is then evaluated to resolve the
 * variable.  This will cause the ELResolver chain described in section
 * 5.6.1 of the spec to be used.  If the expression being evaluated
 * originated in a programmatic API call, the <code>Application</code>'s
 * <code>ELResolver</code> is used to resolve the variable.  This will
 * cause the ELResolver chain described in section 5.6.2 of the spec to
 * be used.</p>
 */

@SuppressWarnings("deprecation")
public class ChainAwareVariableResolver extends VariableResolver {

    public ChainAwareVariableResolver() {

    }


    //
    // Relationship Instance Variables
    // 

    /**
     * See the class javadocs.
     */
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {
        Object result = null;
        ELResolverChainType type = ELResolverChainType.Faces;
        Object valueObject = RequestStateManager.get(context,
                                                     RequestStateManager.EL_RESOLVER_CHAIN_TYPE_NAME);
        if (null != valueObject && 
            valueObject instanceof ELResolverChainType) {
            type = (ELResolverChainType) valueObject;
        }
        if (ELResolverChainType.JSP == type) {
            ValueExpression ve = context.getApplication().getExpressionFactory()
                  .createValueExpression(context.getELContext(),
                                         "#{" + name + "}", Object.class);
            result = ve.getValue(context.getELContext());
        } else if (ELResolverChainType.Faces == type) {
            ELResolver elr = context.getApplication().getELResolver();
            result = elr.getValue(context.getELContext(), null, name);
        }

        return result;
    }
}
