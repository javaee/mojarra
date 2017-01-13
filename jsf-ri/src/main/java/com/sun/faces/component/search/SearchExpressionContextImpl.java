/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.component.search;

import java.util.Set;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHint;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;

public class SearchExpressionContextImpl extends SearchExpressionContext {

    private final FacesContext facesContext;

    private UIComponent source;
    private Set<VisitHint> visitHints;
    private Set<SearchExpressionHint> expressionHints;

    public SearchExpressionContextImpl(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    @Override
    public UIComponent getSource() {
        return source;
    }

    public void setSource(UIComponent source) {
        this.source = source;
    }

    @Override
    public Set<VisitHint> getVisitHints() {
        return visitHints;
    }

    public void setVisitHints(Set<VisitHint> visitHints) {
        this.visitHints = visitHints;
    }

    @Override
    public Set<SearchExpressionHint> getExpressionHints() {
        return expressionHints;
    }

    public void setExpressionHints(Set<SearchExpressionHint> expressionHints) {
        this.expressionHints = expressionHints;
    }

    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }
}
