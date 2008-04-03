package com.sun.faces.sandbox.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlEditorResourcePhaseListener extends StaticResourcePhaseListener {   
    private static final long serialVersionUID = 1L;
    private static String URL_PREFIX = "/htmlEditorResources";

    public static String generateUrl(FacesContext context, String resource) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = "/" + mapping + URL_PREFIX + resource;
        } else {
            uri = URL_PREFIX + resource + mapping;
        }

        return Util.getAppBaseUrl(context) + uri;
    }

    public static void renderHtmlEditorJavascript(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException {
        String mapping = Util.getFacesMapping(context);
        writer.startElement("script", comp);
        writer.write("TinyMCE_Engine.prototype.JSF_MAPPING = '" + mapping + "';");
        if (Util.isPrefixMapped(mapping)) {
            writer.write("TinyMCE_Engine.prototype.MAPPING_TYPE = 'prefix';");
        } else {
            writer.write("TinyMCE_Engine.prototype.MAPPING_TYPE = 'suffix';");
        }
        writer.endElement("script");
    }

    public void afterPhase(PhaseEvent event) {
        // Do nothing here
    }

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) { 
            FacesContext context = event.getFacesContext();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(URL_PREFIX) > -1)){
                String fileName = extractFileName(context, uri);
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                String mimeType = getMimeType(fileName);
                response.setContentType(mimeType);

                processFile(context, fileName, response, mimeType);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    protected String extractFileName(FacesContext context, String uri) {
        String fileName = "";
        String mapping = Util.getFacesMapping(context);
        int start = uri.indexOf(URL_PREFIX) + URL_PREFIX.length();
        int end = uri.length();
        if (Util.isPrefixMapped(mapping)) {
            start += mapping.length();
        } else {
            end -= mapping.length();
        }

        fileName = uri.substring(start, end);

        return fileName;
    }

    @Override
    protected void processFile(FacesContext context, String fileName, HttpServletResponse response, String mimeType) {
        try {
            if (fileName.charAt(0) != '/') {
                fileName = "/" + fileName;
            }
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is != null) {
                try {
                    OutputStream os = response.getOutputStream();
                    /* This is a little ugly, but we need a way to make any CSS or JS references to static
                     * resources, such as images, point to a URL that this PL will pick up.  Ryan suggested
                     * a custom OutputStream like ViewHandlerImpl.WriteBehindStringWriter, which I'll have
                     * to chew on a bit.  This will get me by for now, I hope.
                     */
                    if ("text/html".equals(mimeType) || "text/css".equals(mimeType) || "text/javascript".equals(mimeType)) {
                        String text = Util.readInString(is);
                        text = processUrls(context, text);
                        os.write(text.getBytes());
                    } else {
                        super.processFile(context, fileName, response, mimeType);
                    }
                } finally {
                    is.close();
                }
            } else {
                response.sendError(404, "Could not find " + fileName);
            }
            context.responseComplete();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    protected String processUrls(FacesContext context, String text) {
        StringBuffer buf = new StringBuffer(text.length());
        int start = text.indexOf("{S}");
        int end = -1;
        if (start > -1) {
            buf.append(text.substring(0, start));
            while (start != -1) {
                end = text.indexOf("{E}", start+3);
                if (end > -1) {
                    buf.append(generateUrl(context, text.substring(start+3, end)));
                    start = text.indexOf("{S}", end+3);
                    if (start > -1) {
                        buf.append(text.substring(end+3, start));
                    } else {
                        buf.append(text.substring(end+3));
                    }
                } else {
                    break;
                }
            }
            return buf.toString();
       }
        
       return text;
    }
}