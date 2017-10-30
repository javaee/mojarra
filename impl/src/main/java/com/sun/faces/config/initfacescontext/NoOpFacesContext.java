package com.sun.faces.config.initfacescontext;

import static java.util.Collections.emptyList;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

public abstract class NoOpFacesContext extends FacesContext {

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        List<String> list = emptyList();
        return list.iterator();
    }

    @Override
    public FacesMessage.Severity getMaximumSeverity() {
        return FacesMessage.SEVERITY_INFO;
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        List<FacesMessage> list = emptyList();
        return list.iterator();
    }

    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        return getMessages();
    }

    @Override
    public List<FacesMessage> getMessageList() {
        return emptyList();
    }

    @Override
    public List<FacesMessage> getMessageList(String clientId) {
        return emptyList();
    }

    @Override
    public RenderKit getRenderKit() {
        return null;
    }

    @Override
    public boolean getRenderResponse() {
        return true;
    }

    @Override
    public boolean getResponseComplete() {
        return true;
    }

    @Override
    public boolean isValidationFailed() {
        return false;
    }

    @Override
    public ResponseStream getResponseStream() {
        return null;
    }

    @Override
    public void setResponseStream(ResponseStream responseStream) {
    }

    @Override
    public ResponseWriter getResponseWriter() {
        return null;
    }

    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {
    }

    @Override
    public void setViewRoot(UIViewRoot root) {
    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {
    }

    @Override
    public void renderResponse() {
    }

    @Override
    public void responseComplete() {
    }

    @Override
    public void validationFailed() {
    }
    
    
}
