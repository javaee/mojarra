package jsf.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.ManagedBean;
import javax.faces.model.ViewScoped;

import jsf.util.FacesUtil;
import services.ServicesException;
import services.StoryService;
import dao.StoryDAO;
import entities.Sprint;
import entities.Story;

/**
 * Backing bean for stories functionality
 * @author Vinny
 * @ since 2008
 *
 */
@ManagedBean(name="storiesMB")
@ViewScoped
public class StoriesMB extends AppCrudMB<StoryDAO, StoryService, Story> {

	private static final long serialVersionUID = -5038542176993627445L;
	
	private Sprint selectedSprint;
	
	@Override
	public String getEntitiesLabel() {
		return "stories";
	}

	@Override
	public String getEntityLabel() {
		return "story";
	}

	@Override
	public StoryService getService() {
		return new StoryService();
	}

	@Override
	public List<Story> getLoadedEntities() throws ServicesException {
		List<Story> stories = (this.selectedSprint != null)?this.getService().findBySprint(this.selectedSprint):super.getLoadedEntities();
		return stories;
	}

	@Override
	public Story getNewEntity() {
		Story story = new Story();
		story.setSprint(this.selectedSprint);
		return story;
	}

	@PostConstruct
	protected void init(){
		super.init();
		this.selectedSprint = (Sprint) FacesUtil.getInstance().getRequestAttr("selectedSprint");
	}

	public String showTasks(){
		Story selectedStory = (Story) this.getDm().getRowData();
		FacesUtil.getInstance().putRequestAttr("selectedStory", selectedStory);
		return OUTCOME.TASKS.getValue();
	}
	
	@Override
	public boolean preSave() {
		this.getToSave().setSprint(this.selectedSprint);
		return true;
	}
	
	private enum OUTCOME {
		TASKS("tasks");
		private String value;

		OUTCOME(String $value) {
			this.value = $value;
		}

		public String getValue() {
			return this.value;
		}
	}	
	
}
