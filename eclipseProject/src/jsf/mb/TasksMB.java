package jsf.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ManagedBean;
import javax.faces.model.ViewScoped;

import jsf.util.FacesUtil;
import services.ServicesException;
import services.TaskService;
import entities.Story;
import entities.Task;

/**
 * Backing bean for tasks functionality
 * @author Vinny
 * @ since 2008
 *
 */
@ManagedBean(name="tasksMB")
@ViewScoped
public class TasksMB extends AppMB {

	private static final long serialVersionUID = -3357324277219101747L;
	private TaskService service = new TaskService();
	private DataModel<Task> dmTasks;
	private List<Task> lstTasks;
	private Task taskToSave;
	private Task taskToEdit = null;
	private Task taskToRemove = null;
	private Story selectedStory;
	/**
	 * @category MB action
	 */
	public String load() {
		try {
			this.lstTasks = this.getLoadedEntities();
			this.dmTasks = new ListDataModel<Task>(this.lstTasks);
			getFacesUtil().addGlobalInfoMessage("Tarefas lidas com sucesso.", String.format("%d tarefas lidas com sucesso.", this.lstTasks.size()));
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}//try
		return OUTCOME.SAME_PAGE.getValue();
	}
	
	public List<Task> getLoadedEntities() throws ServicesException {
		return (this.selectedStory != null)?this.service.findByStory(this.selectedStory):this.service.findAll();
	}
	

	/**
	 * @category MB action
	 */
	public String save() {
		try {
			if (this.preSave()){
				this.service.save(this.taskToSave);
				getFacesUtil().addGlobalInfoMessage("Tarefa criada com sucesso.", String.format("Tarefas %s criada com sucesso.", this.taskToSave.getName()));
				this.taskToSave = this.getNewEntity();
			}//if
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}//try
		return this.load();
	}

	public Task getNewEntity() {
		Task task = new Task();
		task.setStory(this.selectedStory);
		return task;
	}
	
	
	@PostConstruct
	protected void init(){
		this.selectedStory = (Story) FacesUtil.getInstance().getRequestAttr("selectedStory");
		this.taskToSave = this.getNewEntity();
		this.load();
	}

	public boolean preSave() {
		this.taskToSave.setStory(this.selectedStory);
		return true;
	}	
	
	private FacesUtil getFacesUtil() {
		return FacesUtil.getInstance();
	}
	
	/**
	 * @category MB action
	 */
	public String edit() {
		this.taskToSave = (Task) this.dmTasks.getRowData();
		getFacesUtil().addGlobalInfoMessage("Edite a tarefa selecionada.", String.format("Edite a tarefa %s. Clique no botão %s para terminar.", this.taskToSave.getName(),"save"));
		return OUTCOME.SAME_PAGE.getValue();
	}

	/**
	 * @category MB action
	 */
	public String remove() {
		try {
			this.taskToRemove = (Task) this.dmTasks.getRowData();
			this.service.remove(taskToRemove);
			getFacesUtil().addGlobalInfoMessage("Tarefa removida com sucesso.", String.format("Tarefa %s. Removida com sucesso.", this.taskToRemove.getName(),"save"));
			this.taskToRemove = null;
		} catch (ServicesException e) {
			getFacesUtil().createErrorMessage(e);
		}//try
		return this.load();
	}
	
	
	private enum OUTCOME {
		SAME_PAGE(null);
		private String value;
		OUTCOME(String $value){
			this.value = $value;
		}
		public String getValue(){
			return this.value;
		}
	}

	public Task getTaskToEdit() {
		return taskToEdit;
	}

	public void setTaskToEdit(Task taskToEdit) {
		this.taskToEdit = taskToEdit;
	}

	public Task getTaskToSave() {
		return taskToSave;
	}

	public void setTaskToSave(Task taskToSave) {
		this.taskToSave = taskToSave;
	}

	public Task getTaskToRemove() {
		return taskToRemove;
	}

	public void setTaskToRemove(Task taskToRemove) {
		this.taskToRemove = taskToRemove;
	}

	public DataModel<Task> getDmTasks() {
		return dmTasks;
	}

	
	
}
