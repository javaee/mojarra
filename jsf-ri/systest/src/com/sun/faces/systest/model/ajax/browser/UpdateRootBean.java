package com.sun.faces.systest.model.ajax.browser;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.FacesException;

@ManagedBean
@RequestScoped
public class UpdateRootBean {

    public String updateRootSimple() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }

    public String updateRootBody() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootAll() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootFaulty() {  // missing end tags

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateBodySimple() {  // missing end tags

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateBodyTag() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewBody");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeText("PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateHead() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewHead");
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootAllEvent() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("input", null);
                writer.writeAttribute("id","newbutton","id");
                writer.writeAttribute("type","button","type");
                writer.writeAttribute("onclick","checkPass();","onclick");
                writer.writeAttribute("value","Click Me","value");
                writer.endElement("input");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootSimpleEvent() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("input", null);
                writer.writeAttribute("id","newbutton","id");
                writer.writeAttribute("type","button","type");
                writer.writeAttribute("onclick","checkPass();","onclick");
                writer.writeAttribute("value","Click Me","value");
                writer.endElement("input");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }


    public String updateRootAllStyle() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("html",null);
                writer.startElement("head",null);
                writer.startElement("title",null);
                writer.writeText("PASSED",null);
                writer.endElement("title");
                writer.endElement("head");
                writer.startElement("body",null);
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeAttribute("style", "background-color: green","style");
                writer.writeText("Green means PASSED",null);
                writer.endElement("span");
                writer.endElement("body");
                writer.endElement("html");
                writer.endUpdate();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    public String updateRootSimpleStyle() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewRoot");
                writer.startElement("span", null);
                writer.writeAttribute("id","newvalue","id");
                writer.writeAttribute("style", "background-color: green","style");
                writer.writeText("Green means PASSED",null);
                writer.endElement("span");
                writer.endUpdate();
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
