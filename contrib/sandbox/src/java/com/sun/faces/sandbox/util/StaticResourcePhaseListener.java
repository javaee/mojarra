/**
 * 
 */
package com.sun.faces.sandbox.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class StaticResourcePhaseListener implements PhaseListener {
//  TODO:  Class.forName("some.shale.class"); useShaleStuff(); catch (ClassNotFound) {useOurStuff()}; 
    private static final long serialVersionUID = 1L;
    protected static Map mimeTypes = new HashMap();
    {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpg", "images/jpg");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("png", "image/png");
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RESTORE_VIEW) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(Util.STATIC_RESOURCE_IDENTIFIER) > -1)){
                try {
                    /*
                    String fileName = uri.substring(uri.lastIndexOf(Util.STATIC_RESOURCE_IDENTIFIER))
                        .substring(Util.STATIC_RESOURCE_IDENTIFIER.length());
                    
                    String mapping = Util.getFacesMapping(context);
                    if (!Util.isPrefixMapped(mapping)) {
                        int index = fileName.indexOf(mapping);
                        fileName = fileName.substring(0, index);
                    }
                    */
                    // TODO:  make sure we can sandbox this correctly (i.e., no file=../foo.txt)
                    String fileName = request.getParameter("file");
                    fileName = "/META-INF/static" +
                        (fileName.startsWith("/") ? "" : "/") + 
                        fileName;
                    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is != null) {
                        OutputStream os = response.getOutputStream();
                        String mimeType = getMimeType(fileName);
                        response.setContentType(mimeType);
                        /* This is a little ugly, but we need a way to make any CSS or JS references to static
                         * resources, such as images, point to a URL that this PL will pick up.  Ryan suggested
                         * a custom OutputStream like ViewHandlerImpl.WriteBehindStringWriter, which I'll have
                         * to chew on a bit.  This will get me by for now, I hope.
                         */
                        if ("text/css".equals(mimeType) || "text/javascript".equals(mimeType)) {
                            String text = Util.readInString(is);
                            text = text.replaceAll("%%%BASE_URL%%%", Util.generateStaticUri(""));
                            os.write(text.getBytes());
                        } else {
                            int count = 0;
                            byte[] buffer = new byte[4096];
                            while ((count = is.read(buffer)) != -1) {
                                if (count > 0) {
                                    os.write(buffer, 0, count);
                                }
                            }
                        }
                    } else {
                        response.sendError(404);
                    }
                    context.responseComplete();
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }
    }
    
    protected String getMimeType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        return (String)mimeTypes.get(extension);
    }

    public void afterPhase(PhaseEvent event) {
        // Do nothing
    }
}