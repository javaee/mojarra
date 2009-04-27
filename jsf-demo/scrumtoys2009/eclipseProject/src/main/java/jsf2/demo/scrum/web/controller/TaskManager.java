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
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;
import jsf2.demo.scrum.web.event.CurrentStoryChangeEvent;
import jsf2.demo.scrum.web.event.CurrentTaskChangeEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@ManagedBean(name = "taskManager")
@SessionScoped
public class TaskManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Task currentTask;
    private DataModel<Task> tasks;
    private List<Task> taskList;
    @ManagedProperty("#{storyManager.currentStory}")
    private Story story;
    private SystemEventListener storyChangeListener;

    @PostConstruct
    public void construct() {
        storyChangeListener = new StoryChangeListener();
        subscribeToEvent(CurrentStoryChangeEvent.class, storyChangeListener);
        init();
    }

    @PreDestroy
    public void destroy() {
        unsubscribeFromEvent(CurrentStoryChangeEvent.class, storyChangeListener);
    }

    public void init() {
        Task task = new Task();
        task.setStory(story);
        setCurrentTask(task);
        if (story != null) {
            taskList = new LinkedList<Task>(story.getTasks());
        } else {
            taskList = Collections.emptyList();
        }
        tasks = new ListDataModel<Task>(taskList);
    }

    public String create() {
        Task task = new Task();
        task.setStory(story);
        setCurrentTask(task);
        return "create";
    }

    public String save() {
        if (currentTask != null) {
            try {
                Task merged = doInTransaction(new PersistenceAction<Task>() {

                    public Task execute(EntityManager em) {
                        if (currentTask.isNew()) {
                            em.persist(currentTask);
                        } else if (!em.contains(currentTask)) {
                            return em.merge(currentTask);
                        }
                        return currentTask;
                    }
                });
                if (!currentTask.equals(merged)) {
                    setCurrentTask(merged);
                    int idx = taskList.indexOf(currentTask);
                    if (idx != -1) {
                        taskList.set(idx, merged);
                    }
                }
                story.addTask(merged);
                if (!taskList.contains(merged)) {
                    taskList.add(merged);
                }
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to save Task: " + currentTask, e);
                addMessage("Error on try to save Task", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public String edit() {
        setCurrentTask(tasks.getRowData());
        return "edit";
    }

    public String remove() {
        final Task task = tasks.getRowData();
        if (task != null) {
            try {
                doInTransaction(new PersistenceActionWithoutResult() {

                    public void execute(EntityManager em) {
                        if (em.contains(task)) {
                            em.remove(task);
                        } else {
                            em.remove(em.merge(task));
                        }
                    }
                });
                story.removeTask(task);
                taskList.remove(task);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to remove Task: " + currentTask, e);
                addMessage("Error on try to remove Task", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public void checkUniqueTaskName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        try {
            Long count = doInTransaction(new PersistenceAction<Long>() {

                public Long execute(EntityManager em) {
                    Query query = em.createNamedQuery((currentTask.isNew()) ? "task.new.countByNameAndStory" : "task.countByNameAndStory");
                    query.setParameter("name", newName);
                    query.setParameter("story", story);
                    if (!currentTask.isNew()) {
                        query.setParameter("currentTask", (!currentTask.isNew()) ? currentTask : null);
                    }
                    return (Long) query.getSingleResult();
                }
            });
            if (count != null && count > 0) {
                throw new ValidatorException(getFacesMessageForKey("task.form.label.name.unique"));
            }
        } catch (ManagerException ex) {
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cancelEdit() {
        return "show";
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
        publishEvent(CurrentTaskChangeEvent.class, currentTask);
    }

    public DataModel<Task> getTasks() {
        return tasks;
    }

    public void setTasks(DataModel<Task> tasks) {
        this.tasks = tasks;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    private class StoryChangeListener implements SystemEventListener, Serializable {

        public void processEvent(SystemEvent event) throws AbortProcessingException {
            story = (Story) event.getSource();
            init();
        }

        public boolean isListenerForSource(Object source) {
            return (source instanceof Story);
        }
    }
}
