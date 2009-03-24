package jsf.mb;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ManagedBean;
import javax.faces.model.SelectItem;
import javax.faces.model.ViewScoped;

import jsf.util.FacesUtil;
import services.BurndownService;
import services.ServicesException;
import entities.Sprint;

@ManagedBean(name="burndownMB")
@ViewScoped
public class BurndownMB extends AppMB {

	private static final long serialVersionUID = 3114986033418662140L;

	private List<Sprint> sprints;
	private DataModel<Sprint> dmSprints;
	private List<SelectItem> siSprints;
	private BurndownService service;
	private Integer selectedSprintId;
	private Sprint selectedSprint;
	
	@PostConstruct
	private void init(){
		this.service = new BurndownService();
//		this.selectedSprint = new Sprint();
		try {
			this.sprints = this.service.findSprints();
			this.dmSprints = new ListDataModel<Sprint>(this.sprints);
			this.siSprints = new LinkedList<SelectItem>();
			for (Sprint sprint: this.sprints){
				this.siSprints.add(new SelectItem(sprint.getId(), sprint.getName()));
			}//for
		} catch (ServicesException e) {
			FacesUtil.getInstance().createErrorMessage(e);
		}//try
	}
	
	public String update(){
		this.selectedSprint = new Sprint();
		try {
			this.selectedSprint = this.service.findById(this.selectedSprintId);
		} catch (ServicesException e) {
			FacesUtil.getInstance().createErrorMessage(e);
		}//try
		return null;
	}

	public DataModel<Sprint> getDmSprints() {
		return dmSprints;
	}


	public Integer getSelectedSprintId() {
		return selectedSprintId;
	}

	public void setSelectedSprintId(Integer selectedSprintId) {
		this.selectedSprintId = selectedSprintId;
	}

	public Sprint getSelectedSprint() {
		return selectedSprint;
	}

	public List<SelectItem> getSiSprints() {
		return siSprints;
	}
	
	
}
