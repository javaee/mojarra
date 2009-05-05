package jsf2.demo.scrum.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "tasks", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "story_id"}))
@NamedQueries({@NamedQuery(name = "task.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story and not(t = :currentTask)"),
    @NamedQuery(name = "task.new.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story")})
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

    protected void changeTaskStatus(Date startDate , Date endDate){
        if (endDate !=null){
            this.setStatus(status.DONE);
        }
        if (endDate ==null && this.startDate!= null){
            this.setStatus(status.WORKING);
        }
        if (endDate  ==null && this.startDate== null){
            this.setStatus(status.TODO);
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
        if (this.story != other.story && (this.story == null || !this.story.equals(other.story))) {
            return false;
        }
        return true;
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
