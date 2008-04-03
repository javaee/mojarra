package com.sun.faces.sandbox.util;

import javax.servlet.http.HttpServletRequest;

public class HtmlEditorResourcePhaseListener extends StaticResourcePhaseListener {   
    private static final long serialVersionUID = 1L;
    
    protected String URL_PREFIX = "/html_editor";

    @Override
    protected boolean isThisMyFile(String uri) {
        return (uri != null) && (uri.indexOf(URL_PREFIX) > -1);
    }

    @Override
    protected String buildFileName(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return uri.substring(uri.indexOf("/", 1));
    }


    /*
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
                    if ("text/html".equals(mimeType) || "text/css".equals(mimeType) || "text/javascript".equals(mimeType)) {
                        String text = Util.readInString(is);
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

    private static String generateUrl(FacesContext context, String resource) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = "/" + mapping + URL_PREFIX + resource;
        } else {
            uri = URL_PREFIX + resource + mapping;
        }

        return Util.getAppBaseUrl(context) + uri;
    }

    private static void renderHtmlEditorJavascript(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException {
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
                String fileName = uri.substring(uri.indexOf("/", 1)); //extractFileName(context, uri);
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

    private String extractFileName(FacesContext context, String uri) {
        String fileName = "";
        String mapping = Util.getFacesMapping(context);
        int start = uri.indexOf(URL_PREFIX); // + URL_PREFIX.length();
        int end = uri.length();
        if (Util.isPrefixMapped(mapping)) {
            start += mapping.length();
        } else {
            end -= mapping.length();
        }

        fileName = uri.substring(start, end);

        return fileName;
    }

    private String processUrls(FacesContext context, String text) {
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
    */
}