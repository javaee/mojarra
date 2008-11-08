/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 */

package javax.faces.application;

import java.util.Iterator;
import java.util.ResourceBundle;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.webapp.pdl.PageDeclarationLanguage;

public abstract class ApplicationWrapper extends Application implements FacesWrapper<Application> {

    public abstract Application getWrapped();
    
    @Override
    public void addELContextListener(ELContextListener listener) {
        getWrapped().addELContextListener(listener);
    }

    @Override
    public void addELResolver(ELResolver resolver) {
        getWrapped().addELResolver(resolver);
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression, FacesContext context, String componentType) throws FacesException {
        return getWrapped().createComponent(componentExpression, context, componentType);
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression, FacesContext context, String componentType, String rendererType) {
        return getWrapped().createComponent(componentExpression, context, componentType, rendererType);
    }

    @Override
    public UIComponent createComponent(FacesContext context, String componentType, String rendererType) {
        return getWrapped().createComponent(context, componentType, rendererType);
    }

    @Override
    public UIComponent createComponent(FacesContext context, Resource componentResource) {
        return getWrapped().createComponent(context, componentResource);
    }
    
    /**
     *
     * @deprecated This has been replaced by {@link
     * #createComponent(javax.el.ValueExpression,javax.faces.context.FacesContext,java.lang.String)}.
     */
    @Override
    public UIComponent createComponent(ValueBinding componentBinding,
            FacesContext context,
            String componentType) {
        return getWrapped().createComponent(componentBinding, context, componentType);
    }
    
    /**
     * @deprecated This has been replaced by calling {@link
     * #getExpressionFactory} then {@link
     * ExpressionFactory#createMethodExpression}.
     */
    public MethodBinding createMethodBinding(String ref,
                                                      Class<?> params[])
        throws ReferenceSyntaxException {
        return getWrapped().createMethodBinding(ref, params);
    }    
    
    /**
     * <p>Call {@link #getExpressionFactory} then call {@link
     * ExpressionFactory#createValueExpression}, passing the argument
     * <code>ref</code>, <code>Object.class</code> for the expectedType,
     * and <code>null</code>, for the fnMapper.</p>
     *
     *
     * @param ref Value binding expression for which to return a
     *  {@link ValueBinding} instance
     *
     * @throws NullPointerException if <code>ref</code>
     *  is <code>null</code>
     * @throws ReferenceSyntaxException if the specified <code>ref</code>
     *  has invalid syntax
     *
     * @deprecated This has been replaced by calling {@link
     * #getExpressionFactory} then {@link
     * ExpressionFactory#createValueExpression}.
     */
    public abstract ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException;

    /**
     *
     * @deprecated This has been replaced by {@link #getELResolver}.  
     */
    public PropertyResolver getPropertyResolver() {
        return getWrapped().getPropertyResolver();
    }

    @Override
    public <T> T evaluateExpressionGet(FacesContext context, String expression, Class<? extends T> expectedType) throws ELException {
        return getWrapped().evaluateExpressionGet(context, expression, expectedType);
    }

    @Override
    public ELContextListener[] getELContextListeners() {
        return getWrapped().getELContextListeners();
    }

    @Override
    public ELResolver getELResolver() {
        return getWrapped().getELResolver();
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return getWrapped().getExpressionFactory();
    }

    @Override
    public ProjectStage getProjectStage() {
        return getWrapped().getProjectStage();
    }

    @Override
    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return getWrapped().getResourceBundle(ctx, name);
    }

    @Override
    public ResourceHandler getResourceHandler() {
        return getWrapped().getResourceHandler();
    }

    @Override
    public void publishEvent(Class<? extends SystemEvent> systemEventClass, Object source) {
        getWrapped().publishEvent(systemEventClass, source);
    }

    @Override
    public void publishEvent(Class<? extends SystemEvent> systemEventClass, Class<?> sourceBaseType, Object source) {
        getWrapped().publishEvent(systemEventClass, sourceBaseType, source);
    }

    @Override
    public void removeELContextListener(ELContextListener listener) {
        getWrapped().removeELContextListener(listener);
    }

    @Override
    public void setResourceHandler(ResourceHandler resourceHandler) {
        getWrapped().setResourceHandler(resourceHandler);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass, Class<?> sourceClass, SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, sourceClass, listener);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass, SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass, Class<?> sourceClass, SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, sourceClass, listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass, SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, listener);
    }

    
}
