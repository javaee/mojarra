package entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @category domain entity
 * @author Vinny 
 * @ since 2008
 */
@Entity @Table(name="tasks")
public class Task extends AbstractEntity {

	private static final long serialVersionUID = -469166036811737970L;
	@Column
	@Temporal(value=TemporalType.DATE)
	private Date finishedAt;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column
	private String name;
	@Column
	@Temporal(value=TemporalType.DATE)
	private Date startedAt;
	@Column
	@Enumerated(EnumType.ORDINAL)
	private TaskStatus status = TaskStatus.TODO;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="story")
	private Story story;
	
	public Task(){}

	public Task(String $name) {
		this();
		this.name = $name;
	}
	
	public int getCode(){
		return this.hashCode();
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
	
	public Date getStartedAt() {
		return this.startedAt;
	}
	public TaskStatus getStatus() {
		return this.status;
	}

	public Story getStory() {
		return this.story;
	}


	public boolean isFinished(){
		return this.finishedAt != null;
	}

	public boolean isStarted() {
		return this.startedAt != null;
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

	public void setStartedAt(Date $startedAt) {
		this.startedAt = $startedAt;
	}

	public void setStatus(TaskStatus $status) {
		this.status = $status;
	}

	public void setStatusForDone(Date $finishedAt){
		this.setFinishedAt($finishedAt);
		this.status = TaskStatus.DONE;
	}
	
	public void setStatusForTODO(){
		this.startedAt = null;
		this.finishedAt = null;
		this.status = TaskStatus.TODO;
	}
	public void setStatusForWorking(Date $startedAt){
		this.setStartedAt($startedAt);
		this.status = TaskStatus.WORKING;
	}

	public void setStory(Story $story) {
		this.story = $story;
	}
	
}
