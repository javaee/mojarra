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

package jsf2.demo.scrum.web.controller;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public abstract class AbstractManager {

    @PersistenceUnit
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction userTransaction;

    protected <T> T doInTransaction(PersistenceAction<T> action) throws ManagerException {
        EntityManager em = emf.createEntityManager();
        try {
            userTransaction.begin();
            T result = action.execute(em);
            userTransaction.commit();
            return result;
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception ex) {
                Logger.getLogger(AbstractManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ManagerException(e);
        } finally {
            em.close();
        }

    }

    protected void doInTransaction(PersistenceActionWithoutResult action) throws ManagerException {
        EntityManager em = emf.createEntityManager();
        try {
            userTransaction.begin();
            action.execute(em);
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception ex) {
                Logger.getLogger(AbstractManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ManagerException(e);
        } finally {
            em.close();
        }
    }

    protected static interface PersistenceAction<T> {

        T execute(EntityManager em);
    }

    protected static interface PersistenceActionWithoutResult {

        void execute(EntityManager em);
    }

    protected void addMessage(String message) {
        addMessage(null, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String componentId, String message) {
        addMessage(componentId, message, FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String message, Severity severity) {
        addMessage(null, message, severity);
    }

    protected void addMessage(String componentId, String message, Severity severity) {
        FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(severity, message, message));
    }

    protected String getMessageForKey(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle rb = ctx.getApplication().getResourceBundle(ctx, "i18n");
        return rb.getString(key);
    }

    protected FacesMessage getFacesMessageForKey(String key) {
        return new FacesMessage(getMessageForKey(key));
    }

    protected Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class for logger is required.");
        }
        return Logger.getLogger(clazz.getName());
    }

    protected void publishEvent(Class<? extends SystemEvent> eventClass, Object source) {
        if (source != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getApplication().publishEvent(ctx, eventClass, source);
        }
    }

    protected void subscribeToEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(eventClass, listener);
    }

    protected void unsubscribeFromEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().unsubscribeFromEvent(eventClass, listener);
    }
}
