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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "tasks", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "story_id"}))
@NamedQueries({
    @NamedQuery(name = "task.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story and not(t = :currentTask)"),
    @NamedQuery(name = "task.getByStory", query = "select t from Task as t where t.story = :story"),
    @NamedQuery(name = "task.new.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story"),
    @NamedQuery(name = "task.getByStatusAndSprint", query = "select t from Task as t where t.status = :status and t.story.sprint = :sprint"),
    @NamedQuery(name = "task.remove.ByProject", query = "delete from Task as t where t.story.sprint.project = :project")
})
public class Task extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    public Task() {
        this.status = TaskStatus.TODO;
    }

    public Task(String name) {
        this();
        this.name = name;
    }

    public Task(String name, Story story) {
        this(name);
        if (story != null) {
            story.addTask(this);
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        changeTaskStatus(this.startDate, endDate);

    }

    protected void changeTaskStatus(Date startDate, Date endDate) {
        if (endDate != null) {
            this.setStatus(TaskStatus.DONE);
        }
        if (endDate == null && this.startDate != null) {
            this.setStatus(TaskStatus.WORKING);
        }
        if (endDate == null && this.startDate == null) {
            this.setStatus(TaskStatus.TODO);
        }
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
        changeTaskStatus(startDate, this.endDate);
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public String getStatusKeyI18n() {
        return "task.show.table.header.status."+status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return !(this.story != other.story
                 && (this.story == null || !this.story.equals(other.story)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 83 * hash + (this.story != null ? this.story.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Task[name=" + name + ",startDate=" + startDate + ",story=" + story + "]";
    }
}
