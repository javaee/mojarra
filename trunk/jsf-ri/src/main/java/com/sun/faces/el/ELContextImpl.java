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


import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

/**
 * Concrete implementation of {@link javax.el.ELContext}.
 * ELContext's constructor is protected to control creation of ELContext
 * objects through their appropriate factory methods.  This version of
 * ELContext forces construction through FacesContextImpl.
 *
 */
public class ELContextImpl extends ELContext {
    
    private FunctionMapper functionMapper = new NoopFunctionMapper();
    private VariableMapper variableMapper;
    private ELResolver resolver;


    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new ELContext associated with the given ELResolver.
     * @param resolver the ELResolver to return from {@link #getELResolver()} 
     */
    public ELContextImpl(ELResolver resolver) {
        this.resolver = resolver;
    }


    // -------------------------------------------------- Methods from ELContext


    public FunctionMapper getFunctionMapper() {        
        return functionMapper;
    }

    public VariableMapper getVariableMapper() {
        if (variableMapper == null) {
            variableMapper = new VariableMapperImpl();
        }
        return variableMapper;
    }

    public ELResolver getELResolver() {
        return resolver;
    }


    // ---------------------------------------------------------- Public Methods


    public void setFunctionMapper(FunctionMapper functionMapper) {

        this.functionMapper = functionMapper;

    }


    // ----------------------------------------------------------- Inner Classes


    private static class VariableMapperImpl extends VariableMapper {

        private Map<String,ValueExpression> variables;

        public VariableMapperImpl() {

            //noinspection CollectionWithoutInitialCapacity
            variables = new HashMap<String,ValueExpression>();

        }

        public ValueExpression resolveVariable(String s) {
            return variables.get(s);
        }

        public ValueExpression setVariable(String s, ValueExpression valueExpression) {
            return (variables.put(s, valueExpression));
        }
    }


    private static class NoopFunctionMapper extends FunctionMapper {

        public Method resolveFunction(String s, String s1) {
            return null;
        }

    }

}
