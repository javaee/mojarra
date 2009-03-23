package entities;

import java.util.LinkedList;
import java.util.List;

/**
 * @category domain entity
 * @author Vinny
 * @ since 2008
 *
 */
public class Meetings {
	public Meeting getEstimation() {
		return this.estimation;
	}
	public void setEstimation(Meeting $estimation) {
		this.estimation = $estimation;
	}
	public Meeting getSprintPlanningI() {
		return this.sprintPlanningI;
	}
	public void setSprintPlanningI(Meeting $sprintPlanningI) {
		this.sprintPlanningI = $sprintPlanningI;
	}
	public Meeting getSprintPlanningII() {
		return this.sprintPlanningII;
	}
	public void setSprintPlanningII(Meeting $sprintPlanningII) {
		this.sprintPlanningII = $sprintPlanningII;
	}
	public List<Meeting> getDailyMeetings() {
		return this.dailyMeetings;
	}
	public void setDailyMeetings(List<Meeting> $dailyMeetings) {
		this.dailyMeetings = $dailyMeetings;
	}
	public Meeting getRevision() {
		return this.revision;
	}
	public void setRevision(Meeting $revision) {
		this.revision = $revision;
	}
	public Meeting getRetrospective() {
		return this.retrospective;
	}
	public void setRetrospective(Meeting $retrospective) {
		this.retrospective = $retrospective;
	}
	private Meeting estimation;
	private Meeting sprintPlanningI;
	private Meeting sprintPlanningII;
	private List<Meeting> dailyMeetings = new LinkedList<Meeting>();
	private Meeting revision;
	private Meeting retrospective;
}
