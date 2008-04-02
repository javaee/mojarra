/**
 * 
 */
package com.sun.faces.sandbox.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.sandbox.component.FileDownload;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
// TODO:  Can this be merged with the StaticResourcePhaseListener?
public class DownloadPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent e) {
        PhaseId phase = e.getPhaseId();
        if (phase == PhaseId.RENDER_RESPONSE) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String uri = request.getRequestURI();
            if ((uri != null) && (uri.indexOf(FileDownload.DOWNLOAD_URI) > -1)){
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                String clientId = request.getParameter(FileDownload.REQUEST_PARAM);
                FileDownload comp = (FileDownload)request.getSession().getAttribute("HtmlDownload-" + clientId);
                if (comp != null) {
                    Object value = comp.getData();
                    if (value != null) {
                        byte[] data = null;
                        if (value instanceof byte[]) {
                            data = (byte[]) value;
                        } else if (value instanceof ByteArrayOutputStream) {
                            data = ((ByteArrayOutputStream) value).toByteArray();
                        } else if (value instanceof InputStream) {
                            data = getBytes((InputStream)value);
                        } else {
                            throw new FacesException("HtmlDownload:  an unsupported data type was found:  " +
                                    value.getClass().getName());
                        }
                        String mimeType = comp.getMimeType();
                        if (FileDownload.METHOD_DOWNLOAD.equals(comp.getMethod())) {
                            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                                    comp.getFileName() + "\"");
                        }
                        response.setContentType(mimeType);
                        try {
                            ServletOutputStream sos = response.getOutputStream();
                            sos.write(data);
                            sos.flush();
                            context.responseComplete();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent e) {
        // nothing to do here
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    protected  byte[] getBytes(InputStream is) {
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
        
        return baos.toByteArray();
    }

}
