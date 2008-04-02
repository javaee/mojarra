/*
 * $Id: MockFacesContext.java,v 1.2 2003/01/16 20:24:26 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


// Mock Object for FacesContext
public class MockFacesContext extends FacesContext {

    public MockFacesContext() {
        setCurrentInstance(this);
    }

    private List applicationEvents = new ArrayList();

    public Iterator getApplicationEvents() {
        return (applicationEvents.iterator());
    }

    public int getApplicationEventsCount() {
        throw new UnsupportedOperationException();
    }

    public ApplicationHandler getApplicationHandler() {
        throw new UnsupportedOperationException();
    }

    private List facesEvents = new LinkedList();

    public Iterator getFacesEvents() {
        // FIXME - specialized iterator behavior is missing
        return (facesEvents.iterator());
    }

    public HttpSession getHttpSession() {
        throw new UnsupportedOperationException();
    }

    private Locale locale = null;

    public Locale getLocale() {
        return (this.locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMaximumSeverity() {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessages() {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessages(UIComponent component) {
        throw new UnsupportedOperationException();
    }

    private Tree requestTree = null;

    public Tree getRequestTree() {
        return (this.requestTree);
    }

    public void setRequestTree(Tree requestTree) {
        this.requestTree = requestTree;
        this.responseTree = requestTree;
    }

    private ResponseStream responseStream = null;

    public ResponseStream getResponseStream() {
        return (this.responseStream);
    }

    public void setResponseStream(ResponseStream responseStream) {
        this.responseStream = responseStream;
    }

    private Tree responseTree = null;

    public Tree getResponseTree() {
        return (this.responseTree);
    }

    public void setResponseTree(Tree responseTree) {
        this.responseTree = responseTree;
    }

    private ResponseWriter responseWriter = null;

    public ResponseWriter getResponseWriter() {
        return (this.responseWriter);
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    private ServletContext servletContext = null;

    public ServletContext getServletContext() {
        return (this.servletContext);
    }

    // Mock object setter
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private ServletRequest servletRequest = null;

    public ServletRequest getServletRequest() {
        return (this.servletRequest);
    }

    // Mock object setter
    public void setServletRequest(ServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    private ServletResponse servletResponse = null;

    public ServletResponse getServletResponse() {
        return (this.servletResponse);
    }

    // Mock object setter
    public void setServletResponse(ServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public ViewHandler getViewHandler() {
        throw new UnsupportedOperationException();
    }

    public void addApplicationEvent(ApplicationEvent event) {
        if (event == null) {
            throw new NullPointerException();
        }
        applicationEvents.add(event);
    }

    public void addFacesEvent(FacesEvent event) {
        if (event == null) {
            throw new NullPointerException();
        }
        facesEvents.add(event);
    }

    public void addMessage(UIComponent component, Message message) {
        if (message == null) {
            throw new NullPointerException();
        }
        throw new UnsupportedOperationException();
    }

    public Class getModelType(String modelReference) {
        throw new UnsupportedOperationException();
    }

    public Object getModelValue(String modelReference) {
        throw new UnsupportedOperationException();
    }

    public void setModelValue(String modelReference, Object value) {
        throw new UnsupportedOperationException();
    }

    public void release() {
        applicationEvents.clear();
        facesEvents.clear();
        locale = null;
        requestTree = null;
        responseStream = null;
        responseTree = null;
        responseWriter = null;
        servletContext = null;
        servletRequest = null;
        servletResponse = null;
    }

}
