/*
 * $Id: ChainAwareVariableResolver.java,v 1.3 2006/08/29 06:12:59 tony_robertson Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.el;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import com.sun.faces.RIConstants;
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
        ELResolverChainType type = null;
        Object valueObject = context.getExternalContext().getRequestMap().get(RIConstants.EL_RESOLVER_CHAIN_TYPE_NAME);
        if (null != valueObject && 
            valueObject instanceof ELResolverChainType) {
            type = (ELResolverChainType) valueObject;
        }
        if (ELResolverChainType.JSP == type) {
            ValueExpression ve = null;
            ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(),
                    "#{" + name + "}", Object.class);
            result = ve.getValue(context.getELContext());
        }
        else if (ELResolverChainType.Faces == type) {
            ELResolver elr = context.getApplication().getELResolver();
            result = elr.getValue(context.getELContext(), null, name);
        }
        else {
            assert(false);
        }

        return result;
    }
}
