/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.FileDownload;
import com.sun.faces.sandbox.util.Util;


/**
 * @author Jason Lee
 *
 */
public class DownloadTag extends UIComponentTag {
    protected String data; 
    protected String fileName;
    protected String height;
    protected String iframe;
    protected String method;
    protected String mimeType;
    protected String text;
    protected String width;

    @Override
    public String getComponentType() {
        return FileDownload.COMPONENT_TYPE;
    }

    public String getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHeight() {
        return height;
    }

    public String getIframe() {
        return iframe;
    }

    public String getMethod() {
        return method;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getRendererType() {
        return FileDownload.RENDERER_TYPE;
    }

    public String getText() {
        return text;
    }

    public String getWidth() {
        return width;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        FileDownload download = null;
        try {
            download = (FileDownload) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.Download.  Perhaps you're missing a tag?");
        }

        if (method != null) {
            if (isValueReference(method)) {
                ValueBinding vb = Util.getValueBinding(method);
                download.setValueBinding("method", vb);
            } else {
                download.setMethod(method);
            }
        }
        if (mimeType != null) {
            if (isValueReference(mimeType)) {
                ValueBinding vb = Util.getValueBinding(mimeType);
                download.setValueBinding("mimeType", vb);
            } else {
                download.setMimeType(mimeType);
            }
        }

        if (data != null) {
            if (isValueReference(data)) {
                ValueBinding vb = Util.getValueBinding(data);
                download.setValueBinding("data", vb);
            } else {
                throw new IllegalStateException("The value for 'foo' must be a ValueBinding.");
            }
        }

        if (fileName != null) {
            if (isValueReference(fileName)) {
                ValueBinding vb = Util.getValueBinding(fileName);
                download.setValueBinding("fileName", vb);
            } else {
                download.setFileName(fileName);
            }
        }

        if (width != null) {
            if (isValueReference(width)) {
                ValueBinding vb = Util.getValueBinding(width);
                download.setValueBinding("width", vb);
            } else {
                download.setWidth(width);
            }
        }

        if (height != null) {
            if (isValueReference(height)) {
                ValueBinding vb = Util.getValueBinding(height);
                download.setValueBinding("height", vb);
            } else {
                download.setHeight(height);
            }
        }

        if (text != null) {
            if (isValueReference(text)) {
                ValueBinding vb = Util.getValueBinding(text);
                download.setValueBinding("text", vb);
            } else {
                download.setText(text);
            }
        }

        if (iframe != null) {
            if (isValueReference(iframe)) {
                ValueBinding vb = Util.getValueBinding(iframe);
                download.setValueBinding("iframe", vb);
            } else {
                download.setIframe(Boolean.parseBoolean(iframe));
            }
        }

    }
}