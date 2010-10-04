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

package jsf2.demo.scrum.model.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "projects")
@NamedQueries({@NamedQuery(name = "project.getAll", query = "select p from Project as p"),
        @NamedQuery(name = "project.getAllOpen", query = "select p from Project as p where p.endDate is null"),
        @NamedQuery(name = "project.countByName", query = "select count(p) from Project as p where p.name = :name and not(p = :currentProject)"),
        @NamedQuery(name = "project.new.countByName", query = "select count(p) from Project as p where p.name = :name")})
public class Project extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, unique = true)
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Sprint> sprints;

    public Project() {
        this.startDate = new Date();
    }

    public Project(String name) {
        this();
        this.name = name;
    }

    public Project(String name, Date startDate) {
        this(name);
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Sprint> getSprints() {
        return (sprints != null) ? Collections.unmodifiableList(sprints) : Collections.<Sprint>emptyList();
    }

    public boolean addSprint(Sprint sprint) {
        if (sprints == null) {
            sprints = new LinkedList<Sprint>();
        }
        if (sprint != null && !sprints.contains(sprint)) {
            sprints.add(sprint);
            sprint.setProject(this);
            return true;
        }
        return false;
    }

    public boolean removeSpring(Sprint sprint) {
        return (sprints != null
                   && !sprints.isEmpty()
                   && sprints.remove(sprint));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        return !((this.name == null)
                 ? (other.name != null)
                 : !this.name.equals(other.name));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Project[name=" + name + ",startDate=" + startDate + ",endDate=" + endDate + "]";
    }
}
