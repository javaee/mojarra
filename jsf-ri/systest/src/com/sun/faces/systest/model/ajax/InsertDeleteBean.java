package com.sun.faces.systest.model.ajax;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.FacesException;

@ManagedBean
@RequestScoped
public class InsertDeleteBean {

    public String insertBefore() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startInsertBefore("hr");
                writer.writeAttribute("id", "h2before", "id");
                writer.startElement("h2", null);
                writer.writeText("BEFORE", null, null);
                writer.endElement("h2");
                writer.endInsert();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }

    public String insertAfter() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.startInsertAfter("hr");
                writer.startElement("h2", null);
                writer.writeAttribute("id", "h2after", "id");
                writer.writeText("AFTER", null, null);
                writer.endElement("h2");
                writer.endInsert();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }


    public String removeBefore() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.delete("h2before");
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }

    public String removeAfter() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.delete("h2after");
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;    
    }

}
