/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.flow;


import com.sun.faces.facelets.util.ReflectionUtil;
import com.sun.faces.util.FacesLogger;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.Parameter;

public class MethodCallNodeImpl extends MethodCallNode implements Serializable {

    private static final long serialVersionUID = -5400138716176841428L;
    
    private final String id;
    
    private static final Logger LOGGER = FacesLogger.FLOW.getLogger();
    
    public MethodCallNodeImpl(String id) {
        this.id = id;
        _parameters = new CopyOnWriteArrayList<Parameter>();            
    }
    
    public MethodCallNodeImpl(FacesContext context, String id, 
            String methodExpressionString,
            String defaultOutcomeString,
            List<Parameter> parametersFromConfig) {
        this(id);
        if (null != parametersFromConfig) {
            _parameters.addAll(parametersFromConfig);
        }
        parameters = Collections.unmodifiableList(_parameters);
        
        ExpressionFactory ef = context.getApplication().getExpressionFactory();
        Class [] paramTypes = new Class[0];
        if (0 < parameters.size()) {
            paramTypes = new Class[parameters.size()];
            int i = 0;
            for (Parameter cur : parameters) {
                if (null != cur.getName()) {
                    try {
                        paramTypes[i] = ReflectionUtil.forName(cur.getName());
                    } catch (ClassNotFoundException cnfe) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, "parameter " + cur.getName() + 
                                    "incorrect type", cnfe);
                        }
                        paramTypes[i] = null;
                    }
                } else {
                    paramTypes[i] = String.class;
                }
                i++;
            }
        }
        ELContext elContext = context.getELContext();
        methodExpression = ef.createMethodExpression(elContext, 
                methodExpressionString, null, paramTypes);
        
        if (null != defaultOutcomeString) {
            outcome = ef.createValueExpression(elContext, defaultOutcomeString, 
                    Object.class);
        }
        
    }
    
    private MethodExpression methodExpression;
    
    private ValueExpression outcome;

    private List<Parameter> _parameters;
    private List<Parameter> parameters;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }
    
    public List<Parameter> _getParameters() {
        if (null == parameters) {
            parameters = Collections.unmodifiableList(_parameters);
        }
        return _parameters;
    }

    @Override
    public MethodExpression getMethodExpression() {
        return methodExpression;
    }
    
    public void setMethodExpression(MethodExpression methodExpression) {
        this.methodExpression = methodExpression;
    }
    
    @Override
    public ValueExpression getOutcome() {
        return outcome;
    }

    public void setOutcome(ValueExpression outcome) {
        this.outcome = outcome;
    }
    
    
}
