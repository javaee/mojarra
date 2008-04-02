/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.sandbox.component.FileDownload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class FileDownloadRenderer extends Renderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent comp) throws IOException {
        if ((context == null) || (comp == null)) {
            throw new NullPointerException();
        }
        FileDownload dl = (FileDownload) comp;
        if (FileDownload.METHOD_INLINE.equals(dl.getMethod())) {
            renderInline(context, dl);
        } else if (FileDownload.METHOD_DOWNLOAD.equals(dl.getMethod())) {
            renderLink(context, dl);
        }
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlDownload-" + dl.getClientId(context), dl);
//      super.encodeEnd(arg0, arg1);
    }

    protected void renderInline(FacesContext context, FileDownload comp) throws IOException {
        String width = comp.getWidth();
        String height = comp.getHeight();
//        if ((width != null) && !"".equals(width)) {
//            throw new FacesException ("HtmlDownload:  'width' must be specified when method='inline'");
//        }
//        if ((height != null) && !"".equals(height)) {
//            throw new FacesException ("HtmlDownload:  'height' must be specified when method='inline'");
//        }
        
        ResponseWriter writer = context.getResponseWriter();
        String uri = generateUri(context, comp);
//        Boolean iframe = comp.getIframe() != null ? comp.getIframe() : Boolean.FALSE; 
        if (Boolean.TRUE.equals(comp.getIframe())) {
            writer.startElement("iframe", comp);
            writer.writeAttribute("src", uri, "src");
            writer.writeAttribute("width", width, "width");
            writer.writeAttribute("height", height, "height");
            writer.endElement("iframe");
        } else {
            writer.startElement("object", comp);
            writer.writeAttribute("data", uri, "data");
            writer.writeAttribute("type", comp.getMimeType(), "type");
            writer.writeAttribute("width", width, "width");
            writer.writeAttribute("height", height, "height");
            writer.endElement("object");
        }
    }

    protected void renderLink(FacesContext context, FileDownload comp) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String text = comp.getText();

        writer.startElement("a", comp);
        writer.writeAttribute("href", generateUri(context, comp), "data");
        if ((text != null) && !"".equals(text)) {
            writer.writeText(text, null);
        } else {
            writer.writeText("Download", null);
        }
        writer.endElement("a");

    }

    // TODO:  Look at the Util method by the same name.  There's a good opportunity
    // for a refactor here.  Thanks you merging code bases. :)
    protected String generateUri(FacesContext context, FileDownload comp) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = "/" + FileDownload.DOWNLOAD_URI + "." + mapping;
        } else {
            uri = FileDownload.DOWNLOAD_URI + mapping;
        }

        uri += "?" + FileDownload.REQUEST_PARAM + "=" + comp.getClientId(context);

        return uri;
    }
}