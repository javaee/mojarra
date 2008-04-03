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