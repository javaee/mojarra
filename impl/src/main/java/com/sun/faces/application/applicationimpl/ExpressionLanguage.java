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

package com.sun.faces.application.applicationimpl;

import static com.sun.faces.util.MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID;
import static com.sun.faces.util.MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID;
import static com.sun.faces.util.MessageUtils.getExceptionMessageString;
import static com.sun.faces.util.Util.canSetAppArtifact;
import static com.sun.faces.util.Util.getCdiBeanManager;
import static com.sun.faces.util.Util.notNull;
import static java.util.logging.Level.FINE;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.MethodBindingMethodExpressionAdapter;
import com.sun.faces.application.ValueBindingValueExpressionAdapter;
import com.sun.faces.el.ELUtils;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

public class ExpressionLanguage {
    
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    
    private static final ELContextListener[] EMPTY_EL_CTX_LIST_ARRAY = {};
    
    private final ApplicationAssociate associate;
    
    private volatile PropertyResolverImpl propertyResolver;
    private volatile VariableResolverImpl variableResolver;
    
    private List<ELContextListener> elContextListeners;
    private CompositeELResolver elResolvers;
    private FacesCompositeELResolver compositeELResolver;

    private Version version = new Version();
    
    public ExpressionLanguage(ApplicationAssociate applicationAssociate) {
        this.associate = applicationAssociate;
        
        propertyResolver = new PropertyResolverImpl();
        variableResolver = new VariableResolverImpl();
        elContextListeners = new CopyOnWriteArrayList<>();
        elResolvers = new CompositeELResolver();
    }
    
    /**
     * @see javax.faces.application.Application#addELContextListener(javax.el.ELContextListener)
     */
    public void addELContextListener(ELContextListener listener) {
        if (listener != null) {
            elContextListeners.add(listener);
        }
    }

    /**
     * @see javax.faces.application.Application#removeELContextListener(javax.el.ELContextListener)
     */
    public void removeELContextListener(ELContextListener listener) {
        if (listener != null) {
            elContextListeners.remove(listener);
        }
    }

    /**
     * @see javax.faces.application.Application#getELContextListeners()
     */
    public ELContextListener[] getELContextListeners() {
        if (!elContextListeners.isEmpty()) {
            return elContextListeners.toArray(new ELContextListener[elContextListeners.size()]);
        }
            
        return EMPTY_EL_CTX_LIST_ARRAY;
    }
    
    /**
     * @see javax.faces.application.Application#getELResolver()
     */
    public ELResolver getELResolver() {

        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }

        return compositeELResolver;
    }

    /**
     * @see javax.faces.application.Application#addELResolver(javax.el.ELResolver)
     */
    public void addELResolver(ELResolver resolver) {

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(getExceptionMessageString(ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ELResolver"));
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (version.isJsf23()) {

            BeanManager cdiBeanManager = getCdiBeanManager(facesContext);

            if (cdiBeanManager != null && !resolver.equals(cdiBeanManager.getELResolver())) {
                elResolvers.add(resolver);
            }
        } else {
            elResolvers.add(resolver);
        }
    }
    
    /**
     * @see javax.faces.application.Application#getExpressionFactory()
     */
    public ExpressionFactory getExpressionFactory() {
        return associate.getExpressionFactory();
    }
    
    /**
     * @see javax.faces.application.Application#evaluateExpressionGet(javax.faces.context.FacesContext,
     *      String, Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluateExpressionGet(FacesContext context, String expression, Class<? extends T> expectedType) throws ELException {
        return (T) getExpressionFactory().createValueExpression(context.getELContext(), expression, expectedType).getValue(context.getELContext());
    }
    
    public CompositeELResolver getApplicationELResolvers() {
        return elResolvers;
    }
    
    public FacesCompositeELResolver getCompositeELResolver() {
        return compositeELResolver;
    }

    public void setCompositeELResolver(FacesCompositeELResolver compositeELResolver) {
        this.compositeELResolver = compositeELResolver;
    }
    
    
    
    
    
    private void performOneTimeELInitialization() {
        if (compositeELResolver != null) {
            throw new IllegalStateException("Class invariant invalidated: " + "The Application instance's ELResolver is not null " + "and it should be.");
        }
        associate.initializeELResolverChains();
    }
    
    
    
    
    
    
    
    
    /**
     * @see javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)
     */
    @Deprecated
    public PropertyResolver getPropertyResolver() {
        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }

        return propertyResolver;
    }
    
    /**
     * @see javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)
     */
    @Deprecated
    public void setPropertyResolver(PropertyResolver resolver) {

        // Throw Illegal State Exception if a PropertyResolver is set after
        // a request has been processed.
        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(getExceptionMessageString(ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "PropertyResolver"));
        }

        if (resolver == null) {
            String message = getExceptionMessageString(NULL_PARAMETERS_ERROR_MESSAGE_ID, "resolver");
            throw new NullPointerException(message);
        }

        propertyResolver.setDelegate(ELUtils.getDelegatePR(associate, true));
        associate.setLegacyPropertyResolver(resolver);
        propertyResolver = new PropertyResolverImpl();

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("set PropertyResolver Instance to ''{0}''", resolver.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#createMethodBinding(String, Class[])
     */
    @Deprecated
    public MethodBinding createMethodBinding(String ref, Class<?> params[]) {

        Util.notNull("ref", ref);

        if (!(ref.startsWith("#{") && ref.endsWith("}"))) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(MessageFormat.format("Expression ''{0}'' does not follow the syntax #{...}", ref));
            }
            throw new ReferenceSyntaxException(ref);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression result;
        try {
            // return a MethodBinding that wraps a MethodExpression.
            if (null == params) {
                params = RIConstants.EMPTY_CLASS_ARGS;
            }
            result = getExpressionFactory().createMethodExpression(context.getELContext(), ref, null, params);
        } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
        }
        return new MethodBindingMethodExpressionAdapter(result);

    }
    
    /**
     * @see javax.faces.application.Application#createValueBinding(String)
     */
    @Deprecated
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {

        notNull("ref", ref);
        ValueExpression result;
        FacesContext context = FacesContext.getCurrentInstance();
        // return a ValueBinding that wraps a ValueExpression.
        try {
            result = getExpressionFactory().createValueExpression(context.getELContext(), ref, Object.class);
        } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
        }
        return new ValueBindingValueExpressionAdapter(result);

    }
    
    /**
     * @see javax.faces.application.Application#getVariableResolver()
     */
    @Deprecated
    public VariableResolver getVariableResolver() {
        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }

        return variableResolver;
    }
    
    /**
     * @see javax.faces.application.Application#setVariableResolver(javax.faces.el.VariableResolver)
     */
    @Deprecated
    public void setVariableResolver(VariableResolver resolver) {
        notNull("variableResolver", resolver);
        canSetAppArtifact(associate, "VariableResolver");

        variableResolver.setDelegate(ELUtils.getDelegateVR(associate, true));
        associate.setLegacyVariableResolver(resolver);

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("set VariableResolver Instance to ''{0}''", variableResolver.getClass().getName()));
        }
    }




}
