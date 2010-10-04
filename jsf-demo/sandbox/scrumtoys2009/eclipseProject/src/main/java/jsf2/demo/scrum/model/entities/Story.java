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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "stories", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "sprint_id"}))
@NamedQueries({@NamedQuery(name = "story.countByNameAndSprint", query = "select count(s) from Story as s where s.name = :name and s.sprint = :sprint and not(s = :currentStory)"),
        @NamedQuery(name = "story.new.countByNameAndSprint", query = "select count(s) from Story as s where s.name = :name and s.sprint = :sprint")})
public class Story extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false)
    private String name;
    private int priority;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    private String acceptance;
    private int estimation;
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public Story() {
        this.startDate = new Date();
    }

    public Story(String name) {
        this();
        this.name = name;
    }

    public Story(String name, Sprint sprint) {
        this(name);
        this.name = name;
        if (sprint != null) {
            sprint.addStory(this);
        }
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Task> getTasks() {
        return (tasks != null) ? Collections.unmodifiableList(tasks) : Collections.<Task>emptyList();
    }

    public List<Task> getDoneTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.DONE));
    }

    public List<Task> getWorkingTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.WORKING));
    }

    public List<Task> getTodoTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.TODO));
    }

    private List<Task> getTasks(TaskStatus status) {
        List<Task> result = new LinkedList<Task>();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                if (task != null && status.equals(task.getStatus())) {
                    result.add(task);
                }
            }
        }
        return result;
    }

    public boolean addTask(Task task) {
        if (tasks == null) {
            tasks = new LinkedList<Task>();
        }
        if (task != null && !tasks.contains(task)) {
            tasks.add(task);
            task.setStory(this);
            return true;
        }
        return false;
    }

    public boolean removeTask(Task task) {
        return (tasks != null && !tasks.isEmpty() && tasks.remove(task));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Story other = (Story) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return !(this.sprint != other.sprint
                 && (this.sprint == null || !this.sprint.equals(other.sprint)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.sprint != null ? this.sprint.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Story[name=" + name + ",startDate=" + startDate + ",sprint=" + sprint + "]";
    }
}
