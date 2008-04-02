/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.MultiFileUpload;

/**
 * @author lee
 *
 */
public class MultiFileUploadTag extends UISandboxComponentTag {
    protected String type;
    protected String fileHolder;
    protected String destinationUrl;
    protected String fileFilter;
    protected String maxFileSize;
    protected String startDir;
    protected String buttonText;
    protected String height;
    protected String width;

    @Override public String getComponentType() { return MultiFileUpload.COMPONENT_TYPE; }
    @Override public String getRendererType() { return MultiFileUpload.RENDERER_TYPE; }

    public void setButtonText(String buttonText)         { this.buttonText = buttonText; }
    public void setDestinationUrl(String destinationUrl) { this.destinationUrl = destinationUrl; }
    public void setFileFilter(String fileFilter)         { this.fileFilter = fileFilter; }
    public void setFileHolder(String fileHolder)         { this.fileHolder = fileHolder; }
    public void setHeight(String height)                 { this.height = height; }
    public void setMaxFileSize(String maxFileSize)       { this.maxFileSize = maxFileSize; }
    public void setStartDir(String startDir)             { this.startDir = startDir; }
    public void setType(String type)                     { this.type = type; }
    public void setWidth(String width)                   { this.width = width; }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        MultiFileUpload upload = null;
        try {
            upload = (MultiFileUpload) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.MultiFileUpload.  Perhaps you're missing a tag?");
        }

        setStringProperty(upload, "type", type);
        setValueBinding(upload, "fileHolder", fileHolder);
        setStringProperty(upload, "destinationUrl", destinationUrl );
        setStringProperty(upload, "fileFilter", fileFilter);
        setStringProperty(upload, "maxFileSize", maxFileSize);
        setStringProperty(upload, "startDir", startDir);
        setStringProperty(upload, "buttonText", buttonText);
        setStringProperty(upload, "height", height);
        setStringProperty(upload, "width", width);
    }
}
