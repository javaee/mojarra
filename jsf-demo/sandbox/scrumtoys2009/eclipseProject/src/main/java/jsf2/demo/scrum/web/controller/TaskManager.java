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

import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("taskManager")
@ConversationScoped
public class TaskManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Task currentTask;
    
    @Inject
    private StoryManager storyManager;

    @PostConstruct
    public void construct() {
        getLogger(getClass()).log(Level.INFO, "new intance of taskManager");
        init();
    }

    public void init() {
        Task task = new Task();
        Story currentStory = storyManager.getCurrentStory();
        task.setStory(currentStory);
        setCurrentTask(task);
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
                }
                storyManager.getCurrentStory().addTask(merged);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to save Task: " + currentTask, e);
                addMessage("Error on try to save Task", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public String edit(Task task) {
        setCurrentTask(task);
        return "edit";
    }

    public String remove(final Task task) {
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
            if (count != null && count > 1) {
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

    public Story getStory() {
        return storyManager.getCurrentStory();
    }

    public void setStory(Story story) {
        storyManager.setCurrentStory(story);
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public String showStories() {
        return "/story/show";
    }


    @PreDestroy
    public void destroy() {
        getLogger(getClass()).log(Level.INFO, "destroy intance of taskManager");
	currentTask = null;
	storyManager = null;
    }

}
