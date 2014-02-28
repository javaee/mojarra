/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;


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

    @PreDestroy
    public void destroy() {
        sprintManager = null;
        currentStory = null;
        stories = null;
        if (null != storyList) {
            storyList.clear();
            storyList = null;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("storyManager");
                }
            }
        }

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
        if (sprintManager.getCurrentSprint() != null) {
            this.stories = new ListDataModel<Story>(sprintManager.getCurrentSprint().getStories());
            return stories;
        } else {
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
        if (sprintManager.getCurrentSprint() != null) {
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
