package jsf2.demo.scrum.model.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
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
    @Transient
    private List<Task> doneTasks;
    @Transient
    private List<Task> workingTasks;
    @Transient
    private List<Task> todoTasks;

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
        return (tasks != null) ? Collections.unmodifiableList(tasks) : Collections.EMPTY_LIST;
    }

    public List<Task> getDoneTasks() {
        if (doneTasks == null) {
            doneTasks = Collections.unmodifiableList(getTasks(TaskStatus.DONE));
        }
        return doneTasks;
    }

    public List<Task> getWorkingTasks() {
        if (workingTasks == null) {
            workingTasks = Collections.unmodifiableList(getTasks(TaskStatus.WORKING));
        }
        return workingTasks;
    }

    public List<Task> getTodoTasks() {
        if (todoTasks == null) {
            todoTasks = Collections.unmodifiableList(getTasks(TaskStatus.TODO));
        }
        return todoTasks;
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
            doneTasks = null;
            workingTasks = null;
            todoTasks = null;
            tasks.add(task);
            task.setStory(this);
            return true;
        }
        return false;
    }

    public boolean removeTask(Task task) {
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.remove(task);
        } else {
            return false;
        }
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
        if (this.sprint != other.sprint && (this.sprint == null || !this.sprint.equals(other.sprint))) {
            return false;
        }
        return true;
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
