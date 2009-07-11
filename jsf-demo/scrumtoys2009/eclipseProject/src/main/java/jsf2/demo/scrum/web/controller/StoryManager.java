package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;


@ManagedBean(name = "storyManager")
@SessionScoped
public class StoryManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{sprintManager}")
    private SprintManager sprintManager;    
    private Story currentStory;
    private DataModel<Story> stories;
    private List<Story> storyList;

    @PostConstruct
    public void construct() {
        init();
    }

    public void init() {
        
        Sprint currentSprint = sprintManager.getCurrentSprint();

        if (currentSprint != null) {
            Story story = new Story();
            setStoryList(new LinkedList<Story>(currentSprint.getStories()));
            story.setSprint(currentSprint);
            setCurrentStory(story);
        } else {
            setStoryList(new ArrayList<Story>());
        }
        stories = new ListDataModel<Story>(getStoryList());
    }

    public String create() {
        Story story = new Story();
        story.setSprint(sprintManager.getCurrentSprint());
        setCurrentStory(story);
        return "create";
    }

    public String save() {
        if (currentStory != null) {
            try {
                Story merged = doInTransaction(new PersistenceAction<Story>() {

                    public Story execute(EntityManager em) {
                        if (currentStory.isNew()) {
                            em.persist(currentStory);
                        } else if (!em.contains(currentStory)) {
                            return em.merge(currentStory);
                        }
                        return currentStory;
                    }
                });
                if (!currentStory.equals(merged)) {
                    setCurrentStory(merged);
                    int idx = getStoryList().indexOf(currentStory);
                    if (idx != -1) {
                        getStoryList().set(idx, merged);
                    }
                }
                sprintManager.getCurrentSprint().addStory(merged);
                if (!storyList.contains(merged)) {
                    getStoryList().add(merged);
                }
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to save Story: " + currentStory, e);
                addMessage("Error on try to save Story", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public String edit() {
        setCurrentStory(stories.getRowData());
        return "edit";
    }

    public String remove() {
        final Story story = stories.getRowData();
        if (story != null) {
            try {
                doInTransaction(new PersistenceActionWithoutResult() {

                    public void execute(EntityManager em) {
                        if (em.contains(story)) {
                            em.remove(story);
                        } else {
                            em.remove(em.merge(story));
                        }
                    }
                });
                sprintManager.getCurrentSprint().removeStory(story);
                getStoryList().remove(story);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to remove Story: " + currentStory, e);
                addMessage("Error on try to remove Story", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public void checkUniqueStoryName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        try {
            Long count = doInTransaction(new PersistenceAction<Long>() {

                public Long execute(EntityManager em) {
                    Query query = em.createNamedQuery((currentStory.isNew()) ? "story.new.countByNameAndSprint" : "story.countByNameAndSprint");
                    query.setParameter("name", newName);
                    query.setParameter("sprint", sprintManager.getCurrentSprint());
                    if (!currentStory.isNew()) {
                        query.setParameter("currentStory", currentStory);
                    }
                    return (Long) query.getSingleResult();
                }
            });
            if (count != null && count > 0) {
                throw new ValidatorException(getFacesMessageForKey("story.form.label.name.unique"));
            }
        } catch (ManagerException ex) {
            Logger.getLogger(StoryManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cancelEdit() {
        return "show";
    }

    public String showTasks() {
        setCurrentStory(stories.getRowData());
        return "showTasks";
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(Story currentStory) {
        this.currentStory = currentStory;        
    }

    public DataModel<Story> getStories() {
        if (sprintManager.getCurrentSprint()!=null){
            this.stories = new ListDataModel(sprintManager.getCurrentSprint().getStories());
            return stories;
        }
        else{
            return new ListDataModel<Story>();
        }
    }

    public void setStories(DataModel<Story> stories) {
        this.stories = stories;
    }

    public Sprint getSprint() {
        return sprintManager.getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        sprintManager.setCurrentSprint(sprint);
    }

    /**
     * @return the storyList
     */
    public List<Story> getStoryList() {
        if (sprintManager.getCurrentSprint()!=null){
            this.storyList = sprintManager.getCurrentSprint().getStories();
        }
        return this.storyList;
    }

    /**
     * @param storyList the storyList to set
     */
    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }

    /**
     * @return the sprintManager
     */
    public SprintManager getSprintManager() {
        return sprintManager;
    }

    /**
     * @param sprintManager the sprintManager to set
     */
    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    
}
