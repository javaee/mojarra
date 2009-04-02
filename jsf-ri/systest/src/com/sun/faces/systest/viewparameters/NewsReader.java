package com.sun.faces.systest.viewparameters;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 * @author Dan Allen
 */
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

        //facesContext.getFlash().setKeepMessages(true); // only needed if navigation case is a redirect
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "/viewParameters/page01");
        // we would like the following instead
        // facesContext.fireNavigation("home");
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

    // Injected Properties

    public void setNewsIndex(NewsIndex newsIndex) {
        this.newsIndex = newsIndex;
    }

}
