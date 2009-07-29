/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.demo.controller;

import com.sun.faces.demo.model.Topics;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

/**
 *
 * @author netdance
 */
@ManagedBean(name="forum")
@RequestScoped
public class ForumController {

   @PersistenceUnit private EntityManagerFactory emf;

    private Collection<Topics> topics;
    private static String title = "JSF Forum";

    public Collection<Topics> getTopics() {
        EntityManager em = emf.createEntityManager();

        try {
            //create an instance of the NamedQuery defined in the Inventory class.
            Query query = em.createNamedQuery("Topics.findAll");
            return query.getResultList();
        }finally{
            em.close();
        }
    }

    protected void addTopic(Topics topic) {
        topics.add(topic);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
