package entities;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity @Table(name="stories")
public class Story extends AbstractEntity {

	private static final long serialVersionUID = 6228732080959507217L;
	@Column
	private String acceptance;
	@Transient
	private List<Task> doneTasks = new LinkedList<Task>();
	@Column
	private int estimation;
	@Temporal(TemporalType.DATE)
	private Date finishedAt;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column
	private String name;
	@Column
	private int priority;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sprint")
	private Sprint sprint;
	@Temporal(TemporalType.DATE)
	private Date startedAt;
	@OneToMany(mappedBy = "story", targetEntity = Task.class)
	private List<Task> tasks = new LinkedList<Task>();
	@Transient
	private List<Task> todoTasks = new LinkedList<Task>();
	@Transient
	private List<Task> workingTasks = new LinkedList<Task>();

	public Story() {
	}

	public Story(List<Task> $tasks, String $name, String $acceptance,
			int $priority, int $estimation) {
		super();
		this.name = $name;
		this.acceptance = $acceptance;
		this.priority = $priority;
		this.estimation = $estimation;
		this.tasks = $tasks;
		for (Task task : $tasks)
			task.setStory(this);
	}

	public String getAcceptance() {
		return this.acceptance;
	}

	public int getCode() {
		return this.hashCode();
	}

	public List<Task> getDoneTasks() {
		return doneTasks;
	}

	public int getEstimation() {
		return this.estimation;
	}

	public Date getFinishedAt() {
		return this.finishedAt;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public int getPriority() {
		return this.priority;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public Date getStartedAt() {
		return this.startedAt;
	}

	public String getStatus() {
		String status = "Não iniciada";
		if (this.startedAt != null) {
			status = "Em andamento";
			if (this.finishedAt != null) {
				status = "Finalizada";
			}// if
		}// if
		return status;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public List<Task> getTodoTasks() {
		return todoTasks;
	}

	public List<Task> getWorkingTasks() {
		return workingTasks;
	}

	public boolean isFinished() {
		return this.finishedAt != null;
	}

	public boolean isStarted() {
		return this.startedAt != null;
	}

	public void remove(Task $task) {
		this.tasks.remove($task);
	}

	public void setAcceptance(String $acceptance) {
		this.acceptance = $acceptance;
	}

	public void setDoneTasks(List<Task> doneTasks) {
		this.doneTasks = doneTasks;
	}

	public void setEstimation(int $estimation) {
		this.estimation = $estimation;
	}

	public void setFinishedAt(Date $finishedAt) {
		this.finishedAt = $finishedAt;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String $name) {
		this.name = $name;
	}

	public void setPriority(int $priority) {
		this.priority = $priority;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public void setStartedAt(Date $startedAt) {
		this.startedAt = $startedAt;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setTodoTasks(List<Task> todoTasks) {
		this.todoTasks = todoTasks;
	}

	public void setWorkingTasks(List<Task> workingTasks) {
		this.workingTasks = workingTasks;
	}
	
}
