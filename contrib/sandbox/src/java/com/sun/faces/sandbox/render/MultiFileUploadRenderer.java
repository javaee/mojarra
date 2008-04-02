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
    public void encodeEnd(FacesContext context, UIComponent comp) throws IOException {
        if ((context == null) || (comp == null)) {
            throw new NullPointerException();
        }
        MultiFileUpload ul = (MultiFileUpload)comp;
        renderAppletTag(context, ul);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setAttribute("HtmlMultiFileUpload-" + ul.getClientId(context), ul);
    }
    
    protected void renderAppletTag(FacesContext context, MultiFileUpload ul) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String template = readTemplateFile(MultiFileUpload.TEMPLATE_FILE);
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        String uploadUrl = generateUri(context, MultiFileUpload.UPLOAD_URI) +
            "?" + MultiFileUpload.REQUEST_PARAM + "=" + ul.getClientId(context);
        String appletClass = ("button".equalsIgnoreCase(ul.getType())) ?
                "ButtonApplet" : "FullApplet"; // default to "full"
            
        StringBuilder deps = new StringBuilder();
        String sep = "";
            
        for (String dep : MultiFileUpload.DEP_JARS) {
            deps.append(sep)
//                .append(Util.generateStaticUri(dep)
                .append (Util.getAppBaseUrl(context))
                .append(MultiFileUpload.JARS_URI)
                .append(generateUri (context, dep)
                        );
            sep = ",";
        }
        
        template = template.replaceAll("%%%DEPS%%%", deps.toString())
            .replaceAll("%%%APPLET_CLASS%%%", appletClass)
            .replaceAll("%%%WIDTH%%%", ul.getWidth())
            .replaceAll("%%%HEIGHT%%%", ul.getHeight())
            .replaceAll("%%%START_DIR%%%", ul.getStartDir())
            .replaceAll("%%%UPLOAD_URL%%%", Util.getAppBaseUrl(context) + uploadUrl)
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