/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;

/**
 * @author Jason Lee
 *
 */
public class MultiFileUpload extends UIOutput {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.MultiFileUpload";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.MultiFileUploadRenderer";
    public static final String[] DEP_JARS = {
        "jars/jsf-ri-sandbox-upload-applet.jar",
        "jars/commons-httpclient-3.0.1.jar",
        "jars/commons-codec-1.3.jar",
        "jars/commons-logging-1.1.jar"
    };
    public static final String JARS_URI = "/Sandbox___MultiFileUpload__jars/";
    public static final String REQUEST_PARAM = "componentId";
    public static final String TEMPLATE_FILE = "/META-INF/MultiFileUpload.template.txt";
    public static final String UPLOAD_URI = "/Sandbox___MultiFileUpload__fileupload";

    private Object[] _state = null;
    protected String buttonText = "Upload Files";
    protected String destinationUrl;
    protected String fileFilter = "";
    protected FileHolder fileHolder;
    protected String height = "250px";
    protected String maxFileSize = "0";
    protected String startDir = "/";
    protected String type = "full";
    protected String width = "500px";
    
    public MultiFileUpload()  { setRendererType(RENDERER_TYPE); }
    public String getFamily() { return COMPONENT_TYPE; }

    public String getButtonText()     { return ComponentHelper.getValue(this, "buttonText", buttonText); }
    public String getDestinationUrl() { return ComponentHelper.getValue(this, "destinationUrl", destinationUrl); }
    public String getFileFilter()     { return ComponentHelper.getValue(this, "fileFilter", fileFilter); }
    public FileHolder getFileHolder() { return ComponentHelper.getValue(this, "fileHolder", fileHolder); }
    public String getHeight()         { return ComponentHelper.getValue(this, "height", height); }
    public String getMaxFileSize()    { return ComponentHelper.getValue(this, "maxFileSize", maxFileSize); }
    public String getStartDir()       { return ComponentHelper.getValue(this, "startDir", startDir); }
    public String getType()           { return ComponentHelper.getValue(this, "type", type); }
    public String getWidth()          { return ComponentHelper.getValue(this, "width", width); }

    public void setButtonText(String buttonText)         { this.buttonText = buttonText; }
    public void setDestinationUrl(String destinationUrl) { this.destinationUrl = destinationUrl; }
    public void setFileFilter(String fileFilters)        { this.fileFilter = fileFilters; }
    public void setFileHolder(FileHolder fileHolder)     { this.fileHolder = fileHolder; }
    public void setHeight(String height)                 { this.height = height; }
    public void setMaxFileSize(String maxFileSize)       { this.maxFileSize = maxFileSize; }
    public void setStartDir(String startDir)             { this.startDir = startDir; }
    public void setType(String type)                     { this.type = type; }
    public void setWidth(String width)                   { this.width = width; }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        fileHolder     = (FileHolderImpl) this._state[1];
        destinationUrl = (String) this._state[2];
        height         = (String) this._state[3];
        maxFileSize    = (String) this._state[4];
        startDir       = (String) this._state[5];
        width          = (String) this._state[6];
        type           = (String) this._state[7];
        buttonText     = (String) this._state[8];
        fileFilter    = (String) this._state[9];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[10];
        }
        _state[0] = super.saveState(_context);
        _state[1] = fileHolder;
        _state[2] = destinationUrl;
        _state[3] = height;
        _state[4] = maxFileSize;
        _state[5] = startDir = "/";
        _state[6] = width;
        _state[7] = type;
        _state[8] = buttonText;
        _state[9] = fileFilter;
        
        return _state;
    }
}