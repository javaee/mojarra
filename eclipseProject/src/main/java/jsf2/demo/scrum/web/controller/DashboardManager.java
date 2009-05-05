package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.web.event.CurrentSprintChangeEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@ManagedBean(name = "dashboardManager")
@ViewScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
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
        if (sprint != null) {
            storyList = new LinkedList<Story>(sprint.getStories());
        } else {
            storyList = Collections.emptyList();
        }
        stories = new ListDataModel<Story>(storyList);
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public DataModel<Story> getStories() {
        return stories;
    }

    public void setStories(DataModel<Story> stories) {
        this.stories = stories;
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
