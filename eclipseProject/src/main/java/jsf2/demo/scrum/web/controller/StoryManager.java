package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.web.event.CurrentSprintChangeEvent;
import jsf2.demo.scrum.web.event.CurrentStoryChangeEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@ManagedBean(name = "storyManager")
@SessionScoped
public class StoryManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Story currentStory;
    private DataModel<Story> stories;
    private List<Story> storyList;
    @ManagedProperty("#{sprintManager.currentSprint}")
    private Sprint sprint;
    private SystemEventListener sprintChangeListener;

    @PostConstruct
    public void construct() {
        sprintChangeListener = new SprintChangeListener();
        subscribeToEvent(CurrentSprintChangeEvent.class, sprintChangeListener);
        init();
    }

    @PreDestroy
    public void destroy() {
        unsubscribeFromEvent(CurrentSprintChangeEvent.class, sprintChangeListener);
    }

    public void init() {
        Story story = new Story();
        story.setSprint(sprint);
        setCurrentStory(story);
        if (sprint != null) {
            storyList = new LinkedList<Story>(sprint.getStories());
        } else {
            storyList = Collections.emptyList();
        }
        stories = new ListDataModel<Story>(storyList);
    }

    public String create() {
        Story story = new Story();
        story.setSprint(sprint);
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
                    int idx = storyList.indexOf(currentStory);
                    if (idx != -1) {
                        storyList.set(idx, merged);
                    }
                }
                sprint.addStory(merged);
                if (!storyList.contains(merged)) {
                    storyList.add(merged);
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
                sprint.removeStory(story);
                storyList.remove(story);
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
                    query.setParameter("sprint", sprint);
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
        publishEvent(CurrentStoryChangeEvent.class, currentStory);
    }

    public DataModel<Story> getStories() {
        return stories;
    }

    public void setStories(DataModel<Story> stories) {
        this.stories = stories;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    private class SprintChangeListener implements SystemEventListener, Serializable {

        public void processEvent(SystemEvent event) throws AbortProcessingException {
            sprint = (Sprint) event.getSource();
            init();
        }

        public boolean isListenerForSource(Object source) {
            return (source instanceof Sprint);
        }
    }
}
