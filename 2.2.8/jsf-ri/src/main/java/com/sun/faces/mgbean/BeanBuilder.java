/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.mgbean;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.el.ELUtils;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Abstract builder for creating and populating JSF managed beans.</p>
 */
public abstract class BeanBuilder {

    private static Logger LOGGER = FacesLogger.MANAGEDBEAN.getLogger();

    private List<String> messages;
    private List<String> references;
    private boolean isInjectible;
    private boolean baked;

    private Class<?> beanClass;
    protected final ManagedBeanInfo beanInfo;



    // ------------------------------------------------------------ Constructors


    /**     
     * @param beanInfo the managed bean metadata
     */
    public BeanBuilder(ManagedBeanInfo beanInfo) {

        this.beanInfo = beanInfo;
        
    }


    // ---------------------------------------------------------- Public Methods


    public Object build(InjectionProvider injectionProvider,
                        FacesContext context) {

        Object bean = newBeanInstance();
        injectResources(bean, injectionProvider);
        buildBean(bean, context);
        invokePostConstruct(bean, injectionProvider);
        return bean;

    }


    public void destroy(InjectionProvider injectionProvider,
                          Object bean) {

        if (isInjectible) {
            try {
                injectionProvider.invokePreDestroy(bean);
            } catch (InjectionProviderException ipe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, ipe.getMessage(), ipe);
                }
            }
        }

    }


    public boolean hasMessages() {

        return (messages != null && !messages.isEmpty());

    }


    public List<String> getMessages() {

        return messages;

    }

    public String getScope() {

        return beanInfo.getScope();

    }


    public boolean isBaked() {

        return baked;

    }


    public Map<String,String> getDescriptions() {

        return beanInfo.getDescriptions();
        
    }


    public Class<?> getBeanClass() {

        return loadBeanClass();

    }

    public ManagedBeanInfo getManagedBeanInfo() {

        return beanInfo;
        
    }

    
    // ------------------------------------------------------- Protected Methods


    protected abstract void buildBean(Object bean, FacesContext context);



    protected void baked() {
        baked = true;
    }

    protected Object newBeanInstance() {

        try {
            return loadBeanClass().newInstance();
        } catch (Exception e) {
            String message = MessageUtils.getExceptionMessageString(
                 MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,
                 beanInfo.getClassName());
            throw new ManagedBeanCreationException(message, e);
        }

    }
    


    protected void injectResources(Object bean,
                                   InjectionProvider injectionProvider) {

        if (isInjectible) {
            try {
                injectionProvider.inject(bean);
            } catch (InjectionProviderException ipe) {
                String message =
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.MANAGED_BEAN_INJECTION_ERROR_ID,
                          beanInfo.getName());
                throw new ManagedBeanCreationException(message, ipe);
            }
        }

    }


    protected void invokePostConstruct(Object bean,
                                       InjectionProvider injectionProvider) {

        if (isInjectible) {
            try {
                injectionProvider.invokePostConstruct(bean);
            } catch (InjectionProviderException ipe) {
                String message =
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.MANAGED_BEAN_INJECTION_ERROR_ID,
                          beanInfo.getName());
                throw new ManagedBeanCreationException(message, ipe);
            }
        }

    }


    protected Class loadClass(String className)  {
        Class valueType = String.class;
        if (null != className && 0 < className.length()) {
            if (className.equals(Boolean.TYPE.getName())) {
                valueType = Boolean.TYPE;
            } else if (className.equals(Byte.TYPE.getName())) {
                valueType = Byte.TYPE;
            } else if (className.equals(Double.TYPE.getName())) {
                valueType = Double.TYPE;
            } else if (className.equals(Float.TYPE.getName())) {
                valueType = Float.TYPE;
            } else if (className.equals(Integer.TYPE.getName())) {
                valueType = Integer.TYPE;
            } else if (className.equals(Character.TYPE.getName())) {
                valueType = Character.TYPE;
            } else if (className.equals(Short.TYPE.getName())) {
                valueType = Short.TYPE;
            } else if (className.equals(Long.TYPE.getName())) {
                valueType = Long.TYPE;
            } else {
                try {
                    valueType = Util.loadClass(className, this);
                } catch (ClassNotFoundException cnfe) {
                    String message =
                         MessageUtils.getExceptionMessageString(
                              MessageUtils.MANAGED_BEAN_CLASS_NOT_FOUND_ERROR_ID,
                              className,
                              beanInfo.getName());
                    throw new ManagedBeanPreProcessingException(message);
                } catch (NoClassDefFoundError ncdfe) {
                    String message =
                         MessageUtils.getExceptionMessageString(
                              MessageUtils.MANAGED_BEAN_CLASS_DEPENDENCY_NOT_FOUND_ERROR_ID,
                              className,
                              beanInfo.getName(),
                              ncdfe.getMessage());
                    throw new ManagedBeanPreProcessingException(message);
                }
            }
        }
        return valueType;
    }


    protected Map<Expression,Expression> getBakedMap(String keyClass,
                                                     String valueClass,
                                                     Map<String,String> mapEntries) {

        if (mapEntries == null || mapEntries.isEmpty()) {
            return new LinkedHashMap<Expression,Expression>(4, 1.0f);
        }
        Class<?> keyClazz = loadClass(keyClass);
        Class<?> valueClazz = loadClass(valueClass);
        Map<Expression,Expression> target = new
             LinkedHashMap<Expression,Expression>(mapEntries.size(), 1.0f);
        for (Map.Entry<String,String> m : mapEntries.entrySet()) {
            String sk = m.getKey();
            String sv = m.getValue();

            target.put(new Expression(sk, keyClazz),
                           (!sv.equals(ManagedBeanInfo.NULL_VALUE))
                           ? new Expression(sv, valueClazz)
                           : null);
        }

        return target;
        
    }


    protected List<Expression> getBakedList(String valueClass,
                                            List<String> entries) {

        Class<?> valueClazz = loadClass(valueClass);

        //noinspection StringBufferWithoutInitialCapacity
        List<Expression> target = new ArrayList<Expression>(entries.size());
        for (String item : entries) {
            target.add((!ManagedBeanInfo.NULL_VALUE.equals(item))
                       ? new Expression(item, valueClazz)
                       : null);
        }

        return target;

    }


    protected void initMap(Map<Expression,Expression> source,
                           Map target,
                           FacesContext context) {

        for (Map.Entry<Expression,Expression> entry
               : source.entrySet()) {
            Expression k = entry.getKey();
            Expression v = entry.getValue();
            //noinspection unchecked
            target.put(k.evaluate(context.getELContext()),
                  (v != null) ? v.evaluate(context.getELContext()) : null);
        }
        
    }


    protected void initList(List<Expression> source,
                            List target,
                            FacesContext context) {

        for (int i = 0, size = source.size(); i < size; i++) {
            Expression value = source.get(i);
            //noinspection unchecked
            target.add((value != null)
                       ? value.evaluate(context.getELContext())
                       : null);
        }

    }


    // ------------------------------------------------- Package Private Methods


    void queueMessage(String message) {
        if (messages == null) {
            messages = new ArrayList<String>(4);
        }
        messages.add(message);
    }


    void queueMessages(List<String> messages) {
        if (this.messages == null) {
            this.messages = messages;
        } else {
            this.messages.addAll(messages);
        }
    }

    /**
     * Performs sanity checking of the <code>ManagedBeanInfo</code>
     * instance provided when the <code>BeanBuilder</code> instance
     * was created.  If any issues are found, queue messages which will
     * be logged when first baked and exposed as exceptions at runtime
     * per the spec.
     */
    void bake() {

        loadBeanClass();
        validateScope();

    }


    List<String> getReferences() {

        return references;

    }


    // --------------------------------------------------------- Private Methods


    private Class<?> loadBeanClass() {
        if (beanClass == null) {
           String className = beanInfo.getClassName();
            Class<?> clazz = loadClass(className);
            ApplicationAssociate associate =
                  ApplicationAssociate.getCurrentInstance();

            if (!associate.isDevModeEnabled()) {
                beanClass = clazz;
            }

            // validate the bean class is public and has a public
            // no-arg ctor
            int classModifiers = clazz.getModifiers();
            if (!Modifier.isPublic(classModifiers)) {
                String message =
                      MessageUtils.getExceptionMessageString(
                            MessageUtils.MANAGED_BEAN_CLASS_IS_NOT_PUBLIC_ERROR_ID,
                            className,
                            beanInfo.getName());
                queueMessage(message);
            }
            if (Modifier.isInterface(classModifiers)
                || Modifier.isAbstract(classModifiers)) {
                String message =
                      MessageUtils.getExceptionMessageString(
                            MessageUtils.MANAGED_BEAN_CLASS_IS_ABSTRACT_ERROR_ID,
                            className,
                            beanInfo.getName());
                queueMessage(message);
            }

            try {
                Constructor ctor =
                      clazz.getConstructor(RIConstants.EMPTY_CLASS_ARGS);
                if (!Modifier.isPublic(ctor.getModifiers())) {
                    String message =
                          MessageUtils.getExceptionMessageString(
                                MessageUtils.MANAGED_BEAN_CLASS_NO_PUBLIC_NOARG_CTOR_ERROR_ID,
                                className,
                                beanInfo.getName());
                    queueMessage(message);
                }
            } catch (NoSuchMethodException nsme) {
                String message =
                      MessageUtils.getExceptionMessageString(
                            MessageUtils.MANAGED_BEAN_CLASS_NO_PUBLIC_NOARG_CTOR_ERROR_ID,
                            className,
                            beanInfo.getName());
                queueMessage(message);
            }

            if (!hasMessages()) {
                // class is ok, scan for annotations
                this.isInjectible = Util.classHasAnnotations(clazz);
            }
            return clazz;
        }
        return beanClass;
    }


    private void validateScope() {

        String scope = beanInfo.getScope();
        if (!ELUtils.isScopeValid(scope)) {
            // custom scope - make sure it's valid
            if (!SharedUtils.isExpression(scope)) {
                String message = MessageUtils.getExceptionMessageString(
                         MessageUtils.MANAGED_BEAN_INVALID_SCOPE_ERROR_ID,
                         beanInfo.getName());
                throw new ManagedBeanPreProcessingException(message);
            }
        }

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * This is a holder class for ValueExpressions.  It will try to perform
     * static lifespan checking of the expression per the specification.  If
     * it is unable to determine the scope of the expression, validation will
     * be deferred to runtime.
     */
    protected class Expression {

        private String expressionString;
        private Class<?> expectedType;
        private ValueExpression ve;
        private boolean validateLifespanRuntime = false;
        private String[] segment = new String[1];

        // -------------------------------------------------------- Constructors


        public Expression(String expressionString,
                          Class<?> expectedType) {

            this.expressionString = expressionString;
            this.expectedType = expectedType;

            if (SharedUtils.isExpression(this.expressionString)) {
                List<String> expressions = ELUtils.getExpressionsFromString(this.expressionString);
                if (!expressions.isEmpty()) {
                    for (String expression : expressions) {
                        ELUtils.getScope(expression, segment);
                        if (segment[0] != null) {
                            if (references == null) {
                                references = new ArrayList<String>(4);
                            }
                            if (!references.contains(segment[0])) {
                                references.add(segment[0]);
                            }
                        }
                        segment[0] = null;
                    }
                }
                if (!SharedUtils.isExpression(beanInfo.getScope())) {
                    ELUtils.Scope expressionScope = ELUtils
                          .getNarrowestScopeFromExpression(this.expressionString);
                    if (expressionScope != null) {
                        // expression scope isn't null which means we have enough
                        // information to statically validate.
                        validateLifespan(expressionScope,
                                         validateLifespanRuntime);
                    } else {
                        validateLifespanRuntime = true;
                    }
                }
            } else {
                if (this.expressionString != null) {
                    this.expressionString =
                          "#{\""
                          + this.expressionString.replaceAll("[\\\\\"]",
                                                             "\\\\$0")
                          + "\"}";
                }
            }
        }


        // ------------------------------------------------------ Public Methods


        public Object evaluate(ELContext context) {
            if (this.expressionString == null) {
                return null;
            }
            if (validateLifespanRuntime) {
                ELUtils.Scope expScope =
                     ELUtils.getScope(this.expressionString, segment);
                validateLifespan(expScope, true);
            }
            if (ve == null) {
                ve = ((expectedType.isPrimitive())
                      ? ELUtils.createValueExpression(expressionString, expectedType)
                      : ELUtils.createValueExpression(expressionString, Object.class));
            }
            if (expectedType.isPrimitive()) {
                return ve.getValue(context);
            } else {
                Object tmpval = ve.getValue(context);
                return ((tmpval != null) ? ELUtils.coerce(tmpval, expectedType) : null);
            }
        }


        // ----------------------------------------------------- Private Methods


        private void validateLifespan(ELUtils.Scope expressionScope, boolean runtime) {
            if (ELUtils.isScopeValid(beanInfo.getScope())) {
                if (!ELUtils.hasValidLifespan(expressionScope,
                                              ELUtils.getScope(beanInfo.getScope()))) {
                    String message = MessageUtils.getExceptionMessageString(
                          MessageUtils.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID,
                          this.expressionString,
                          expressionScope,
                          beanInfo.getName(),
                          beanInfo.getScope());
                    if (runtime) {
                        throw new ManagedBeanCreationException(message);
                    } else {
                        queueMessage(message);
                    }
                }
            }

        }

    } // END Expression
}
