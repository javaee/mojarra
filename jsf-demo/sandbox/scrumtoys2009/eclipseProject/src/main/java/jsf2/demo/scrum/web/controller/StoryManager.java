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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.faces.context.ExternalContext;


@Named("storyManager")
@SessionScoped
public class StoryManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private SprintManager sprintManager;
    private Story currentStory;

    @PostConstruct
    public void construct() {
        init();
    }

    @PreDestroy
    public void destroy() {
        sprintManager = null;
        currentStory = null;
        
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
            story.setSprint(currentSprint);
            setCurrentStory(story);
        }
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
                }
                sprintManager.getCurrentSprint().addStory(merged);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to save Story: " + currentStory, e);
                addMessage("Error on try to save Story", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    public String edit(Story story) {
        setCurrentStory(story);
        return "edit";
    }

    public String remove(final Story story) {
        if (story != null) {
            try {
                doInTransaction(new PersistenceActionWithoutResult() {

                    public void execute(EntityManager em) {
                        Query query = em.createNamedQuery("task.remove.ByProject");
                        query.setParameter("project", story.getSprint().getProject());
                        query.executeUpdate();

                        em.remove(em.find(Story.class, story.getId()));
                    }
                });
                sprintManager.getCurrentSprint().removeStory(story);
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
    
    private @Inject Conversation conversation;

    public String showTasks(Story story) {
        conversation.begin();
        setCurrentStory(story);
        return "showTasks";
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(Story currentStory) {
        this.currentStory = currentStory;
    }

    public Sprint getSprint() {
        return sprintManager.getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        sprintManager.setCurrentSprint(sprint);
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }


}
