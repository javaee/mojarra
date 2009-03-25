package jsf.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Backing bean for sprints functionality
 * @author Vinny
 * @ since 2008
 *
 */
@ManagedBean(name="allInOneMB")
@ViewScoped
public class AllInOneMB extends AppMB {

	private static final long serialVersionUID = -4229563828412701957L;
	private SprintsMB sprintsMB = new SprintsMB();
	private StoriesMB storiesMB = new StoriesMB();
	private TasksMB tasksMB = new TasksMB();
	public SprintsMB getSprintsMB() {
		return sprintsMB;
	}
	public void setSprintsMB(SprintsMB sprintsMB) {
		this.sprintsMB = sprintsMB;
	}
	public StoriesMB getStoriesMB() {
		return storiesMB;
	}
	public void setStoriesMB(StoriesMB storiesMB) {
		this.storiesMB = storiesMB;
	}
	public TasksMB getTasksMB() {
		return tasksMB;
	}
	public void setTasksMB(TasksMB tasksMB) {
		this.tasksMB = tasksMB;
	}
}
