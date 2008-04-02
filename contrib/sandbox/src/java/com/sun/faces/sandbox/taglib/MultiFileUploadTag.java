/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.MultiFileUpload;
import com.sun.faces.sandbox.util.Util;

/**
 * @author lee
 *
 */
public class MultiFileUploadTag extends UIComponentTag{
    protected String type;
    protected String fileHolder;
    protected String destinationUrl;
    protected String fileFilter;
    protected String maxFileSize;
    protected String startDir;
    protected String buttonText;
    protected String height;
    protected String width;

    @Override
    public String getComponentType() {
        return MultiFileUpload.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return MultiFileUpload.RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        MultiFileUpload upload = null;
        try {
            upload = (MultiFileUpload) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.MultiFileUpload.  Perhaps you're missing a tag?");
        }

        if (type != null) {
            if (isValueReference(type)) {
                ValueBinding vb = Util.getValueBinding(type);
                upload.setValueBinding("type", vb);
            } else {
                upload.setType(type);
            }
        }
        if (fileHolder != null) {
            if (isValueReference(fileHolder)) {
                ValueBinding vb = Util.getValueBinding(fileHolder);
                upload.setValueBinding("fileHolder", vb);
            } else {
                throw new IllegalStateException("The value for 'fileHolder' must be a ValueBinding.");
            }
        }
        if (destinationUrl  != null) {
            if (isValueReference(destinationUrl )) {
                ValueBinding vb = Util.getValueBinding(destinationUrl );
                upload.setValueBinding("destinationUrl ", vb);
            } else {
                upload.setDestinationUrl(destinationUrl );
            }
        }
        if (fileFilter != null) {
            if (isValueReference(fileFilter)) {
                ValueBinding vb = Util.getValueBinding(fileFilter);
                upload.setValueBinding("fileFilter", vb);
            } else {
                upload.setFileFilter(fileFilter);
            }
        }
        if (maxFileSize != null) {
            if (isValueReference(maxFileSize)) {
                ValueBinding vb = Util.getValueBinding(maxFileSize);
                upload.setValueBinding("maxFileSize", vb);
            } else {
                upload.setMaxFileSize(maxFileSize);
            }
        }
        if (startDir != null) {
            if (isValueReference(startDir)) {
                ValueBinding vb = Util.getValueBinding(startDir);
                upload.setValueBinding("startDir", vb);
            } else {
                upload.setStartDir(startDir);
            }
        }
        if (buttonText != null) {
            if (isValueReference(buttonText)) {
                ValueBinding vb = Util.getValueBinding(buttonText);
                upload.setValueBinding("buttonText", vb);
            } else {
                upload.setButtonText(buttonText);
            }
        }
        if (height != null) {
            if (isValueReference(type)) {
                ValueBinding vb = Util.getValueBinding(height);
                upload.setValueBinding("height", vb);
            } else {
                upload.setHeight(height);
            }
        }
        if (width != null) {
            if (isValueReference(width)) {
                ValueBinding vb = Util.getValueBinding(type);
                upload.setValueBinding("width", vb);
            } else {
                upload.setWidth(width);
            }
        }
    }
}
