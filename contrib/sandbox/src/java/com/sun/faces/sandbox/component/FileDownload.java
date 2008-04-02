/**
 * 
 */
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Jason Lee
 *
 * http://svn.sourceforge.net/viewvc/wicket-stuff/trunk/wicket-contrib-jasperreports/src/examples/java/wicket/contrib/jasperreports/examples/ReportLinksPage.java?revision=1213&view=markup
 * http://svn.sourceforge.net/viewvc/wicket-stuff/trunk/wicket-contrib-jasperreports/src/examples/java/wicket/contrib/jasperreports/examples/ReportLinksPage.html?view=markup
 * 
 *       new ResourceLink(this, "linkToPdf", new JRPdfResource(reportFile) {
 *           @Override
 *           public JRDataSource getReportDataSource() {
 *               return new ExampleDataSource();
 *           }
 *       });
 *       <a href="#" wicket:id="linkToPdf">display PDF report</a>
 */
public class FileDownload extends UIOutput {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.FileDownload";
    public static final String RENDERER_TYPE = "com.sun.faces.sandbox.FileDownloadRenderer";
    public static final String DOWNLOAD_URI = "Sandbox___Download";
    public static final String METHOD_DOWNLOAD = "download";
    public static final String METHOD_INLINE = "inline";
    public static final String REQUEST_PARAM = "componentId";
    private Object[] _state = null;
    /**
     * 
     */
    protected String fileName;
    protected String height;
    protected Boolean iframe;
    /**
     * This property determines the method with which the object is delivered to 
     * the client.  Valid options are inline or download.  If "inline" is chosen,
     * the object will be rendered in the body of the page via an <object /> tag.
     * If "download" is chosen, a link will be generated, which the user can 
     * click to download the object.
     */
    protected String method;
    
    /**
     * The user will need to supply the mime type for the download.
     */
    protected String mimeType;
    protected String text;
    /**
     * This property is the data of the actual object to be rendered/downloaded.
     */
    protected Object data;
    protected String width;
    
    public FileDownload() {
        setRendererType(RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }
    
    public String getFileName() {
        if (null != this.fileName) {
            return this.fileName;
        }
        ValueBinding _vb = getValueBinding("fileName");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }

    public String getHeight() {
        if (null != this.height) {
            return this.height;
        }
        ValueBinding _vb = getValueBinding("height");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }

    
    public Boolean getIframe() {
        if (null != this.iframe) {
            return this.iframe;
        }
        ValueBinding _vb = getValueBinding("iframe");
        return (_vb != null) ? (Boolean) _vb.getValue(getFacesContext()) : null;
    }
    public String getMethod() {
        if (null != this.method) {
            return this.method;
        }
        ValueBinding _vb = getValueBinding("method");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }
    
    public String getMimeType() {
        if (null != this.mimeType) {
            return this.mimeType;
        }
        ValueBinding _vb = getValueBinding("mimeType");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }
    public String getText() {
        if (null != this.text) {
            return this.text;
        }
        ValueBinding _vb = getValueBinding("text");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }
    
    public Object getData() {
        if (null != this.data) {
            return this.data;
        }
        ValueBinding _vb = getValueBinding("data");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }
    public String getWidth() {
        if (null != this.width) {
            return this.width;
        }
        ValueBinding _vb = getValueBinding("width");
        return (_vb != null) ? (String) _vb.getValue(getFacesContext()) : null;
    }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        method = (String) this._state[1];
        mimeType = (String) this._state[2];
        data = (Object) this._state[3];
        fileName = (String) this._state[4];
        width = (String) this._state[5];
        height = (String) this._state[6];
        iframe = (Boolean) this._state[7];
        text = (String) this._state[8];
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[9];
        }
        _state[0] = super.saveState(_context);
        _state[1] = method;
        _state[2] = mimeType;
        _state[3] = data;
        _state[4] = fileName;
        _state[5] = width;
        _state[6] = height;
        _state[7] = iframe;
        _state[8] = text;
        
        return _state;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setIframe(Boolean iframe) {
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

    public void setData(Object data) {
        this.data = data;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}