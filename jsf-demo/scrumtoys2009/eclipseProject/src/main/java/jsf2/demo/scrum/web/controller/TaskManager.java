package jsf2.demo.scrum.web.controller;

import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@ManagedBean(name = "taskManager")
@SessionScoped
public class TaskManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Task currentTask;
    private DataModel<Task> tasks;
    private List<Task> taskList;
    @ManagedProperty("#{storyManager}")
    private StoryManager storyManager;

    @PostConstruct
    public void construct() {
        init();
    }


    public void init() {
        Task task = new Task();
        Story currentStory = storyManager.getCurrentStory();
        task.setStory(currentStory);
        setCurrentTask(task);
        if (currentStory != null) {
            taskList = new LinkedList<Task>(currentStory.getTasks());
        } else {
            taskList = new ArrayList<Task>();
        }
        tasks = new ListDataModel<Task>(taskList);
    }

    public String create() {
        Task task = new Task();
        task.setStory(storyManager.getCurrentStory());
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
                storyManager.getCurrentStory().addTask(merged);
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
                storyManager.getCurrentStory().removeTask(task);
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
                    query.setParameter("story", storyManager.getCurrentStory());
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
    }

    public DataModel<Task> getTasks() {
        this.tasks = new ListDataModel(storyManager.getCurrentStory().getTasks());
        return tasks;
    }

    public void setTasks(DataModel<Task> tasks) {
        this.tasks = tasks;
    }

    public Story getStory() {
        return storyManager.getCurrentStory();
    }

    public void setStory(Story story) {
        storyManager.setCurrentStory(story);
    }

    /**
     * @return the storyManager
     */
    public StoryManager getStoryManager() {
        return storyManager;
    }

    /**
     * @param storyManager the storyManager to set
     */
    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

}
