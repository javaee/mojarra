/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.sandbox.component.FileDownload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class FileDownloadRenderer extends Renderer {
    protected Object oldBinding = null;

    @Override
    public boolean getRendersChildren() {
        return false;
    }
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        FileDownload dl = (FileDownload) component;
        super.encodeBegin(context, component);
        if ((dl.getUrlVar() != null) || (component.getChildCount() > 0)){
            setElValue(context, dl);
        }
        if (FileDownload.METHOD_DOWNLOAD.equals(dl.getMethod())) { // || (comp.getChildCount() == 0)) {
            renderLink(context, dl);
        }
    }
    
    protected void setElValue(FacesContext context, FileDownload comp) {
        ValueBinding vb = Util.getValueBinding("#{"+comp.getUrlVar()+"}");
        vb.setValue(context, generateUri(context, comp));
        
    }
    
    protected void resetElValue(FacesContext context, FileDownload comp) {
        ValueBinding vb = Util.getValueBinding("#{"+comp.getUrlVar()+"}");
        vb.setValue(context, null);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        FileDownload dl = (FileDownload) component;
        if (FileDownload.METHOD_INLINE.equals(dl.getMethod())) {
            renderInline(context, dl);
        } else if (FileDownload.METHOD_DOWNLOAD.equals(dl.getMethod())) {
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement("a"); // finsh up the link!
        }
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlDownload-" + dl.getClientId(context), dl);
        resetElValue(context, dl);
        super.encodeEnd(context, component);
    }

    protected void renderInline(FacesContext context, FileDownload comp) throws IOException {
//        String width = comp.getWidth();
//        String height = comp.getHeight();
        
        ResponseWriter writer = context.getResponseWriter();
        String uri = generateUri(context, comp);
        if (Boolean.TRUE.equals(comp.getIframe())) {
            writer.startElement("iframe", comp);
            writer.writeAttribute("src", uri, "src");
//            writer.writeAttribute("width", width, "width");
//            writer.writeAttribute("height", height, "height");
            Util.renderPassThruAttributes(writer, comp);
            writer.endElement("iframe");
        } else {
            writer.startElement("object", comp);
            writer.writeAttribute("data", uri, "data");
            writer.writeAttribute("type", comp.getMimeType(), "type");
//            writer.writeAttribute("width", width, "width");
//            writer.writeAttribute("height", height, "height");
            Util.renderPassThruAttributes(writer, comp);
            writer.endElement("object");
        }
    }

    protected void renderLink(FacesContext context, FileDownload comp) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("a", comp);
        writer.writeAttribute("href", generateUri(context, comp), "data");
        Util.renderPassThruAttributes(writer, comp);
        if (comp.getChildCount() > 0) {
            //
        } else {
            writer.writeText("Download", null);
        }

    }

    // TODO:  Look at the Util method by the same name.  There's a good opportunity
    // for a refactor here.  Thank you, merging code bases. :)
    protected String generateUri(FacesContext context, FileDownload comp) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = Util.getAppBaseUrl(context) + mapping + "/" + FileDownload.DOWNLOAD_URI;
        } else {
            uri = FileDownload.DOWNLOAD_URI + mapping;
        }

        uri += "?" + FileDownload.REQUEST_PARAM + "=" + comp.getClientId(context);

        return uri;
    }
}