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

package com.sun.faces.demo.controller;

import com.sun.faces.demo.model.Threads;
import com.sun.faces.demo.model.Topics;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

/**
 *
 * @author netdance
 */
@ManagedBean(name = "topic")
@RequestScoped
public class TopicController implements Serializable {

    @PersistenceUnit
    private EntityManagerFactory emf;
    
    private Topics topic = null;
    private Integer topicId = null;


    public TopicController() {
        FacesContext context = FacesContext.getCurrentInstance();

        String id = context.getExternalContext()
                .getRequestParameterMap().get("topicId");

        if (id == null) {
            topic = new Topics();
            return;
        }
        try {
            topicId = Integer.valueOf(id);
        } catch (Exception e) {
            topic = new Topics();
            return;
        }
        if (topicId == null || topicId.intValue() <= 0) {
            topic = new Topics();
            return;
        }

    }


    @PostConstruct
    private void init() {
        if (topic != null) return;

        FacesContext context = FacesContext.getCurrentInstance();

        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createNamedQuery("Topics.findByTopicid");
            query.setParameter("topicid", topicId);
            topic =  (Topics)query.getSingleResult();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage((e.getLocalizedMessage()));
            context.addMessage(null, message);
            topic = new Topics();
        }finally{
            em.close();
        }
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return topic.getTitle();
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        topic.setTitle(title);
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return topic.getSubject();
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        topic.setSubject(subject);
    }

    /**
     * Get the TopicID for this topic
     * @return the topic ID
     */
    public int getId() {
        if (topic.getTopicid() == null) {
            return 0;
        } else {
            return topic.getTopicid();
        }
    }

    public Collection<Threads> getThreads() {
        return topic.getThreadsCollection();
    }

    public void createTopic() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(topic);
            em.flush();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
