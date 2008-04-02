/**
 * 
 */
package com.sun.faces.sandbox.web.applet.upload;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

/**
 * @author Jason Lee
 *
 */
public abstract class BaseApplet extends JApplet {
    protected static final boolean CANCEL = false;
    protected static final boolean UPLOAD = true;

    protected String baseUrl = "";
    protected String redirectUrl;
    protected boolean result = CANCEL;
    protected String sessionId;
    protected String startDir = "/";
    protected String type;
    protected String uploadUrl;
    protected String buttonText;
    protected String fileFilter;

    @Override
    public String[][] getParameterInfo() {
        String[][] info = {
                // Parameter Name     Kind of Value   Description
                {"startdir", "FileSystem Path", "Where to start looking for the files"},
                {"url", "URL", "URL to which to upload the files" },
                {"sessionId", "Session ID", "The current session ID"},
                {"type", "Applet 'type'", "Should the applet render in 'full' mode, or 'button' mode?"},
                {"buttonText", "Button Text", "Text that should appear on the button in 'button' mode"},
                {"fileFilter", "File Filter", "The description of the filter, and the extensions to allow (e.g.:  'Description|ext1,ext2,ext3)"}
        };
        return info;
    }

    @Override
    public void init() {
        super.init();
        getParameters();
    }

    protected void getParameters() {
        String docBase[] = getDocumentBase().toString().split("/");
        if (docBase != null) {
            baseUrl = docBase[0] + "//" + docBase[2];
        }
        startDir = getParameter("startdir") != null ? getParameter("startdir") : "\\";
        uploadUrl = baseUrl + (getParameter("url") != null ? getParameter("url") : "/"); // should this blow up instead?
        sessionId = getParameter("sessionId") != null ? getParameter("sessionId") : "";
        type = getParameter("type") != null ? getParameter("type") : "full";
        buttonText = getParameter("buttonText") != null ? getParameter("buttonText") : "Upload Files";
        fileFilter = getParameter("fileFilter") != null ? getParameter("fileFilter") : "";
    }

    protected void uploadFiles(List<File> files)  {
        try {
            debugDialog(this.getCodeBase().toExternalForm());
            HttpClient client = new HttpClient();
            PostMethod postMethod = new PostMethod(uploadUrl);
            postMethod.setRequestHeader("Cookie", "JSESSIONID=" + sessionId);
            List<Part> parts = new ArrayList(); 
            for (File file : files) {
                parts.add(new FilePart(file.getName(), file));
            }
            Part[] partArray = (Part[]) parts.toArray(new Part[] {});
            postMethod.setRequestEntity( new MultipartRequestEntity(partArray, 
                    postMethod.getParams()) );
            getAppletContext().showStatus("Uploading files...");
            int status = client.executeMethod(postMethod);
            String response = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection();
            postMethod = null;
            client = null;
            if (status == 200) {
                getAppletContext().showDocument(new URL(baseUrl + response));
            } else {
                StringBuffer sb = new StringBuffer(response.trim());
                for (int i = 0; i < response.length(); i++) {
                    int index = 128 * (i + 1);
                    if (index > sb.length()) {
                        index = sb.length();
                    }
                    sb.insert(index, "\n");
                }
                JOptionPane.showMessageDialog(this, status + ":  " + 
                        //((response.length() < 128) ? response : "<truncated>"),
                        sb.toString(),
                        "Server Response", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void debugDialog(String message) {
//        JOptionPane.showMessageDialog(this, message, "Debug", JOptionPane.INFORMATION_MESSAGE);
    }
}