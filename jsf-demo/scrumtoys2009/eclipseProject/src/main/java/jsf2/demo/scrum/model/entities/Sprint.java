/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "sprints", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "project_id"}))
@NamedQueries({@NamedQuery(name = "sprint.countByNameAndProject", query = "select count(s) from Sprint as s where s.name = :name and s.project = :project and not(s = :currentSprint)"),
    @NamedQuery(name = "sprint.new.countByNameAndProject", query = "select count(s) from Sprint as s where s.name = :name and s.project = :project")})
public class Sprint extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false)
    private String name;
    private String goals;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "iteration_scope")
    private int iterationScope;
    @Column(name = "gained_story_points")
    private int gainedStoryPoints;
    @Temporal(TemporalType.TIME)
    @Column(name = "daily_meeting_time")
    private Date dailyMeetingTime;
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Story> stories;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Sprint() {
        this.startDate = new Date();
    }

    public Sprint(String name) {
        this();
        this.name = name;
    }

    public Sprint(String name, Project project) {
        this(name);
        this.project = project;
    }

    @SprintNameUniquenessConstraint
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
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

    public int getIterationScope() {
        return iterationScope;
    }

    public void setIterationScope(int iterationScope) {
        this.iterationScope = iterationScope;
    }

    public int getGainedStoryPoints() {
        return gainedStoryPoints;
    }

    public void setGainedStoryPoints(int gainedStoryPoints) {
        this.gainedStoryPoints = gainedStoryPoints;
    }

    public Date getDailyMeetingTime() {
        return dailyMeetingTime;
    }

    public void setDailyMeetingTime(Date dailyMeetingTime) {
        this.dailyMeetingTime = dailyMeetingTime;
    }

    public List<Story> getStories() {
        return (stories != null) ? Collections.unmodifiableList(stories) : Collections.EMPTY_LIST;
    }

    public boolean addStory(Story story) {
        if (stories == null) {
            stories = new LinkedList<Story>();
        }
        if (story != null && !stories.contains(story)) {
            stories.add(story);
            story.setSprint(this);
            return true;
        }
        return false;
    }

    public boolean removeStory(Story story) {
        if (stories != null && !stories.isEmpty()) {
            return stories.remove(story);
        } else {
            return false;
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sprint other = (Sprint) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.project != other.project && (this.project == null || !this.project.equals(other.project))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.project != null ? this.project.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Sprint[name=" + name + ",startDate=" + startDate + ",project=" + project + "]";
    }
}
