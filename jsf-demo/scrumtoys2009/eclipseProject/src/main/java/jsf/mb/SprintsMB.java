package jsf.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
@ManagedBean(name = "sprintsMB")

public class SprintsMB extends AppCrudMB<SprintDAO, SprintService, Sprint> {

    private static final long serialVersionUID = -1094408292080760267L;
    private boolean showAddNewStoryForm = false;

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

    /**
     * @return the showAddNewStoryForm
     */
    public boolean isShowAddNewStoryForm() {
        System.out.println("isShowAddNewStoryForm - showAddNewStoryForm =" + showAddNewStoryForm);
        return showAddNewStoryForm;
    }

    /**
     * @param showAddNewStoryForm the showAddNewStoryForm to set
     */
    public void setShowAddNewStoryForm(boolean showAddNewStoryForm) {
        System.out.println("setShowAddNewStoryForm - showAddNewStoryForm =" + showAddNewStoryForm);
        this.showAddNewStoryForm = showAddNewStoryForm;
        System.out.println("setShowAddNewStoryForm - showAddNewStoryForm =" + showAddNewStoryForm);

    }

    public void showForm() {
        System.out.println("showForm - showAddNewStoryForm =" + showAddNewStoryForm);
        this.setShowAddNewStoryForm(true);
        System.out.println("showForm - showAddNewStoryForm =" + showAddNewStoryForm);
    }

    public void hideForm() {
        System.out.println("hideForm - showAddNewStoryForm =" + showAddNewStoryForm);
        this.setShowAddNewStoryForm(false);
        System.out.println("hideForm - showAddNewStoryForm =" + showAddNewStoryForm);
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

    public String showStories() {
        Sprint selectedSprint = (Sprint) this.getDm().getRowData();
        FacesUtil.getInstance().putRequestAttr("selectedSprint", selectedSprint);
        return OUTCOME.STORIES.getValue();
    }

    public String showDashboard() {
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
