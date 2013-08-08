/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.faces.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The CDI context for @ConversationScoped.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CdiConversationScopeContext implements Context {
    /**
     * Stores the bean manager.
     */
    private BeanManager beanManager;
    
    /**
     * Constructor.
     */
    public CdiConversationScopeContext() {
    }

    /**
     * Get the scope we work for.
     * 
     * @return ConversationScoped.class
     */
    public Class<? extends Annotation> getScope() {
        return ConversationScoped.class;
    }

    /**
     * Get the instance for the given contextual and creationalContext.
     * 
     * @param <T> the type.
     * @param contextual the contextual.
     * @param creationalContext the creational context.
     * @return the instance.
     */
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        T result;
        
        if (beanManager == null) {
            beanManager = getBeanManager();
        }
        
        Set<Bean<?>> beans = beanManager.getBeans("javax.enterprise.context.conversation");
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext beanContext = beanManager.createCreationalContext(bean);
        CdiConversation conversation = (CdiConversation) bean.create(beanContext);
        result = (T) conversation.getConversation().createBean(contextual, creationalContext);
        return result;
    }

    /**
     * Get the instance for the given contextual.
     * 
     * @param <T> the type.
     * @param contextual the contextual.
     * @return the instance.
     */
    public <T> T get(Contextual<T> contextual) {
        T result;
        
        if (beanManager == null) {
            beanManager = getBeanManager();
        }

        Set<Bean<?>> beans = beanManager.getBeans("javax.enterprise.context.conversation");
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext beanContext = beanManager.createCreationalContext(bean);
        CdiConversation conversation = (CdiConversation) bean.create(beanContext);
        result = (T) conversation.getConversation().getBean(contextual);
        return result;
    }

    /**
     * Is the scope active.
     * 
     * @return true.
     */
    public boolean isActive() {
        return true;
    }

    /**
     * Get the bean manager.
     * 
     * @return the bean manager.
     */
    private BeanManager getBeanManager() {
        BeanManager result = null;

        try {
            InitialContext initialContext = new InitialContext();
            result = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException ne) {
        }
        if (result == null) {
            try {
                InitialContext initialContext = new InitialContext();
                result = (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
            } catch (NamingException ne) {
            }
        }
        return result;
    }
}
