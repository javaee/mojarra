/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package jsf2.demo.scrum.web.controller;

import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jsf2.demo.scrum.model.entities.TaskStatus;


@Named("dashboardManager")
@ViewScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TaskManager taskManager;
    @Inject
    private SprintManager sprintManager;
    @Inject
    private StoryManager storyManager;
    @Inject
    private StoryList storyList;

    private ListDataModel<Task> toDoTasks;
    private ListDataModel<Task> workingTasks;
    private ListDataModel<Task> doneTasks;

    @PostConstruct
    public void init() {
        List<Task> toDoTasksList = getTasksByStatus(TaskStatus.TODO);
        toDoTasks = new ListDataModel<Task>(toDoTasksList);
        
        List<Task> workingTasksList = getTasksByStatus(TaskStatus.WORKING);
        workingTasks = new ListDataModel<Task>(workingTasksList);
        
        List<Task> doneTasksList = getTasksByStatus(TaskStatus.DONE);
        doneTasks = new ListDataModel<Task>(doneTasksList);
    }
    
    @PreDestroy
    public void destroy() {
        toDoTasks = null;
        workingTasks = null;
        doneTasks = null;
    }

    public Sprint getSprint() {
        return getSprintManager().getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        this.getSprintManager().setCurrentSprint(sprint);
    }

    public DataModel<Story> getStories() {
        return storyList.getStories();
    }

    public void setStories(DataModel<Story> stories) {
        storyList.setStories(stories);
    }
    
    private List<Task> getTasksByStatus(final TaskStatus status) {
        try {
            return doInTransaction(new PersistenceAction<List<Task>>() {

                @SuppressWarnings({"unchecked"})
                public List<Task> execute(EntityManager em) {
                    Sprint s = storyManager.getSprint();
                    if (s == null)
                        return Collections.EMPTY_LIST;

                    Query query = em.createNamedQuery("task.getByStatusAndSprint");
                    query.setParameter("status", status);
                    query.setParameter("sprint", s);
                    
                    return (List<Task>) query.getResultList();
                }
            });
        } catch (ManagerException ex) {
            Logger.getLogger(DashboardManager.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.EMPTY_LIST;
        }
    }

    public ListDataModel<Task> getToDoTasks() {
        /*List<Task> toDoTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(toDoTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            toDoTasksList.addAll(story.getTodoTasks());
        }
        toDoTasks = new ListDataModel<Task>(toDoTasksList);*/
        return toDoTasks;
    }

    public ListDataModel<Task> getWorkingTasks() {
        /*List<Task> workingTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(workingTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            workingTasksList.addAll(story.getWorkingTasks());
        }
        workingTasks = new ListDataModel<Task>(workingTasksList);*/
        return workingTasks;
    }

    public ListDataModel<Task> getDoneTasks() {
        /*List<Task> doneTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(doneTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            doneTasksList.addAll(story.getDoneTasks());
        }
        doneTasks = new ListDataModel<Task>(doneTasksList);*/
        return doneTasks;
    }

    private String editTask(Task currentTask) {
        if (currentTask == null)
            return "";

        taskManager.setCurrentTask(currentTask);
        Story currentStory = storyManager.getCurrentStory();
        if (currentStory != currentTask.getStory()) {
            storyManager.setCurrentStory(currentTask.getStory());
        }
        return "/task/edit";
    }

    public String editToDoTask() {
        return editTask(toDoTasks.getRowData());
    }

    public String editDoneTask() {
        return editTask(doneTasks.getRowData());
    }

    public String editWorkingTask() {
        return editTask(workingTasks.getRowData());
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

    public StoryList getStoryList() {
        return storyList;
    }

    public void setStoryList(StoryList storyList) {
        this.storyList = storyList;
    }

}
