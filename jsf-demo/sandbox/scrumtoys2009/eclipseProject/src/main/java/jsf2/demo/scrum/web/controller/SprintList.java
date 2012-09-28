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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.flow.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jsf2.demo.scrum.model.entities.Project;
import jsf2.demo.scrum.model.entities.Sprint;

/**
 *
 * @author Eder Magalhaes
 */
@Named("sprintList")
@ViewScoped
public class SprintList extends AbstractManager implements Serializable {
    
    @Inject
    private SprintManager sprintManager;
    
    private DataModel<Sprint> sprints;
    
    private List<Sprint> sprintList;
    
    @PostConstruct
    public void init() {
        try {
            setSprintList(doInTransaction(new PersistenceAction<List<Sprint>>() {

                @SuppressWarnings({"unchecked"}) 
                public List<Sprint> execute(EntityManager em) {
                    Project p = sprintManager.getProject();
                    if (p == null)
                        return Collections.EMPTY_LIST;
                    
                    Query query = em.createNamedQuery("sprint.getByProject");
                    query.setParameter("project", p);
                    return (List<Sprint>) query.getResultList();
                }
            }));
        } catch (ManagerException ex) {
            Logger.getLogger(SprintList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public DataModel<Sprint> getSprints() {
        this.sprints = new ListDataModel<Sprint>(sprintList);
        return this.sprints;
    }

    public void setSprints(DataModel<Sprint> sprints) {
        this.sprints = sprints;
    }

    public void setSprintList(List<Sprint> sprintList) {
        this.sprintList = sprintList;
    }

    public List<Sprint> getSprintList() {
        return sprintList;
    }
    
    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }
    
    public String edit() {
        return sprintManager.edit(sprints.getRowData());
    }
    
    public String remove() {
        String result = sprintManager.remove(sprints.getRowData());
        init();
        return result;
    }
    
    public String showStories() {
        return sprintManager.showStories(sprints.getRowData());
    }

    public String showDashboard() {
        return sprintManager.showDashboard(sprints.getRowData());
    }
    
}
