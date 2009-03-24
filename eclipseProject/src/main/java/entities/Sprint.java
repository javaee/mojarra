package entities;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
/**
 * @category domain entity
 * @author Vinny 
 * @ since 2008
 */
@Entity @Table(name="sprints")
public class Sprint extends AbstractEntity {
	
	private static final long serialVersionUID = 5765920299851765782L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String goals;
	@Transient
	private int iterationScope;
	@Transient
	private int gainedStoryPoints;
	@Transient
	private Meetings meetings;
	@Temporal(TemporalType.DATE)
	private Date startedAt;
	@Temporal(TemporalType.DATE)
	private Date finishedAt;
	@Transient
	private String dailyMeetingTime;
	@OneToMany(mappedBy = "sprint", targetEntity = Story.class)
	private List<Story> stories = new LinkedList<Story>();

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
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

	public Meetings getMeetings() {
		return meetings;
	}

	public void setMeetings(Meetings meetings) {
		this.meetings = meetings;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

	public String getDailyMeetingTime() {
		return dailyMeetingTime;
	}

	public void setDailyMeetingTime(String dailyMeetingTime) {
		this.dailyMeetingTime = dailyMeetingTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
