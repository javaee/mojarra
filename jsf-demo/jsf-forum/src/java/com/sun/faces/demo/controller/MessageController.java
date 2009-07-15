/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.demo.controller;

import com.sun.faces.demo.model.Messages;
import com.sun.faces.demo.model.Threads;
import java.util.ArrayList;
import java.util.Collection;
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
@ManagedBean(name = "message")
@RequestScoped
public class MessageController {

    @PersistenceUnit
    private EntityManagerFactory emf;
    String threadId = null;
    Threads thread = null;
    Messages message = null;
    private String subject = null;
    private String text = null;

    public MessageController() {

        FacesContext context = FacesContext.getCurrentInstance();

        threadId = context.getExternalContext().getRequestParameterMap().get("threadId");

        if (threadId == null || !threadId.matches("[0-9]+")) {
            FacesMessage fmessage = new FacesMessage(
                    "Parameter threadId not set correctly, " + threadId);
            context.addMessage(null, fmessage);
            return;
        }

        message = new Messages();

    }

    private void initThread() {
        thread = message.getThreadid();

        if (thread == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            EntityManager em = emf.createEntityManager();

            try {
                Query query = em.createNamedQuery("Threads.findByThreadid");
                query.setParameter("threadid", Integer.valueOf(threadId));
                thread = (Threads) query.getSingleResult();
            } catch (Exception e) {
                FacesMessage fmessage = new FacesMessage((e.getLocalizedMessage()));
                context.addMessage(null, fmessage);
                thread = new Threads();
            } finally {
                em.close();
            }
        }
        message.setThreadid(thread);

    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return message.getSubject();
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        message.setSubject(subject);
    }

    /**
     * @return the text
     */
    public String getText() {
        return message.getText();
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        message.setText(text);
    }

    public void createMessage() {
        if (thread == null) {
            initThread();
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(message);
            Collection<Messages> cm = thread.getMessagesCollection();
            if (cm == null) {
                cm = new ArrayList();
            }
            cm.add(message);
            thread.setMessagesCollection(cm);
            em.merge(thread);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
