package jsf2.demo.scrum.web.controller;

import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = "dashboardManager")
@SessionScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{taskManager}")
    private TaskManager taskManager;
    @ManagedProperty("#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty("#{storyManager}")
    private StoryManager storyManager;


    private ListDataModel<Task> toDoTasks;
    private ListDataModel<Task> workingTasks;
    private ListDataModel<Task> doneTasks;


    public Sprint getSprint() {
        return getSprintManager().getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        this.getSprintManager().setCurrentSprint(sprint);
    }

    public DataModel<Story> getStories() {
        return storyManager.getStories();
    }


    public void setStories(DataModel<Story> stories) {
        storyManager.setStories(stories);
    }

    public ListDataModel getToDoTasks() {
        List toDoTasksList = new ArrayList();
        for (Story story : storyManager.getStoryList()) {
            toDoTasksList.addAll(story.getTodoTasks());
        }
        toDoTasks = new ListDataModel(toDoTasksList);
        return toDoTasks;
    }

    public ListDataModel getWorkingTasks() {
        List workingTasksList = new ArrayList();
        for (Story story : storyManager.getStoryList()) {
            workingTasksList.addAll(story.getWorkingTasks());
        }
        workingTasks = new ListDataModel(workingTasksList);
        return workingTasks;
    }

    public ListDataModel getDoneTasks() {
        List doneTasksList = new ArrayList();
        for (Story story : storyManager.getStoryList()) {
            doneTasksList.addAll(story.getDoneTasks());
        }
        doneTasks = new ListDataModel(doneTasksList);
        return doneTasks;
    }

    public String editToDoTask() {
        taskManager.setCurrentTask(toDoTasks.getRowData());
        return "/task/edit";
    }

    public String editDoneTask() {
        taskManager.setCurrentTask(doneTasks.getRowData());
        return "/task/edit";
    }

    public String editWorkingTask() {
        taskManager.setCurrentTask(workingTasks.getRowData());
        return "/task/edit";
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

}
