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
