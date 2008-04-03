/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.sandbox.component.MultiFileUpload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee
 *
 */
public class MultiFileUploadRenderer extends Renderer {
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

        MultiFileUpload ul = (MultiFileUpload)component;
        renderAppletTag(context, ul);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlMultiFileUpload-" + ul.getClientId(context), ul);
    }
    
    protected void renderAppletTag(FacesContext context, MultiFileUpload ul) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String template = readTemplateFile(MultiFileUpload.TEMPLATE_FILE);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // Just pass back /AppContext/foo.jsf (or /AppContext/faces/foo.xhtml, etc)
        // The applet will construct the full URL based on
        // documentBase
        String uploadUrl = 
            context.getApplication().getViewHandler().getActionURL(context, generateUri(context, MultiFileUpload.UPLOAD_URI)) +
            "?" + MultiFileUpload.REQUEST_PARAM + "=" + ul.getClientId(context);
        String appletClass = ("button".equalsIgnoreCase(ul.getType())) ?
                "ButtonApplet" : "FullApplet"; // default to "full"
            
        StringBuilder deps = new StringBuilder();
        String sep = "";
            
        for (String dep : MultiFileUpload.DEP_JARS) {
            String depURI =
                context.getApplication().getViewHandler().getActionURL(context, generateUri(context, MultiFileUpload.JARS_URI + dep));
            deps.append(sep)
//                .append(Util.generateStaticUri(dep))
                .append(depURI);
            sep = ",";
        }
        
        template = template.replaceAll("%%%DEPS%%%", deps.toString())
            .replaceAll("%%%APPLET_CLASS%%%", appletClass)
            .replaceAll("%%%WIDTH%%%", ul.getWidth())
            .replaceAll("%%%HEIGHT%%%", ul.getHeight())
            .replaceAll("%%%START_DIR%%%", ul.getStartDir())
            .replaceAll("%%%UPLOAD_URL%%%", uploadUrl)
            .replaceAll("%%%BUTTON_TEXT%%%", ul.getButtonText())
            .replaceAll("%%%FILE_FILTER%%%", ul.getFileFilter())
            .replaceAll("%%%SESSION_ID%%%", request.getSession().getId());
        writer.write(template);
        
    }
    
    protected String readTemplateFile(String fileName) {
        InputStream is = getClass().getResourceAsStream(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
            is.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toString();
    }
    
    // TODO:  Look at the Util method by the same name.  There's a good opportunity
    // for a refactor here.  Thank you, merging code bases. :)
    protected String generateUri(FacesContext context, String target) {
        String uri = "";
        String mapping = Util.getFacesMapping(context);
        if (Util.isPrefixMapped(mapping)) {
            uri = "/" + target + "." + mapping;
        } else {
            uri = target + mapping;
        }

        return uri;
    }
}