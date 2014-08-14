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

import jsf2.demo.scrum.model.entities.Project;
import jsf2.demo.scrum.model.entities.Sprint;

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
import javax.faces.context.ExternalContext;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("sprintManager")
@SessionScoped
public class SprintManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Sprint currentSprint;
    @Inject
    private ProjectManager projectManager;
    private Project currentProject;

    @PostConstruct
    public void construct() {
        init();
    }

    @PreDestroy
    public void destroy() {
        projectManager = null;
        currentProject = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("sprintManager");
                }
            }
        }

    }

    public void init() {
        Sprint sprint = new Sprint();
        Project pmCurrentProject = getProjectManager().getCurrentProject();
        sprint.setProject(pmCurrentProject);
        setCurrentSprint(sprint);
    }

    public String create() {
        Sprint sprint = new Sprint();
        sprint.setProject(getProjectManager().getCurrentProject());
        setCurrentSprint(sprint);
        return "create";
    }

    public String save() {
        if (currentSprint != null) {
            try {
                Sprint merged = doInTransaction(new PersistenceAction<Sprint>() {

                    public Sprint execute(EntityManager em) {
                        if (currentSprint.isNew()) {
                            em.persist(currentSprint);
                        } else if (!em.contains(currentSprint)) {
                            return em.merge(currentSprint);
                        }
                        return currentSprint;
                    }
                });
                if (!currentSprint.equals(merged)) {
                    setCurrentSprint(merged);
                }
                getProjectManager().getCurrentProject().addSprint(merged);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to save Sprint: " + currentSprint, e);
                addMessage("Error on try to save Sprint", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }
    
    
    public String edit(Sprint sprint) {
        setCurrentSprint(sprint);
        return "edit";
    }

    public String remove(final Sprint sprint) {
        if (sprint != null) {
            try {
                doInTransaction(new PersistenceActionWithoutResult() {

                    public void execute(EntityManager em) {
                        Query query = em.createNamedQuery("task.remove.ByProject");
                        query.setParameter("project", sprint.getProject());
                        query.executeUpdate();

                        query = em.createNamedQuery("story.remove.ByProject");
                        query.setParameter("project", sprint.getProject());
                        query.executeUpdate();

                        em.remove(em.find(Sprint.class, sprint.getId()));
                    }
                });
                getProjectManager().getCurrentProject().removeSpring(sprint);
            } catch (Exception e) {
                getLogger(getClass()).log(Level.SEVERE, "Error on try to remove Sprint: " + currentSprint, e);
                addMessage("Error on try to remove Sprint", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        return "show";
    }

    /*
    * This method can be pointed to by a validator methodExpression, such as:
    *
    * <h:inputText id="itName" value="#{sprintManager.currentSprint.name}" required="true"
    *   requiredMessage="#{i18n['sprint.form.label.name.required']}" maxLength="30" size="30"
    *   validator="#{sprintManager.checkUniqueSprintName}" />
    */

    public void checkUniqueSprintNameFacesValidatorMethod(FacesContext context, UIComponent component, Object newValue) {

        final String newName = (String) newValue;
        String message = checkUniqueSprintNameApplicationValidatorMethod(newName);
        if (null != message) {
            throw new ValidatorException(getFacesMessageForKey("sprint.form.label.name.unique"));
        }
    }


    /*
    * This method is called by the JSR-303 SprintNameUniquenessConstraintValidator.
    * If it returns non-null, the result must be interpreted as the localized
    * validation message.
    *
    */

    public String checkUniqueSprintNameApplicationValidatorMethod(String newValue) {
        String message = null;

        final String newName = newValue;
        try {
            Long count = doInTransaction(new PersistenceAction<Long>() {

                public Long execute(EntityManager em) {
                    Query query = em.createNamedQuery((currentSprint.isNew()) ? "sprint.new.countByNameAndProject" : "sprint.countByNameAndProject");
                    query.setParameter("name", newName);
                    query.setParameter("project", getProjectManager().getCurrentProject());
                    if (!currentSprint.isNew()) {
                        query.setParameter("currentSprint", currentSprint);
                    }
                    return (Long) query.getSingleResult();
                }
            });
            if (count != null && count > 0) {
                message = getFacesMessageForKey("sprint.form.label.name.unique").getSummary();
            }
        } catch (ManagerException ex) {
            Logger.getLogger(SprintManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    public String cancelEdit() {
        return "show";
    }

    public String showStories(Sprint sprint) {
        setCurrentSprint(sprint);
        return "showStories";
    }

    public String showDashboard(Sprint sprint) {
        setCurrentSprint(sprint);
        return "showDashboard";
    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(Sprint currentSprint) {
        this.currentSprint = currentSprint;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public Project getProject() {
        Project pmCurrentProject = projectManager.getCurrentProject();
        // Verify if the currentProject is out of date
        // If there is a new CurrentProject we need to update sprintList and set currentSprint to null and tell user he/she needs to select a Sprint
        if (pmCurrentProject != currentProject) {
            this.setCurrentSprint(null);
            this.currentProject = pmCurrentProject;
        }
        return currentProject;
    }

    public void setProject(Project project) {
        projectManager.setCurrentProject(project);
    }

}
