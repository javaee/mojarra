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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author netdance
 */
@Entity
@Table(name = "TOPICS", catalog = "", schema = "FORUM")
@NamedQueries({@NamedQuery(name = "Topics.findAll", query = "SELECT t FROM Topics t"), @NamedQuery(name = "Topics.findByTopicid", query = "SELECT t FROM Topics t WHERE t.topicid = :topicid"), @NamedQuery(name = "Topics.findByTitle", query = "SELECT t FROM Topics t WHERE t.title = :title"), @NamedQuery(name = "Topics.findBySubject", query = "SELECT t FROM Topics t WHERE t.subject = :subject")})
public class Topics implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TOPICID", nullable = false)
    private Integer topicid;
    @Basic(optional = false)
    @Column(name = "TITLE", nullable = false, length = 40)
    private String title;
    @Column(name = "SUBJECT", length = 160)
    private String subject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topicid")
    private Collection<Threads> threadsCollection;

    public Topics() {
    }

    public Topics(Integer topicid) {
        this.topicid = topicid;
    }

    public Topics(Integer topicid, String title) {
        this.topicid = topicid;
        this.title = title;
    }

    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Collection<Threads> getThreadsCollection() {
        return threadsCollection;
    }

    public void setThreadsCollection(Collection<Threads> threadsCollection) {
        this.threadsCollection = threadsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (topicid != null ? topicid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Topics)) {
            return false;
        }
        Topics other = (Topics) object;
        if ((this.topicid == null && other.topicid != null) || (this.topicid != null && !this.topicid.equals(other.topicid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sun.faces.demo.model.Topics[topicid=" + topicid + "]";
    }

}
