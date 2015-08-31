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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.test.servlet30.faceletsViewAction;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


@RequestScoped @ManagedBean
public class NewsReader {

    private FacesContext facesContext;

    @ManagedProperty("#{newsIndex}")
    private NewsIndex newsIndex;

    private List<NewsStory> stories;

    private NewsStory selectedStory;

    private Long selectedStoryId;

    @PostConstruct
    public void postConstruct() {
        facesContext = FacesContext.getCurrentInstance();
        stories = new ArrayList<NewsStory>(newsIndex.getEntries().values());
    }

    public void loadStory() {
        if (!facesContext.isValidationFailed()) {
            NewsStory story = newsIndex.getStory(selectedStoryId);
            if (story != null) {
                selectedStory = story;
                return;
            }

            facesContext.addMessage(null, new FacesMessage("The headline you requested does not exist."));
        }

    }
    
    public String goToPage01IfValidationFailed() {
        if (facesContext.isValidationFailed()) {
            return "/page01";
        }
        return null;
    }

    public List<NewsStory> getStories() {
        return stories;
    }

    public NewsStory getSelectedStory() {
        return selectedStory;
    }

    public Long getSelectedStoryId() {
        return selectedStoryId;
    }

    public void setSelectedStoryId(Long storyId) {
        this.selectedStoryId = storyId;
    }
    
    public boolean isMissingStoryId() {
        return null == selectedStoryId;
    }

    // Injected Properties

    public void setNewsIndex(NewsIndex newsIndex) {
        this.newsIndex = newsIndex;
    }

}
