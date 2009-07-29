/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.demo.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author netdance
 */
@Entity
@Table(name = "MESSAGES", catalog = "", schema = "FORUM")
@NamedQueries({@NamedQuery(name = "Messages.findAll", query = "SELECT m FROM Messages m"), @NamedQuery(name = "Messages.findByMessageid", query = "SELECT m FROM Messages m WHERE m.messageid = :messageid"), @NamedQuery(name = "Messages.findBySubject", query = "SELECT m FROM Messages m WHERE m.subject = :subject"), @NamedQuery(name = "Messages.findByText", query = "SELECT m FROM Messages m WHERE m.text = :text"), @NamedQuery(name = "Messages.findByCreationTime", query = "SELECT m FROM Messages m WHERE m.creationTime = :creationTime")})
public class Messages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MESSAGEID", nullable = false)
    private Integer messageid;
    @Column(name = "SUBJECT", length = 80)
    private String subject;
    @Column(name = "TEXT", length = 10000)
    private String text;
    @Basic(optional = false)
    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime =  new Date(System.currentTimeMillis());
    @JoinColumn(name = "THREADID", referencedColumnName = "THREADID", nullable = false)
    @ManyToOne(optional = false)
    private Threads threadid;

    public Messages() {
    }

    public Messages(Integer messageid) {
        this.messageid = messageid;
    }

    public Messages(Integer messageid, Date creationTime) {
        this.messageid = messageid;
        this.creationTime = creationTime;
    }

    public Integer getMessageid() {
        return messageid;
    }

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Threads getThreadid() {
        return threadid;
    }

    public void setThreadid(Threads threadid) {
        this.threadid = threadid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageid != null ? messageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Messages)) {
            return false;
        }
        Messages other = (Messages) object;
        if ((this.messageid == null && other.messageid != null) || (this.messageid != null && !this.messageid.equals(other.messageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sun.faces.demo.model.Messages[messageid=" + messageid + "]";
    }

}
