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
import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;

/**
 *
 * @author Eder Magalhaes
 */
@Named("storyList")
@ViewScoped
public class StoryList extends AbstractManager implements Serializable{

    @Inject
    private StoryManager storyManager;

    private DataModel<Story> stories;
    private List<Story> storyList;

    @PostConstruct
    public void init() {
        try {
            setStoryList(doInTransaction(new PersistenceAction<List<Story>>() {

                @SuppressWarnings({"unchecked"})
                public List<Story> execute(EntityManager em) {
                    Sprint s = storyManager.getSprint();
                    if (s == null)
                        return Collections.EMPTY_LIST;

                    Query query = em.createNamedQuery("story.getBySprint");
                    query.setParameter("sprint", s);
                    return (List<Story>) query.getResultList();
                }
            }));
        } catch (ManagerException ex) {
            Logger.getLogger(StoryList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public DataModel<Story> getStories() {
        this.stories = new ListDataModel<Story>(storyList);

        return stories;
    }

    public void setStories(DataModel<Story> stories) {
        this.stories = stories;
    }


    public List<Story> getStoryList() {
        return this.storyList;
    }

    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }

    public String edit() {
        return storyManager.edit(stories.getRowData());
    }

    public String remove() {
        return storyManager.remove(stories.getRowData());
    }

    public String showTasks() {
        return storyManager.showTasks(stories.getRowData());
    }
}
