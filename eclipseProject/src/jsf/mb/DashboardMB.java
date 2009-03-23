package jsf.mb;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import jsf.util.FacesUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import entities.Sprint;
import entities.Story;
import entities.Task;
import entities.TaskStatus;
import entities.sample.SampleSprint;

/**
 * Backing bean for dashboard functionality
 * @author Vinny
 * @ since 2008
 *
 */
//@ManagedBean(name="dashboardMB")
//@ViewScoped
public class DashboardMB extends AppMB  {

	private Sprint selectedSprint;
	private DataModel<Story> dmStories;
	private Log log = LogFactory.getLog(DashboardMB.class);
	private List<Task> workingTasks = new LinkedList<Task>();
	private DataModel<Task> dmWorkingTasks;
	private List<Task> doneTasks = new LinkedList<Task>();
	private DataModel<Task> dmDoneTasks;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @category constructor
	 */
	public DashboardMB(){
//		this.init();
	}
	
	/**
	 * @category action
	 */
	public String startDashboard(){
		FacesUtil.getInstance().addGlobalInfoMessage("Dashboard started.",String.format("Dashboard %s started at %s.", this.selectedSprint.getName(), this.selectedSprint.getStartedAt()));
		this.refreshTasks();
		return null;
	}
	
	/**
	 * @category action
	 */
	public String startStory(){
		Story story = (Story) this.getDmStories().getRowData();
		story.setStartedAt(new Date());
		this.refreshTasks();
		return null;
	}

	/**
	 * @category action
	 */
	public String finishStory(){
		Story story = (Story) this.getDmStories().getRowData();
		story.setFinishedAt(new Date());
		this.refreshTasks();
		return null;
	}
	
	/**
	 * @category action
	 */
	public String startTask(){
		Task task = (Task) this.getDmTodoTasks().getRowData();
		task.setStatusForWorking(new Date());
		this.refreshTasks();
		return null;
	}

	/**
	 * @category action
	 */
	public String removeTodoTask(){
		Task task = (Task) this.getDmTodoTasks().getRowData();
		task.getStory().remove(task);
		this.refreshTasks();
		return null;
	}

	/**
	 * @category action
	 */
	public String removeWorkingTask(){
		Task task = (Task) this.getDmWorkingTasks().getRowData();
		task.getStory().remove(task);
		this.refreshTasks();
		return null;
	}

	/**
	 * @category action
	 */
	public String finishTask(){
		Task task = (Task) this.getDmWorkingTasks().getRowData();
		task.setStatusForDone(new Date());
		this.refreshTasks();
		return null;
	}
		
	/**
	 * @category 
	 */
	
	public String getStatus(){
		String status = "Not started";
		if (this.selectedSprint.getStartedAt() != null){
			status = "Working";
			if (this.selectedSprint.getFinishedAt() != null){
				status = "Finished";
			}//if
		}//if
		return status;
	}
	
	public int getDaysToFinish(){
		return (this.selectedSprint.getStartedAt() != null)?((int) (new Date().getTime() - this.selectedSprint.getStartedAt().getTime()) / (1000*60*60*24)): 0; 
	}
	
	/**
	 * DICA anotação abaixo substitui a necessidade da chamada no construtor
	 */
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init(){
		log.info("Starting stories and their tasks.");
		this.selectedSprint = (Sprint) FacesUtil.getInstance().getRequestAttr("selectedSprint");
		if (this.selectedSprint == null){
			log.info("Loading sample sprint.");
			this.selectedSprint = new SampleSprint();
		} else {
			log.info("Loading SELECTED sprint.");
		}//if
		this.dmStories = new ListDataModel<Story>(this.selectedSprint.getStories());
		log.info(String.format("Number of stories: %d", this.selectedSprint.getStories().size()));
		this.refreshTasks();
	}

	private void refreshTasks() {
		this.doneTasks.clear();
		this.todoTasks.clear();
		this.workingTasks.clear();
		this.selectedSprint.setIterationScope(0);
		this.selectedSprint.setGainedStoryPoints(0);
		for (Story story: this.selectedSprint.getStories()){
			this.selectedSprint.setIterationScope(this.selectedSprint.getIterationScope() + story.getEstimation());
			if (story.getFinishedAt() != null) this.selectedSprint.setGainedStoryPoints(this.selectedSprint.getGainedStoryPoints() + story.getEstimation());
			for (Task task: story.getTasks()){
				if (task.getStory().getFinishedAt() != null){
					task.setStatusForDone(task.getStory().getFinishedAt());
					if (task.getStartedAt() == null){
						task.setStartedAt(task.getStory().getStartedAt());
					}//if
				}//if
				if (task.getStatus() == TaskStatus.DONE) this.doneTasks.add(task);
				if (task.getStatus() == TaskStatus.TODO) this.todoTasks.add(task);
				if (task.getStatus() == TaskStatus.WORKING) this.workingTasks.add(task);
			}//for
		}//for
		this.dmDoneTasks = new ListDataModel<Task>(this.doneTasks);
		this.dmTodoTasks = new ListDataModel<Task>(this.todoTasks);
		this.dmWorkingTasks = new ListDataModel<Task>(this.workingTasks);
	}

    @PreDestroy
	protected void release(){
		log.info("Liberando recursos do MB.");		
	}
	
	public DataModel<Story> getDmStories(){
		return this.dmStories;
	}
	
	private List<Task> todoTasks = new LinkedList<Task>();
	private DataModel<Task> dmTodoTasks;
	
	public List<Task> getTodoTasks() {
		return this.todoTasks;
	}

	public DataModel<Task> getDmTodoTasks() {
		return this.dmTodoTasks;
	}

	public List<Task> getWorkingTasks() {
		return this.workingTasks;
	}

	public DataModel<Task> getDmWorkingTasks() {
		return this.dmWorkingTasks;
	}

	public List<Task> getDoneTasks() {
		return this.doneTasks;
	}

	public DataModel<Task> getDmDoneTasks() {
		return this.dmDoneTasks;
	}

	public Sprint getSelectedSprint() {
		return selectedSprint;
	}
	
}
