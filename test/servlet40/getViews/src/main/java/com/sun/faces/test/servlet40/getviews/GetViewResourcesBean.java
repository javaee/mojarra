package com.sun.faces.test.servlet40.getviews;

import static java.util.stream.Collectors.toList;
import static javax.faces.application.ResourceVisitOption.TOP_LEVEL_VIEWS_ONLY;

import java.util.List;
import java.util.stream.Stream;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceVisitOption;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class GetViewResourcesBean {
    
    @Inject
    private FacesContext context;
    
    @Inject @ManagedProperty("#{param['path']}")
    private String path;
    
    @Inject @ManagedProperty("#{param['maxDepth']}")
    private Integer maxDepth;
    
    @Inject @ManagedProperty("#{param['topLevel']}")
    private boolean topLevel;
    
    public List<String> getViewResources() {
        
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        
        path = path != null && !path.isEmpty() ? path : "/";
        ResourceVisitOption[] options = topLevel? new ResourceVisitOption[] {TOP_LEVEL_VIEWS_ONLY} : new ResourceVisitOption[] {};
        Stream<String> viewResources;
        
        if (maxDepth != null) {
            viewResources = resourceHandler.getViewResources(context, path, maxDepth, options);
        } else {
            viewResources = resourceHandler.getViewResources(context, path, options);
        }
        
        return viewResources.collect(toList());
    }

}
