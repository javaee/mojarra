/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.FileDownload;


/**
 * @author Jason Lee
 *
 */
public class DownloadTag extends UISandboxComponentTag {
    protected String data; 
    protected String fileName;
    protected String height;
    protected String iframe;
    protected String method;
    protected String mimeType;
    protected String text;
    protected String width;
    protected String urlVar;

    @Override public String getComponentType() { return FileDownload.COMPONENT_TYPE; }
    @Override public String getRendererType()  { return FileDownload.RENDERER_TYPE; }

    public void setData(String data)         { this.data = data; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setHeight(String height)     { this.height = height; }
    public void setIframe(String iframe)     { this.iframe = iframe; }
    public void setMethod(String method)     { this.method = method; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public void setText(String text)         { this.text = text; }
    public void setWidth(String width)       { this.width = width; }
    public void setUrlVar(String urlVar)     { this.urlVar = urlVar; }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        FileDownload download = null;
        try {
            download = (FileDownload) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.Download.  Perhaps you're missing a tag?");
        }

        setStringProperty(download, "method",method);
        setStringProperty(download, "mimeType", mimeType);
        setStringProperty(download, "fileName", fileName);
        setStringProperty(download, "width", width);
        setStringProperty(download, "height", height);
        setStringProperty(download, "text", text);
        setBooleanProperty(download, "iframe", iframe);
        setStringProperty(download, "urlVar", urlVar);
        setValueBinding(download,"data", data);
    }
}