package jsf.mb;

import javax.faces.model.ManagedBean;
import javax.faces.model.ViewScoped;

import jsf.util.FacesUtil;
import services.SprintService;
import dao.DAOException;
import dao.SprintDAO;
import dao.memory.SprintDAOMemory;
import entities.Sprint;

/**
 * Backing bean for sprints functionality
 * @author Vinny
 * @ since 2008
 *
 */
@ManagedBean(name="sprintsMB")
@ViewScoped
public class SprintsMB extends AppCrudMB<SprintDAO, SprintService, Sprint> {

	private static final long serialVersionUID = -1094408292080760267L;

	@Override
	public String getEntitiesLabel() {
		return "sprints";
	}

	@Override
	public String getEntityLabel() {
		return "sprint";
	}

	@Override
	public Sprint getNewEntity() {
		return new Sprint();
	}

	@Override
	public SprintService getService() {
		return new SprintService();
	}
	
	private enum OUTCOME {
		STORIES("stories"), DASHBOARD("dashboard");
		private String value;

		OUTCOME(String $value) {
			this.value = $value;
		}

		public String getValue() {
			return this.value;
		}
	}	

	public String showStories(){
		Sprint selectedSprint = (Sprint) this.getDm().getRowData();
		FacesUtil.getInstance().putRequestAttr("selectedSprint", selectedSprint);
		return OUTCOME.STORIES.getValue();
	}

	public String showDashboard(){
		Sprint selectedSprint = (Sprint) this.getDm().getRowData();
		try {
			selectedSprint = new SprintDAOMemory().findAllById(selectedSprint);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FacesUtil.getInstance().putRequestAttr("selectedSprint", selectedSprint);
		return OUTCOME.DASHBOARD.getValue();
	}
	
	@Override
	public boolean preSave() {
		return true;
	}
}
