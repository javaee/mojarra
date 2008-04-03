/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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