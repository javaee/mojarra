/**
 * 
 */
package com.sun.faces.sandbox.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.faces.sandbox.component.MultiFileUpload;
import com.sun.faces.sandbox.model.FileHolder;


/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class MultiFileUploadPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 1L;

    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(MultiFileUpload.JARS_URI) > -1)){
                try {
                    String fileName = uri.substring(uri.lastIndexOf(MultiFileUpload.JARS_URI))
                        .substring(MultiFileUpload.JARS_URI.length());
                    int index = fileName.indexOf(".jar");
                    fileName = "/META-INF/jars/" + fileName.substring(0, index+4);
                    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                    InputStream is = getClass().getResourceAsStream(fileName);
                    OutputStream os = response.getOutputStream();

                    response.setContentType("application/x-java-applet");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                    int count = 0;
                    byte[] buffer = new byte[4096];
                    while ((count = is.read(buffer)) != -1) {
                        if (count > 0) {
                            os.write(buffer, 0, count);
                        }
                    }
                    context.responseComplete();
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
    }

    public void afterPhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(MultiFileUpload.UPLOAD_URI) > -1)){
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                String clientId = request.getParameter(MultiFileUpload.REQUEST_PARAM);
                Object obj = request.getSession().getAttribute("HtmlMultiFileUpload-" + clientId);

                MultiFileUpload comp = (MultiFileUpload) obj;
                DiskFileItemFactory factory = new DiskFileItemFactory();
                int maxFileSize = 0;
                try {
                    maxFileSize = Integer.parseInt(comp.getMaxFileSize());
                } catch (Exception nfe) {
                    //
                }
                factory.setSizeThreshold(maxFileSize);
                ServletFileUpload upload = new ServletFileUpload(factory);

                try {
                    List items = upload.parseRequest(request);  // Get the FileItems from the request
                    FileHolder fileHolder = comp.getFileHolder();
                    if (fileHolder != null) {
                        for (int i = 0; i < items.size(); i++) {
                            FileItem item = (FileItem)items.get(i);
                            fileHolder.addFile(item.getName(), item.getInputStream());
                        }
                    }
                    String destinationUrl = comp.getDestinationUrl();
                    // If we have a complete URL, just run with that
                    if (!destinationUrl.startsWith("http")) {
                        // If we get just a view name, append the / so the API call below
                        // will be happy
                        if (destinationUrl.charAt(0) != '/') {
                            destinationUrl = "/" + destinationUrl;
                        }
                        // Generate the complete URL.
                        // NOTE:  This does *NOT* currently resolve a navigation case result string
                        // to the mapped view from the Faces configuration.  This assumes that the string
                        // in question is the *name* of a view, so "success" will become
                        // http://localhost:8080/MyContext/success.jsf, not
                        // http://localhost:8080/MyContext/youdidit.jsf that you may have mapped in
                        // your Faces config.  This is due to the lack of a portable way of making
                        // that resolution.
                        // TODO:  Fix this
                        destinationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                            response.encodeURL(context.getApplication().getViewHandler().getActionURL(context, destinationUrl));
                    }
                    response.setContentType("text/text");
                    response.getOutputStream().write(destinationUrl.getBytes());
                    context.responseComplete();
                } catch (Exception e1) {
                    // TODO:  Determine the configured error page and go there?
                    // Return a 500 to the client?
                    e1.printStackTrace();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
