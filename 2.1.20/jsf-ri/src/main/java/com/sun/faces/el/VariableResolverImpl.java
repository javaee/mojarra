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

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;
import javax.el.ELException;

import com.sun.faces.util.MessageUtils;

/**
 * <p>
 * Concrete implementation of <code>VariableResolver</code>.
 * </p>
 */
@SuppressWarnings("deprecation")
public class VariableResolverImpl extends VariableResolver {

    private VariableResolver delegate;

    // ------------------------------------------- Methods from VariableResolver

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }
        if (name == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "name");
            throw new NullPointerException(message);
        }

        Object result;

        if (delegate != null) {
            result = delegate.resolveVariable(context, name);
        } else {
            try {
                result = context.getApplication().getELResolver().getValue(context.getELContext(), null, name);
            } catch (ELException elex) {
                throw new EvaluationException(elex);
            }
        }
        return result;

    }


    // ---------------------------------------------------------- Public Methods


    public void setDelegate(VariableResolver delegate) {

        this.delegate = delegate;

    }

    public VariableResolver getDelegate() {
        return this.delegate;
    }
}
