/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.demo.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author netdance
 */
@Entity
@Table(name = "THREADS", catalog = "", schema = "FORUM")
@NamedQueries({@NamedQuery(name = "Threads.findAll", query = "SELECT t FROM Threads t"), @NamedQuery(name = "Threads.findByThreadid", query = "SELECT t FROM Threads t WHERE t.threadid = :threadid"), @NamedQuery(name = "Threads.findByTitle", query = "SELECT t FROM Threads t WHERE t.title = :title")})
public class Threads implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "THREADID", nullable = false)
    private Integer threadid;
    @Basic(optional = false)
    @Column(name = "TITLE", nullable = false, length = 40)
    private String title;
    @JoinColumn(name = "TOPICID", referencedColumnName = "TOPICID", nullable = false)
    @ManyToOne(optional = false)
    private Topics topicid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "threadid")
    private Collection<Messages> messagesCollection;

    public Threads() {
    }

    public Threads(Integer threadid) {
        this.threadid = threadid;
    }

    public Threads(Integer threadid, String title) {
        this.threadid = threadid;
        this.title = title;
    }

    public Integer getThreadid() {
        return threadid;
    }

    public void setThreadid(Integer threadid) {
        this.threadid = threadid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topics getTopicid() {
        return topicid;
    }

    public void setTopicid(Topics topicid) {
        this.topicid = topicid;
    }

    public Collection<Messages> getMessagesCollection() {
        return messagesCollection;
    }

    public void setMessagesCollection(Collection<Messages> messagesCollection) {
        this.messagesCollection = messagesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (threadid != null ? threadid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Threads)) {
            return false;
        }
        Threads other = (Threads) object;
        if ((this.threadid == null && other.threadid != null) || (this.threadid != null && !this.threadid.equals(other.threadid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sun.faces.demo.model.Threads[threadid=" + threadid + "]";
    }

}
