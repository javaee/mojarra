/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
